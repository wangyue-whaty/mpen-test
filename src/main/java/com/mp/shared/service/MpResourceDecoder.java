package com.mp.shared.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.mp.lib.so.MPFileManager;
import com.mp.lib.so.SoundResourceDecoder;
import com.mp.shared.CompatUtils;
import com.mp.shared.common.BookInfo;
import com.mp.shared.common.Code;
import com.mp.shared.common.CodeInfo;
import com.mp.shared.common.FileModule;
import com.mp.shared.common.FullBookInfo;
import com.mp.shared.common.HotArea;
import com.mp.shared.common.LearnWordStructureInfo;
import com.mp.shared.common.MpCode;
import com.mp.shared.common.PageInfo;
import com.mp.shared.common.QuickCodeInfo;
import com.mp.shared.common.ResourceVersion;
import com.mp.shared.common.ShCode;
import com.mp.shared.utils.FUtils;

/**
 * Created by feng on 3/15/17.
 *
 * 用来共享调用so库解析MP文件包
 */

public final class MpResourceDecoder {
    /**
     * TODO ZYT 使用这个生产 url
     */
    public interface UrlGenerator {
        /**
         * 根据本地 savePath 和 localFileName 来生成 一个 URL
         * @param savePath  传给解析库的本地目录，存储资源解析出的单独文件
         * @param localFileName 解析库生产的文件名
         * @return url
         */
        String getUrl(String savePath, String localFileName);
        
        String getFileSavePath(String url);
    }

    private static final String TAG = "MpResourceDecoder";

    private static final String SEP = "___";
    
    private static final String CODE_SEP = "---";
    
    private static final String SUB_SEP = "_";
    
    private static final String ENTER_SIGN = "\n";
    
    private static final String TAB_SIGN = "\t";
    
    private static final ShCode GAME_VOICE_CODE = new ShCode(0x8fffffff, ShCode.OID3_BASE);

    public static final ShCode ORAL_TEST_CODE = new ShCode(0x8ffffffe, ShCode.OID3_BASE);

    //定义了新规则下功能码值范围
    private static final int FUNCTION_LIMIE_MIN = 1570001;
    private static final int FUNCTION_LIMIE_MAX = 1572000;

    /**
     * 传递书封面码值、点读码值、语音路径和文件路径进行解码.
     *
     * @param quickCodeInfo
     *            点读码
     * @param retryDefaultLanguage
     *            如果quickCodeInfo指定的language不是0，而且解析没有结果，是不是重试 language 0
     * @param filePath
     *            资源文件路径
     * @param savePath
     *            so库解码结果文件存放根目录，假设引进存在
     * @param urlGenerator
     *            url 生成器；如果为null，LanguageInfo用soundFile；否则用 soundUrls
     */
    public static CodeInfo decode(QuickCodeInfo quickCodeInfo, boolean retryDefaultLanguage,
                                  String filePath, String savePath,
                                  UrlGenerator urlGenerator, boolean useCache) {
        final CodeInfo firstResult = decodeInternal(quickCodeInfo, filePath, savePath, urlGenerator, useCache);
        if (retryDefaultLanguage && firstResult == null && quickCodeInfo.language != 0) {
            quickCodeInfo.language = 0;
            return decodeInternal(quickCodeInfo, filePath, savePath, urlGenerator, useCache);
        }
        return firstResult;
    }

    // 当资源错误时返回此对象
    private static final CodeInfo codeInfoWithBadRes = new CodeInfo();

    /**
     * 损坏资源判断
     * @param codeInfo 解码信息对象
     * @return true or false
     */
    public static final boolean isBadResource(CodeInfo codeInfo) {
        return codeInfo == codeInfoWithBadRes;
    }

    /**
     * 传递书封面码值、点读码值、语音路径和文件路径进行解码. － 只用指定的语言
     */
    private static CodeInfo decodeInternal(QuickCodeInfo quickCodeInfo,
                                            String filePath, String savePath,
                                            UrlGenerator urlGenerator, boolean useCache) {
        if (quickCodeInfo.code == null
            || filePath == null
            || savePath == null
            || (quickCodeInfo.code == null || !quickCodeInfo.isValid())) {
            return null;
        }
        final Code code = quickCodeInfo.code;
        final HotArea.RelatedCodes hotAreaCodes = new HotArea.RelatedCodes();
        final HotArea.LanguageInfo languageInfo = new HotArea.LanguageInfo();
        //CompatUtils.impl.error(TAG, "filePath: " + filePath + " functionCode:" + quickCodeInfo.getFunctionCode() + " lang:" + quickCodeInfo.language);
        // 1. 解码
        if (code.type == Code.Type.MP) {
            if (quickCodeInfo.pageInfo == null || !quickCodeInfo.mayComputeMp()) {
                return null;
            }
        }
        final String utc = String.valueOf(quickCodeInfo.getBookInfo().version.updateTime);
        final int retVal = decodeOne(code.shCode, code.mpPoint, quickCodeInfo.language, hotAreaCodes, languageInfo,
            filePath, savePath, urlGenerator, quickCodeInfo.getFunctionCode(), quickCodeInfo.getBookInfo().id, utc, useCache);
        // 返回-2代表资源包本身格式错误，参考decodeOne内部处理
        if (BAD_RESOURCE_RET == retVal) {
            codeInfoWithBadRes.quickCodeInfo = quickCodeInfo;
            return codeInfoWithBadRes;
        }
        // 2. 继续分析返回码
        // 131075对应16进制的0x20003 中 0x20003取最高位
        final int retType = retVal >> 16;
        final int new_numVoices = retVal & 0xffffff;
        final int numVoices = retVal & 0xffff;
        final CodeInfo codeInfo = new CodeInfo();
        codeInfo.quickCodeInfo = quickCodeInfo;
        codeInfo.hotAreaCodes = hotAreaCodes;
        switch (retType) {
        //0x0f17: 0f: 特殊功能， 17：功能值
        case 0x0f17:
            codeInfo.languageInfos = new HotArea.LanguageInfo[1];
            // 最新功能码定义
            //todo 需要将新的功能码静态定义出来
            languageInfo.numFunc = (new_numVoices >= FUNCTION_LIMIE_MIN && new_numVoices <= FUNCTION_LIMIE_MAX) ? new_numVoices : -1;
            final String extraPath = urlGenerator == null ? languageInfo.getVoice() : urlGenerator.getFileSavePath(languageInfo.getVoice());
            if(extraPath != null && (extraPath.endsWith(".txt")||extraPath.endsWith(".json"))){
                languageInfo.extra = FUtils.fileToString(extraPath);
            }
            codeInfo.languageInfos[0] = languageInfo;
            break;
        //0x0f04: 0f: 特殊功能， 04：功能值
        case 0x0f04:
            codeInfo.languageInfos = new HotArea.LanguageInfo[1];
            languageInfo.funciton = (numVoices >= 1001 && numVoices <= 1010) ? HotArea.FUNCTIONS[numVoices - 1001] : null;
            final String path = urlGenerator == null ? languageInfo.getVoice() : urlGenerator.getFileSavePath(languageInfo.getVoice());
            if(path != null && path.endsWith(".txt")){
                languageInfo.extra = FUtils.fileToString(path);
            }
            codeInfo.languageInfos[0] = languageInfo;
            break;
        case 4:
            languageInfo.videoInfo = CodeInfo.VIDEO_TRUE; // TODO 获取 video URL
            codeInfo.languageInfos = new HotArea.LanguageInfo[1];
            codeInfo.languageInfos[0] = languageInfo;
            break;
        case 3:
            codeInfo.isGame = true;
            codeInfo.languageInfos = new HotArea.LanguageInfo[numVoices];
            codeInfo.languageInfos[quickCodeInfo.language] = languageInfo;
            if (!decodeAll(code.shCode, code.mpPoint, quickCodeInfo.language, codeInfo.languageInfos, filePath,
                savePath, urlGenerator, numVoices - 1, quickCodeInfo.getFunctionCode(), quickCodeInfo.getBookInfo().id, utc, useCache)) {
                return null;
            }

            // continue to decode the special voice code
            final HotArea.RelatedCodes gameVoiceCodes = new HotArea.RelatedCodes();
            final HotArea.LanguageInfo gameVoice0 = new HotArea.LanguageInfo();
            final int gameVoiceCodeRet = decodeOne(GAME_VOICE_CODE, null, 0, gameVoiceCodes, gameVoice0, filePath,
                savePath, urlGenerator, quickCodeInfo.getFunctionCode(), quickCodeInfo.getBookInfo().id, utc, useCache);
            final int gameVoiceType = gameVoiceCodeRet >> 16;   
            final int numGameVoices = gameVoiceCodeRet & 0xffff;
            if (gameVoiceType != 3 && numGameVoices > 2 && numGameVoices < 30) {
                CompatUtils.impl.error(TAG, "数据错误：gameVoiceCodeRet:" + gameVoiceCodeRet);
                return null;
            }
            final HotArea.LanguageInfo[] gamePromptVoices = new HotArea.LanguageInfo[numGameVoices];
            gamePromptVoices[0] = gameVoice0;
            if (!decodeAll(GAME_VOICE_CODE, null, 0, gamePromptVoices, filePath, savePath,
                    urlGenerator, -1, quickCodeInfo.getFunctionCode(), quickCodeInfo.getBookInfo().id, utc, useCache)) {
                return null;
            }

            // codeInfo.languageInfos 的最后一项是游戏的二进制文件；前面的是游戏问题语音
            // gamePromptVoices 是那些 0x8fffffff代表的语音
            final String gameFile = codeInfo.languageInfos[numVoices - 1].getVoice();
            try {

                codeInfo.gameInfo = GameParser.build(gamePromptVoices, codeInfo.languageInfos,
                        new File(gameFile));
            } catch (Exception e) {
                CompatUtils.impl.error(TAG, "Parse GameInfo failed for " + gameFile, e);
                return null;
            }
            break;
        // 0x0f00  0f: 特殊功能， 00： 无功能值
        case 0x0f00:
        case 2:
        case 1:
            codeInfo.languageInfos = new HotArea.LanguageInfo[1];
            codeInfo.languageInfos[0] = languageInfo;
            break;
        default:
            return null;
        }
        if (quickCodeInfo.bookInfo != null && codeInfo.hotAreaCodes != null && codeInfo.hotAreaCodes.shCodes != null) {
            if (codeInfo.hotAreaCodes.shCodes[0].isIn(quickCodeInfo.bookInfo.coverCodes)) {
                codeInfo.isCover = true;
            }
        }
        codeInfo.isRegular = !codeInfo.isCover && !codeInfo.isGame;
        return codeInfo;
    }

    /**
     * decode and get all the languageInfos from resource file for the given
     * code
     */
    private static boolean decodeAll(ShCode shCode, MpCode.Point mpPoint, int decodedNum,
            HotArea.LanguageInfo[] languageInfos, String resourceFile, String savePath,
            UrlGenerator urlGenerator, int skipUrlGeneratorIdx, int functionCode, String bookId, String utc, boolean useCache) {
        final int numVoices = languageInfos.length;
        for (int idx = 0; idx < numVoices; ++idx) {
            if (idx != decodedNum) {
                final HotArea.LanguageInfo oneLanguageInfo = new HotArea.LanguageInfo();
                final int thisRet = decodeOne(shCode, mpPoint, idx, null, oneLanguageInfo,
                        resourceFile, savePath,
                        skipUrlGeneratorIdx == idx ? null : urlGenerator, functionCode, bookId, utc, useCache);
                if (thisRet <= 0xffff) {
                    CompatUtils.impl.error(TAG, "数据错误：同样解码返回值无效：" + thisRet);
                    return false;
                }
                languageInfos[idx] = oneLanguageInfo;
            }
        }
        return true;
    }

    private static final int BAD_RESOURCE_RET = -1;
    private static final int OTHER_RET = -2;
    private static final String STR_BAD_RESOURCE_RET = "-1";

    /**
     * decode and get one hotAreaCodes and one languageInfo from resource file
     * for the given code
     */
    private static int decodeOne(ShCode shCode, MpCode.Point mpPoint, int language, HotArea.RelatedCodes hotAreaCodes,
        HotArea.LanguageInfo languageInfo, String resourceFile, String savePath, UrlGenerator urlGenerator, int functionCode, String bookId, String utc, boolean useCache) {
        //CompatUtils.impl.error(TAG, "soDecode, shCode is null: " + (shCode == null));
        final byte[] ret = soDecode(shCode, mpPoint, language,  functionCode,resourceFile, savePath, bookId, utc, useCache);

        // 2. 检查 String -- TODO 需要修改返回规范结果
        final String str = decodeByteArray(ret);
        //CompatUtils.impl.error(TAG, "soDecode:"+str);
        if (str == null || str.isEmpty()) {
            return OTHER_RET;
        }
        if (STR_BAD_RESOURCE_RET.equals(str)) {
            return BAD_RESOURCE_RET;
        }
        final String[] parts = str.split(SEP); // TODO KM 规范化返回
        if (parts == null || parts.length < 2) {
            return OTHER_RET;
        }
        final int retVal = Integer.parseInt(parts[0]);
        if (retVal == 0 || retVal == 65535) {
            return OTHER_RET;
        }
        // 3. 解析返回String 各个部分
        if (!parseParts(shCode, parts, language, savePath, hotAreaCodes, languageInfo, urlGenerator)) {
            return OTHER_RET;
        }
        return retVal;
    }

    /**
     * helper to parse the parts[] and fill in hotAreaCodes, languageInfo use
     * shCode == null to decide whether it decodeByCode or decodeByXY
     * 
     * @return true if everything is ok
     */
    private static boolean parseParts(ShCode shCode, String[] parts, int language, String savePath,
        HotArea.RelatedCodes hotAreaCodes, HotArea.LanguageInfo languageInfo, UrlGenerator urlGenerator) {
        // TODO 更好的处理语言显示
        languageInfo.language = (language == 0 ? "English" : "中文");
        // 返回示例：131075___Yes, please. 131075对应16进制的0x20003 2代表语音类型
        // 3代表一共有几份语音资料（中文、英文、文本）
        String localFileName = null;
        if (shCode == null) {
            if (parts.length < 3) {
                return false;
            }
            if (parts.length == 5) {
                languageInfo.text = parts[4];
            }
            if (hotAreaCodes != null) {
                final String[] codeInfos = parts[1].split(CODE_SEP);
                hotAreaCodes.shCodes = new ShCode[codeInfos.length];
                for(int i = 0; i < codeInfos.length; i++){
                    final String[] codeInfo = codeInfos[i].split(SUB_SEP);
                    hotAreaCodes.shCodes[i] = new ShCode(Integer.parseInt(codeInfo[0]), Integer.parseInt(codeInfo[1]));
                }
                hotAreaCodes.id = parts[2];
            }
            if(parts.length > 3){
                localFileName = parts[3];
            }
        } else {
            if (parts.length < 2) {
                return false;
            }
            if (parts.length == 4) {
                languageInfo.text = parts[3];
            }
            if (hotAreaCodes != null) {
                final String[] codeInfos = parts[1].split(CODE_SEP);
                hotAreaCodes.shCodes = new ShCode[codeInfos.length];
                for(int i = 0; i < codeInfos.length; i++){
                    final String[] codeInfo = codeInfos[i].split(SUB_SEP);
                    hotAreaCodes.shCodes[i] = new ShCode(Integer.parseInt(codeInfo[0]), Integer.parseInt(codeInfo[1]));
                }
            }
            if(parts.length > 2){
                localFileName = parts[2];
            }
        }
        if (localFileName != null) {
            if (urlGenerator == null) {
                languageInfo.soundFile = savePath + "/" + localFileName;
            } else {
                languageInfo.soundUrls = new String[1];
                languageInfo.soundUrls[0] = urlGenerator.getUrl(savePath, localFileName);
            }
        }
        return true;
    }

    /**
     * convenient method to call the native method if shCode is not null, call
     * the mpParseMPByCode; otherwise call mpParseMPByXY
     */
    private static byte[] soDecode(ShCode shCode, MpCode.Point mpPoint, int language, int functionCode ,String resourceFile,
        String savePath, String bookId, String utc, boolean useCache) {
        return shCode == null ? (useCache ? SoundResourceDecoder.mpParseMPByXYCache(mpPoint.pageNum, mpPoint.x, mpPoint.y, language, functionCode, resourceFile, savePath, bookId, utc):
            SoundResourceDecoder.mpParseMPByXY(mpPoint.pageNum, mpPoint.x, mpPoint.y, language, functionCode, resourceFile, savePath)) :
                SoundResourceDecoder.mpParseMPByCode(shCode.code, language, functionCode, resourceFile,savePath);
    }

    /**
     * 解码byte[] 到 String，尝试 UTF8 和 GBK encoding
     * @return null if failed
     */
    private static String decodeByteArray(byte[] bytes) {
        String str = null;
        try {
            str = new String(bytes, "UTF8"); // TODO KM GBK 还是 UTF8 －－ 最终需要是 UTF8
        } catch (UnsupportedEncodingException exception) {
            CompatUtils.impl.error("ResourceDecoder", "so返回编码错误 - 不是 UTF8");
            try {
                str = new String(bytes, "GBK"); // TODO KM GBK 还是 UTF8 －－ 最终需要是
            } catch (UnsupportedEncodingException e) {
                CompatUtils.impl.error("ResourceDecoder", "so返回编码错误 - 不是 GBK");
            }
        }
        return str;
    }

    /**
     * 获取书籍信息.
     * 
     */
    public static boolean getFullBookInfo(FullBookInfo fullBookInfo, String filePath) {
        //CompatUtils.impl.error(TAG, "mpGetFileInfo:"+filePath+"-"+bookInfo.id);
        final byte[] ret = SoundResourceDecoder.mpGetFileInfo(filePath);
        final String str = decodeByteArray(ret);
        //CompatUtils.impl.error(TAG, "mpGetFileInfo:"+str);
        if (str == null || "-1".equals(str)) {
            CompatUtils.impl.error(TAG, "解码获取文件信息异常");
            return false;
        }
        final String[] fileInfos = str.split(SEP);
        if (fileInfos.length < 10) {
            CompatUtils.impl.error(TAG, "解码获取文件信息异常");
            return false;
        }
        final BookInfo bookInfo = fullBookInfo.bookInfo;
        try {
            String[] coverCodeInfos = fileInfos[0].split(CODE_SEP);
            bookInfo.coverCodes = new ShCode[coverCodeInfos.length];
            for(int i=0; i<coverCodeInfos.length; i++){
                String[] coverCodeInfo = coverCodeInfos[i].split(SUB_SEP);
                bookInfo.coverCodes[i]=new ShCode(Integer.parseInt(coverCodeInfo[0]),
                    Integer.parseInt(coverCodeInfo[1]));
            }
            bookInfo.version = new ResourceVersion(Long.parseLong(fileInfos[1]),
                Integer.parseInt(fileInfos[2]));
            final ShCode.Range contentRange = getCodeRange(fileInfos[3]);
            final ShCode.Range gameRange = getCodeRange(fileInfos[4]);
            ShCode.Range[] rang = null;
            if(contentRange != null && gameRange != null){
                rang = new ShCode.Range[2];
                rang[0] = contentRange;
                rang[1] = gameRange;
            }else if(contentRange != null && gameRange == null){
                rang = new ShCode.Range[1];
                rang[0] = contentRange;
            }
            bookInfo.shCodeRanges = rang;
            fullBookInfo.isbn = fileInfos[5];
            // TODO 老孔包中名称不对，需要修正，由于预下载书籍激活时需要校对包中的名称，所以BookList中书名还是使用包中名称
            bookInfo.name = fileInfos[6];
            bookInfo.publisher = fileInfos[7];
            String[] languages = fileInfos[8].split(CODE_SEP);
            bookInfo.languages = new String[languages.length];
            for (int i = 0; i < languages.length; i++) {
                bookInfo.languages[i] = languages[i];
            }
            bookInfo.hasSpeakingEvaluation = "1".equals(fileInfos[9]) ? true : false;
            bookInfo.hasVideo = "1".equals(fileInfos[10]) ? true : false;
            return true;
        } catch (Exception e) {
            CompatUtils.impl.error(TAG, bookInfo.id + bookInfo.name + "解码获取文件信息异常", e);
            return false;
        }
    }
    
    /**
     * TODO 根据返回值获取码值范围
     */
    private static ShCode.Range getCodeRange(String rangeStr){
        if(rangeStr.length() == 0){
            return null;
        }
        String[] codeScope = rangeStr.split(CODE_SEP);
        if(codeScope.length != 2){
            return null;
        }
        final String[] endCodeInfo = codeScope[1].split(SUB_SEP);
        ShCode.Range range = new ShCode.Range(Integer.parseInt(codeScope[0]), Integer.parseInt(endCodeInfo[0]), Integer.parseInt(endCodeInfo[1]));
        return range;
    }
    
    /**
     * 单词学习获取目录结构
     * 
     */
    public static LearnWordStructureInfo getLearnWordStructure(FullBookInfo fullBookInfo, String filePath, String savePath) {
        //CompatUtils.impl.error(TAG, "getLearnWordStructure mpParseMPByCode:" + filePath + " -- " + savePath);
        final byte[] ret = SoundResourceDecoder.mpParseMPByCode(0x8ffffffe, 0, QuickCodeInfo.FUNCTION_CODE_MIN, filePath, savePath);
        //CompatUtils.impl.error(TAG, "getLearnWordStructure mpParseMPByCode:"+(ret==null?-1:ret.length));
        final String str = decodeByteArray(ret);
        //CompatUtils.impl.error(TAG, "getLearnWordStructure mpParseMPByCode:"+str);
        if (str == null || str.isEmpty()) {
            return null;
        }
        final String[] fileInfos = str.split(SEP);
        if (fileInfos.length < 2) {
            CompatUtils.impl.error(TAG, "解码获取文件信息异常");
            return null;
        }
        final int retType = Integer.parseInt(fileInfos[0]) >> 16;
        if (retType != 1) {
            return null;
        }
        final String fileStr = FUtils.fileToString(savePath + "/" + fileInfos[2]);
        final String[] structureStr = fileStr.split(ENTER_SIGN);
        final int length = structureStr.length;
        final LearnWordStructureInfo.UnitInfo[] unitInfos = new LearnWordStructureInfo.UnitInfo[length];
        for (int i = 0; i < length; i++) {
            String[] infos = structureStr[i].split(TAB_SIGN);
            if (infos.length != 3) {
                continue;
            }
            unitInfos[i] = new LearnWordStructureInfo.UnitInfo(infos[0],
                new ShCode.Range(Integer.parseInt(infos[1]), Integer.parseInt(infos[2].trim()), ShCode.WYS_SUBTYPE));
        }
        return new LearnWordStructureInfo(fullBookInfo.bookInfo.id, unitInfos, fullBookInfo.bookInfo.version, fullBookInfo.sequence);
    }
    
    /**
     * TODO 大文件切分成小文件提供下载
     * 
     */
    public static List<FileModule> getFileMoudles(String path, String savePath) {
        final byte[] ret = MPFileManager.splitFile(path, savePath, 0);
        final String str = decodeByteArray(ret);
        if (str == null || str.isEmpty()) {
            return null;
        }
        final String[] fileInfos = str.split(SEP);
        final int fileNum = Integer.valueOf(fileInfos[0]);
        if (fileInfos.length < 2 || fileNum <= 0) {
            CompatUtils.impl.error(TAG, "解码异常");
            return null;
        }
        final String[] fileInfoStr = fileInfos[1].split(CODE_SEP);
        final int length = fileInfoStr.length;
        final List<FileModule> fileModules = new ArrayList<FileModule>();
        for (int i = 0; i < length; i++) {
            final String[] infos = fileInfoStr[i].split(SUB_SEP);
            if (infos.length != 3) {
                CompatUtils.impl.error(TAG, "解码异常！");
                return null;
            }
            final FileModule fileModule = new FileModule(infos[0], infos[2],Long.valueOf(infos[1]),
                savePath + "/" + infos[0]);
            fileModules.add(fileModule);
        }
        return fileModules;
    }

    /**
     * 获取口语考试卷quickCodeInfo信息
     */
    public static QuickCodeInfo getOralTestQuickCodeInfo(BookInfo bookInfo, int pageNum) {
        final QuickCodeInfo quickCodeInfo = new QuickCodeInfo();
        quickCodeInfo.pageInfo = new PageInfo();
        quickCodeInfo.bookInfo = new BookInfo();
        quickCodeInfo.bookInfo.id = bookInfo.id;
        quickCodeInfo.code = new Code();
        quickCodeInfo.code.type = Code.Type.MP;
        // TODO 临时设置的一些值，屏蔽MpResourceDecoder类 decodeInternal方法中quickCodeInfo.isValid()条件，为了调到MPResourceDecoder类中soDecode()方法中SoundResourceDecoder.SoundResourceDecoder.mpParseMPByXY（）方法
        quickCodeInfo.code.prx = 1;
        quickCodeInfo.code.pry = 1;
        quickCodeInfo.code.mpCode = new MpCode(1, 0, (byte) 5);
        // 解析口语考试MP包中的exam.json文件,mpPoint.x=375,mpPoint.y=125为固定值。
        quickCodeInfo.code.mpPoint = new MpCode.Point();
        quickCodeInfo.code.mpPoint.x = 375;
        quickCodeInfo.code.mpPoint.y = 125;
        quickCodeInfo.code.mpPoint.pageNum = pageNum;
        return quickCodeInfo;
    }
}

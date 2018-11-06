package com.mp.shared.service;

import com.mp.shared.common.GameInfo;
import com.mp.shared.common.HotArea;
import com.mp.shared.common.ShCode;
import com.mp.shared.common.Utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 4/7/17.
 *
 * 分析游戏二进制文件，解析出 GameInfo
 *
 * 这个分析版本会严格检测数据，即使无用数据，也会检测格式或者期望值
 */

public final class GameParser {
    private static final String TAG = "GameParser";

    /**
     * self defined IOException，表示二进制数据解析错误
     */
    public static final class WrongDataFormatException extends IOException {
        public WrongDataFormatException(String cause) {
            super(cause);
        }
    }

    /**
     *
     * @param gamePromptVoices  promptVoices 是从 0x8fffffff 码过来的
     * @param gameVoices  是从游戏码过来的
     * @param gameBinaryFile 一般是游戏码最后一个解析文件
     * @return
     * @throws IOException
     * @throws IndexOutOfBoundsException
     */
    public static GameInfo build(HotArea.LanguageInfo[] gamePromptVoices,
                                 HotArea.LanguageInfo[] gameVoices,
                                 File gameBinaryFile)
            throws IOException, IndexOutOfBoundsException {
        if (Utils.isEmpty(gamePromptVoices) || Utils.isEmpty(gameVoices)) {
            throw new WrongDataFormatException("Input is empty");
        }
        // 1. 建立全局数据
        final ByteBuffer bb = ByteBuffer.wrap(toByteArray(gameBinaryFile));
        bb.order(ByteOrder.LITTLE_ENDIAN);
        final Data data = Data.build(gamePromptVoices, gameVoices, bb);
        final GameInfo gameInfo = new GameInfo();
        final int[] voiceIdxBuffer = new int[10];  // 10 is big enough
        final int QUESTION_DATA_OFFSET = 0x2A;  // 从 gameStartOffset 开始
        final int REGULAR_QUESTION_DATA_LEN = 0x32;
        // 2. 验证游戏头部
        {
            bb.position(data.gameStartOffset);
            final int startNum = bb.getInt();
            final int numStages = bb.getShort();
            if (startNum != 0x0008060E || numStages != 1) {
                throw new WrongDataFormatException("wrong game data header:" + startNum + " : " + numStages);
            }
            bb.position(bb.position() + 4);  // skip 4 bytes
            final String enterGameVoiceIdx = data.getVoices(bb.get(), bb.get(), 1)[0];
            //data.checkVoice00(enterGameVoiceIdx);
            final String leaveGameVoice = data.getVoices(bb.get(), bb.get(), 1)[0];
        }
        // 3. 解析关口头部
        {
            bb.position(data.gameStartOffset + 16);
            final int enterOid = bb.getInt();
            checkNum(enterOid, 0, "进入这一关的 OID 码");
            // voices
            final String enterStageVoice = data.getVoices(bb.get(), bb.get(), 1)[0];
            data.checkBlankVoice(enterStageVoice);
            final String leaveStageVoice = data.getVoices(bb.get(), bb.get(), 1)[0];
            final String successEndVoice = data.getVoices(bb.get(), bb.get(), 1)[0];
            final String failureEndVoice = data.getVoices(bb.get(), bb.get(), 1)[0];
            final String allDoneSummaryVoice = data.getVoices(bb.get(), bb.get(), 1)[0];
            data.checkBlankVoice(allDoneSummaryVoice);
            // num questions and answers
            final int numQuestions = bb.getShort();
            final int numQuestionsToAnswer = bb.getShort();
            if (numQuestionsToAnswer <= 0 || numQuestions < numQuestionsToAnswer) {
                throw new WrongDataFormatException("numQuestions and numQuestionsToAnswer");
            }
            final boolean isQuestionOrdered = bb.getShort() == 1;
            // set GameInfo values
            gameInfo.welcomeVoice = data.gameVoices[0].getVoice();
            gameInfo.leaveGameVoice = leaveStageVoice;
            gameInfo.successEndVoice = successEndVoice;
            gameInfo.failureEndVoice = failureEndVoice;
            gameInfo.numQuestionsToAnswer = numQuestionsToAnswer;
            gameInfo.questionAnswers = new GameInfo.QuestionAnswer[numQuestions];
            gameInfo.isQuestionOrdered = isQuestionOrdered;
        }
        // 4 解析每个问题
        bb.position(data.gameStartOffset + QUESTION_DATA_OFFSET);
        final int originalNumQuestions = gameInfo.questionAnswers.length;
        for (int qIdx = 0; qIdx < originalNumQuestions; ++qIdx) {
            // 问题类型
            final int questionType = bb.getShort();  // 1 为问答,2 为迷宫,3 为连线,4 为按顺序答题
            if (questionType == 1) {
                gameInfo.questionAnswers[qIdx] = new GameInfo.QuestionAnswer();
                final GameInfo.QuestionAnswer curQa = gameInfo.questionAnswers[qIdx];  // short hand
                parseRegularQuestionAnswer(data, bb, curQa, qIdx);
                // 跳到下个问题数据开始
                bb.position(data.gameStartOffset + QUESTION_DATA_OFFSET
                        + REGULAR_QUESTION_DATA_LEN * (qIdx + 1));
            } else if (questionType == 4) {
                // TODO ZYT questionType 1 和 4 数据分析是一样的还是不一样的？
            } else if (questionType == 3) {
                checkNum(gameInfo.questionAnswers.length, 1, "连线题 超过一个问题");
                // 1. 问题头部
                final String questionVoice = data.getVoices(bb.get(), bb.get(), 1)[0];  // skip -- 问题语音
                final String[] choosenPromptVoices = data.getVoices(bb.get(), bb.get(), 1);
                final String[] allPlayedPromptVoices = data.getVoices(bb.get(), bb.get(), 1);
                final String[] failedPromptVoices = data.getVoices(bb.get(), bb.get(), 1);
                final int numSubQuestions = bb.getShort();
                final int allowWrongNum = bb.getShort();
                final int subQuestionStartOffset = bb.getInt() + data.gameStartOffset;
                final int subQuestionDataLen = bb.getShort() * 2;
                gameInfo.questionAnswers = new GameInfo.QuestionAnswer[numSubQuestions];
                gameInfo.numQuestionsToAnswer = numSubQuestions;
                gameInfo.isUserDefinedOrder = true;
                // 2. 分组
                for (int subIdx = 0; subIdx < numSubQuestions; ++subIdx) {
                    gameInfo.questionAnswers[subIdx] = new GameInfo.QuestionAnswer();
                    final GameInfo.QuestionAnswer curQa = gameInfo.questionAnswers[subIdx];  // short hand
                    // 跳到问题数据开始
                    bb.position(subQuestionStartOffset + subIdx * subQuestionDataLen);
                    // 开始解析
                    final int firstCode = bb.getInt();
                    final String firstCodeVoice = data.getVoices(bb.get(), bb.get(), 1)[0];
                    final String[] correctAnsweredPrompt = data.getVoices(bb.get(), bb.get(), 1);
                    final String[] wrongPromptVoices = getPromptVoices(data, bb);
                    final int numFollowupCodes = bb.getShort();
                    checkNum(numFollowupCodes, 1, "连线题答案数量");
                    final int followupCodesStartOffset = bb.getInt() + data.gameStartOffset;
                    final String[] answerPromptVoices = getPromptVoices(data, bb);
                    //data.checkBlankVoice(answerPromptVoices[0]); // 一般为空不用；有时连线题这个地方也会有其它无用语音
                    // 填充curQa
                    curQa.questionVoice = firstCodeVoice;
                    curQa.correctAnwseredVoice = correctAnsweredPrompt[0];
                    {
                        curQa.answerCodes = new GameInfo.AnswerCode[numFollowupCodes + 1];
                        curQa.answerCodes[0] = data.getAnswerCode(firstCode);
                        curQa.answerCodes[0].playCodeOwnVoice = true;
                        final int savedPosition = bb.position();
                        for (int codeIdx = 0; codeIdx < numFollowupCodes; ++codeIdx) {
                        	try {
                                curQa.answerCodes[codeIdx + 1] = data.getAnswerCode(
                                        bb.getInt(followupCodesStartOffset + codeIdx * 4));
                                curQa.answerCodes[codeIdx + 1].playCodeOwnVoice = true;
                        	} catch (IndexOutOfBoundsException e) {
                        		throw new WrongDataFormatException(
                        				"＃" + qIdx + " 连线问题解析，答案码超过文件长度。" + numFollowupCodes
                        				+ " 个问题，从偏移 " + followupCodesStartOffset + " (文件长度:"
                        				+ bb.capacity() + ")");
                        	}
                        }
                        bb.position(savedPosition);
                    }
                    curQa.numGoodAnswersToPass = numFollowupCodes + 1;
                    curQa.isAnswerOrdered = true;

                    //curQa.correctVoices = correctAnsweredPrompt;
                    curQa.wrongVoices = wrongPromptVoices;
                    curQa.choosenVoices = choosenPromptVoices;
                    curQa.allowWrongNum = allowWrongNum;
                }
            } else {  // questionType == 2
                throw new WrongDataFormatException("不支持 迷宫 问题");
            }
        }
        if (!gameInfo.isGameInfoOk()) {
            throw new WrongDataFormatException("parsed GameInfo invalid");
        }
        return gameInfo;
    }

    /**
     * 检查 short num是给定数字；否则 throw
     */
    private static  void checkNum(short num, short expected, String errorMsg) throws IOException {
        if (num != expected) {
            throw new WrongDataFormatException(errorMsg + " -- " + num + " VS " + expected);
        }
    }
    /**
     * 检查 int num是给定数字；否则 throw
     */
    private static  void checkNum(int num, int expected, String errorMsg) throws IOException {
        if (num != expected) {
            throw new WrongDataFormatException(errorMsg + " -- " + num + " VS " + expected);
        }
    }

    /**
     * 解析一个类型 1 问题
     * @param data
     * @param bb
     * @param curQa
     * @param qIdx
     * @throws IOException
     * @throws IndexOutOfBoundsException
     */
    private static void parseRegularQuestionAnswer(Data data, ByteBuffer bb,
                                                   GameInfo.QuestionAnswer curQa, int qIdx)
            throws IOException, IndexOutOfBoundsException {
        bb.getShort();  // skip 2
        // 问题语音：如果是 0 就不设置
        final int questionVoiceIdx = bb.getShort();
        if (questionVoiceIdx > 0) {
            curQa.questionVoice = data.gameVoices[questionVoiceIdx].getVoice();
        } // else == 0: 和welcomeVoice重复
        // 要答对多少个答案才通过本题
        curQa.numGoodAnswersToPass = bb.getShort();
        // 答案是否是随机的，true则为顺序答案（对应顺序回答的问题），false为乱序答案（对应乱序回答的问题
        curQa.isAnswerOrdered = bb.getShort() != 0;
        // 答对语音；0 表示随机选；应该是0
        final int correctVoicePromptIdx = bb.getShort();
        checkNum(correctVoicePromptIdx, 0, "答对语音；0 表示随机选；应该是0");
        // 允许错误次数
        curQa.allowWrongNum = bb.getShort();
        if (curQa.allowWrongNum <= 0) {
            throw new WrongDataFormatException("allowWrongNum 错误：" + curQa.allowWrongNum);
        }
        // 正确答案
        final int numGoodAnswers = bb.getShort();
        curQa.answerCodes = new GameInfo.AnswerCode[numGoodAnswers];
        final int answerCodeOffset = bb.getInt() + data.gameStartOffset;
        if (numGoodAnswers <= 0 || answerCodeOffset < data.gameStartOffset) {
            throw new WrongDataFormatException("good answer data incorrect");
        } else {
            // 继续填充答案码
            final int savedPosition = bb.position();
        	try {
	            for (int codeIdx = 0; codeIdx < numGoodAnswers; ++codeIdx) {
	                curQa.answerCodes[codeIdx] = data.getAnswerCode(
	                        bb.getInt(answerCodeOffset + codeIdx * 4));
	            }
        	} catch (IndexOutOfBoundsException e) {
        		throw new WrongDataFormatException(
        				"＃" + qIdx + " 问答问题解析，答案码超过文件长度。" + numGoodAnswers
        				+ " 个问题，从偏移 " + answerCodeOffset + " (文件长度:"
        				+ bb.capacity() + ")");
        	}
            bb.position(savedPosition);
        }
        // 错误答案 －－ 无用
        final int numWrongAnswers = bb.getShort();
        checkNum(numWrongAnswers, 0, "num wrong answers, not used, should be 0");
        final int wrongAnswerCodeOffset = bb.getInt();
        //checkNum(wrongAnswerCodeOffset, data.gameStartOffset, "wrong answer offset, shoud be gameStartOffset");
        // 正确答案提示音
        curQa.correctVoices = getPromptVoices(data, bb);
        // 已选答案提示音
        curQa.choosenVoices = getPromptVoices(data, bb);
        // 回答错误码 提示音 －－ 无用
        if (getPromptVoices(data, bb) != null) {
            throw new WrongDataFormatException("回答错误码提示音不为空");
        }
        // 选择错误提示音
        curQa.wrongVoices = getPromptVoices(data, bb);
        // 两个无用的：找到最后一个问题之后的总结提示音；最后 1 次机会用完的答错提示音
        final String[] summaryPromptVoices = getPromptVoices(data, bb);
        //data.checkBlankVoice(summaryPromptVoices[0]);
        final String[] lastWrongPromptVoices = getPromptVoices(data, bb);
        //data.checkBlankVoice(lastWrongPromptVoices[0]);
        if (curQa.correctVoices.length == 0 || curQa.choosenVoices.length != 1
                || curQa.wrongVoices.length == 0
                || summaryPromptVoices.length != 1
                || lastWrongPromptVoices.length != 1) {
            throw new WrongDataFormatException("分析各种提示语音数据；问题＃" + qIdx);
        }
    }

    /**
     * 格式是： 两个字节语音个数； ＋ 一个字节文件偏移 ＋ 0x80
     */
    private static String[] getPromptVoices(Data data, ByteBuffer bb) throws IOException {
        final int numPrompts = bb.getShort();
        if (numPrompts > 0) {
            return data.getVoices(bb.get(), bb.get(), numPrompts);
        } else if (numPrompts == 0) {
            checkNum(bb.getShort(), (short) 0xFFFF, "应该是 FF FF");
            return null;
        } else {
            throw new WrongDataFormatException("prompt voices should >= 0: " + numPrompts);
        }
    }



    /**
     * 读取文件内容，转换为字节数组
     * TODO 放在 shared.common.Utils
     * @param file 文件
     * @return 转换后的字节数组
     * @throws IOException
     */
    public static byte[] toByteArray(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath());
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, bufSize))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bos.close();
        }
    }

    /**
     * 放各种相关数据
     */
    private static class Data {
        final HotArea.LanguageInfo[] gamePromptVoices;
        final HotArea.LanguageInfo[] gameVoices;
        final int numPrompts;
        final int numVoices;
        final Map<Integer, HotArea.Area> hotAreas;
        final Map<Integer, List<Integer>>  relatedCodesMap;
        final int audiosStartOffset;
        final int gameStartOffset;
        final Map<Integer, Short> promptVoicesMap;
        Data(HotArea.LanguageInfo[] gamePromptVoices, HotArea.LanguageInfo[] gameVoices,
             Map<Integer, HotArea.Area> hotAreas, int audiosStartOffset, int gameStartOffset,
             Map<Integer, Short> promptVoicesMap, Map<Integer, List<Integer>>  relatedCodesMap) {
            this.gamePromptVoices = gamePromptVoices;
            this.gameVoices = gameVoices;
            numPrompts = gamePromptVoices.length;
            numVoices = gameVoices.length - 1;  // last one is game binary file, not voice file
            this.hotAreas = hotAreas;
            this.audiosStartOffset = audiosStartOffset;
            this.gameStartOffset = gameStartOffset;
            this.promptVoicesMap = promptVoicesMap;
            this.relatedCodesMap = relatedCodesMap;
        }

        /**
         * 构建一个 Data 数据结构
         * @throws IOException
         * @throws IndexOutOfBoundsException
         */
        static Data build(HotArea.LanguageInfo[] gamePromptVoices, HotArea.LanguageInfo[] gameVoices,
                          ByteBuffer bb) throws IOException, IndexOutOfBoundsException {
            // 1. 分析热区
            final int hostAreasStartPos = bb.getShort();
            final Map<Integer, HotArea.Area> hotAreas = getHotArea(bb, hostAreasStartPos);
            final Map<Integer, List<Integer>> relatedCodesMap = getRelatedCodes(bb);
            // 2. 索引开始地址：语音，游戏内容
            bb.position(6);
            final int audiosStartOffset = bb.getInt(); // offset 6
            final int gameStartOffset = bb.getInt();  // offset 10
            // 3. 解析 语音
            final Map<Integer, Short> promptVoicesMap = getVoiceMap(bb, audiosStartOffset, gameStartOffset);
            return new Data(gamePromptVoices, gameVoices, hotAreas, audiosStartOffset,
                    gameStartOffset, promptVoicesMap, relatedCodesMap);
        }

        /**
         * helper 获取 gameVoice 或者 gamePromptVoices
         * @param offsetByte 文件内部偏移 或者 语音指针
         * @param flag  0x80: game Prompt； 0x00: game voices
         * @param numExpected
         * @return
         * @throws IOException
         */
        String[] getVoices(byte offsetByte, byte flag, int numExpected) throws IOException {
            final String[] voices = new String[numExpected];
            final int offset = offsetByte & 0xFF;
            if (flag == (byte) 0x80) {
                final int[] indice = getPromptVoicesIdx(offsetByte, numExpected);
                for (int idx = 0; idx < numExpected; ++idx) {
                    voices[idx] = gamePromptVoices[indice[idx]].getVoice();
                }
            } else if (flag == 0) {
                if (offset >= numVoices) {
                    throw new WrongDataFormatException("gameVoices 指针越界：" + offset + " VS " + numVoices);
                } else if (numExpected != 1) {
                    throw new WrongDataFormatException("gameVocies 一次一个：" + numExpected);
                }
                voices[0] = gameVoices[offset].getVoice();
            }
            return voices;
        }

        /**
         * 检测是不是缺省空语音文件，不是throw
         * 空语音文件索引可能是 0，也可能是 1
         */
        void checkBlankVoice(String voice) throws IOException {
            if (!gamePromptVoices[0].getVoice().equals(voice)
            		&& (gamePromptVoices.length <= 1 || !gamePromptVoices[1].getVoice().equals(voice))) {
                throw new WrongDataFormatException("Not blank voice: " + voice);
            }
        }

        /**
         * 检测是不是缺省空语音文件或者＃0 gameVoices，不是throw
         */
        void checkVoice00(String voice) throws IOException {
            if (!gamePromptVoices[0].getVoice().equals(voice)
            		&& (gamePromptVoices.length <= 1 || !gamePromptVoices[1].getVoice().equals(voice))
            		&& !gameVoices[0].getVoice().equals(voice)) {
                throw new WrongDataFormatException("Not 00 voice: " + voice);
            }
        }

        /**
         * helper 帮助构建 GameInfo.AnswerCode，主要设置热区
         */
        GameInfo.AnswerCode getAnswerCode(int code) throws IOException {
            final GameInfo.AnswerCode answerCode = new GameInfo.AnswerCode();
            answerCode.relatedCodes = new HotArea.RelatedCodes();
            if (relatedCodesMap != null && relatedCodesMap.size()>0) {
                final List<Integer> codes = relatedCodesMap.get(code);
                if(codes == null || codes.size() <= 0){
                    throw new WrongDataFormatException("码值没有对应的二代码或三代码：" + code);
                }
                final int codeNum = codes.size();
                answerCode.relatedCodes.shCodes = new ShCode[codeNum];
                for (int i = 0; i < codeNum; i++) {
                    answerCode.relatedCodes.shCodes[i] = new ShCode(codes.get(i), ShCode.OID3_BASE);
                }
            }else {
                answerCode.relatedCodes.shCodes = new ShCode[1];
                answerCode.relatedCodes.shCodes[0] = new ShCode(code, ShCode.OID3_BASE);
            }
            answerCode.hotarea = hotAreas.get(code);
            if (answerCode.hotarea == null && !hotAreas.isEmpty()) {
                throw new WrongDataFormatException("码值没有对应热区：" + code);
            }
            return answerCode;
        }


        /********************************************************************
         * 以下 Data 内部使用
         */
        /**
         * 获取prompt语音指针
         * @param offset  文件内偏移
         * @param numExpected 相关语音数量
         * @return voiceIdxBuffer里面填充了语音指针；或者 throw
         */
        private int[] getPromptVoicesIdx(byte offset, int numExpected) throws IOException {
            for (int idx = 0; idx < numExpected; ++idx) {
                final Integer key = (offset & 0xFF) * 2 + idx * 4 + audiosStartOffset;
                final Short val = promptVoicesMap.get(key);
                if (val == null) {
                    throw new WrongDataFormatException("Game voice index parse error: " + numExpected + " @" + offset);
                }
                voiceIdxBuffer[idx] = val;
            }
            return voiceIdxBuffer;
        }
        private final int[] voiceIdxBuffer = new int[10]; // 10 is big enough

        /**
         * 获取语音信息.
         * Key: 在文件的偏移地址; Value: 对应 0x8fffffff 语音文件指针
         */
        private static Map<Integer, Short> getVoiceMap(ByteBuffer byteBuffer, int audiosStartIndex,
                                                       int gameStartOffset) throws IOException {
            byteBuffer.position(audiosStartIndex);
            Map<Integer, Short> voiceMap = new HashMap<Integer, Short>();
            final short PREFIX_NUM = -32767; // 0x8001;
            final short END_NUM = -1; // 0xFFFF
            while (byteBuffer.position() < gameStartOffset) {
                short temp = byteBuffer.getShort();
                if (temp == PREFIX_NUM) {
                    voiceMap.put(byteBuffer.position() - 2, byteBuffer.getShort());
                } else if (temp == END_NUM) {
                    continue;
                } else {
                    throw new WrongDataFormatException("voice map 数据格式不对");
                }
            }
            return voiceMap;
        }


        /**
         * helper function
         * 获取热区信息.
         */
        private static HashMap<Integer, HotArea.Area> getHotArea(ByteBuffer byteBuffer, int hotareaOffset)
                throws IOException {
            final HashMap<Integer, ArrayList<OneArea>> codeAreasMap = new HashMap<>();
            byteBuffer.position(hotareaOffset);
            while (true) {
                final int code = byteBuffer.getInt();
                if (code == -1) {
                    break;
                }
                final OneArea area = new OneArea(code, byteBuffer.getInt(),
                        new int[] {byteBuffer.getInt(), byteBuffer.getInt(),
                                byteBuffer.getInt(), byteBuffer.getInt() });
                ArrayList<OneArea> array = codeAreasMap.get(area.code);
                if (array != null) {
                    if (area.pageNum != array.get(0).pageNum) {
                        throw new WrongDataFormatException("Hotarea Data problem");
                    }
                    array.add(area);
                } else {
                    array = new ArrayList<OneArea>();
                    array.add(area);
                    codeAreasMap.put(area.code, array);
                }
            }
            HashMap<Integer, HotArea.Area> codeHotareaMap = new HashMap<>();
            for (Map.Entry<Integer, ArrayList<OneArea>> entry: codeAreasMap.entrySet()) {
                final ArrayList<OneArea> array = entry.getValue();
                final int numTuples = array.size();
                final int[][] areas = new int[numTuples][];
                for (int idx = 0; idx < numTuples; ++idx) {
                    areas[idx] = array.get(idx).area;
                }
                codeHotareaMap.put(entry.getKey(), new HotArea.Area(array.get(0).pageNum, areas));
            }
            return codeHotareaMap;
        }
        
        /**
         * helper function
         * 获取热区信息.
         */
        private static Map<Integer, List<Integer>>  getRelatedCodes(ByteBuffer byteBuffer){
            final Map<Integer, List<Integer>> relatedCodesMap = new HashMap<Integer, List<Integer>>();
            try {
                while (true){
                    final int code = byteBuffer.getInt();
                    if (code == -1){
                        break;
                    }
                    final List<Integer> codeList = new ArrayList<Integer>();
                    codeList.add(code);
                    while (true) {
                        final int codeTemp = byteBuffer.getInt();
                        if (codeTemp == -1 || codeTemp == -2 ){
                            break;
                        }
                        codeList.add(codeTemp);
                    }
                    // TODO 不知道哪一个码值会出现在游戏答案中，所以分别将每一个码值都单独作为key放到map中保存
                    for (Integer integer : codeList) {
                        relatedCodesMap.put(integer, codeList);
                    }
                }
            } catch (Exception e) {
                // 兼容1.5包
                e.printStackTrace();
            }
            return relatedCodesMap;
        }

        /**
         * internal data structure to hold one hot area
         */
        private static class OneArea {
            final Integer code;
            final int pageNum;
            final int[] area;  // 依次排放 [left, top, right, bottom]
            OneArea(int code, int pageNum, int[] area) {
                this.code = code;
                this.pageNum = pageNum;
                this.area = area;
            }
        }
    }

}
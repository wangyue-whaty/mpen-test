package com.chivox;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mp.shared.common.Exam.SubTopic;
import com.mp.shared.record.ChivoxResult;
import com.mp.shared.record.ChivoxResult.ChivoxPredEvalResult;
import com.mp.shared.record.ChivoxResult.ChivoxRecscoreResult;
import com.mp.shared.record.ChivoxResult.ChivoxSentEvalResult;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.OralTestDetail;

/**
 *
 * 驰声口语考试引擎类
 *
 */
// 包名，类名不能改动,驰声sdk集成固定
public final class AIEngine {
    private static final Logger LOGGER = LoggerFactory.getLogger(AIEngine.class);

    static {
        try {
            System.load(ResourceUtils.getURL(Constants.AIENGINE_SO_PATH).getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public interface aiengine_callback {
        public abstract int run(byte[] id, int type, byte[] data, int size);
    }

    private static final String appKey = "1522231290000044";
    private static final String secretKey = "967be39be9e74d86559e1189af9fd8b0";
    private static final String userId = "aidemo_sx";
    private static final String deviceId = "";
    private static final String serialNumber = "";
    private static final String timestamp = "";
    private static final String params = "";
    public static final int AIENGINE_MESSAGE_TYPE_JSON = 1;
    public static final int AIENGINE_MESSAGE_TYPE_BIN = 2;

    public static final int AIENGINE_OPT_GET_VERSION = 1;
    public static final int AIENGINE_OPT_GET_MODULES = 2;
    public static final int AIENGINE_OPT_GET_TRAFFIC = 3;

    /**
     * 作用：创建引擎实例
     * 
     * @param cfg
     *            引擎相关配置, JSON格式, 应该包括appKey, secretKey, provision等信息
     * @param context
     * @return 返回实例指针 成功 返回NULL 失败，此时应该检查各参数
     */
    public static native long aiengine_new(String cfg, Object context);

    /**
     * 销毁引擎实例
     * 
     * @param engine
     *            引擎实例的指针
     * @return 0成功 -1失败
     */
    public static native int aiengine_delete(long engine);

    /**
     * 
     * @param engine
     *            引擎实例的指针
     * @param param
     *            启动参数, JSON格式。包括三部分：app(应用相关信息)，audio(音频格式参数)，request(内核相关参数)。
     *            其中音频参数是要跟音频本身一致的，且需符合如下要求: *wav：单通道、16kHz 采样率、16bits采样精度
     *            *mp3：单通道、16kHz 采样率、16bits采样精度 *ogg：单通道、16kHz 采样率、16bits采样精度
     *            *flv：单通道、22.05kHz 采样率、16bits采样精度 *amr：单通道、8kHz 采样率、16bits采样精度
     * @param id
     *            requestId，调用前传入空字符数组，调用后SDK将生成的唯一请求ID记录在其中，与评测结果中的 tokenId对应
     * @param callback
     *            回调函数，评分结果和评分中的异常都会在触发到这个回调函数中
     * @param context
     *            回调参数，在aiengine_start时传入可在callback函数中原样带回
     * @return
     */
    public static native int aiengine_start(long engine, String param, byte[] id, aiengine_callback callback,
            Object context);

    /**
     * 执行指定动作, 例如向引擎传入音频数据（音频数据必须已经去除头信息）
     * 
     * @param engine
     *            引擎实例的指针
     * @param data
     *            与动作相对应的数据
     * @param size
     *            数据大小
     * @return 0成功 1失败
     */
    public static native int aiengine_feed(long engine, byte[] data, int size);

    /**
     * 结束引擎当前次请求, 结果会在aiengine_start时设置的callback回调函数中返回
     * 
     * @param engine
     *            引擎实例的指针
     * @return 0成功 -1失败
     */
    public static native int aiengine_stop(long engine);

    public static native int aiengine_get_device_id(byte[] device_id, Object context);

    /**
     * 
     * @param engine
     *            取消引擎当前正在处理的请求, 相当于reset操作
     * @return 0成功 -1失败
     */
    public static native int aiengine_cancel(long engine);

    public static native int aiengine_log(long engine, String log);

    public static native int aiengine_opt(long engine, int opt, byte[] data, int size);

    public static int AIENGINE_MAXBUF = 1024 * 10;

    public OralTestDetail chivoxOralTestDetail;
    public static String callbackResult;
    public long engine;
    private Boolean doWait;
    private SaveOralTestCallback saveOralTestCallback;
    /**
     * 异步回调接口, 评分结果和评分中的异常都会在触发到这个回调函数中 注意：回调里不可做任何UI操作, IO操作, 复杂计算,
     * 及其它可能任何造成阻塞或等待的操作, 如有需要应该把这些操作提交给其它线程完成
     */
    private aiengine_callback callback = new aiengine_callback() {
        /**
         * id:requestId，与调用aiengine_start后生成的请求唯一标识对应 type:引擎返回的message类型, 目前支持：
         * AIENGINE_MESSAGE_TYPE_JSON, AIENGINE_MESSAGE_TYPE_BIN (仅在使用语音合成内核时用到)
         * data:回调参数，调用aiengine_start时传入的usrdata参数原样带回 size:message的大小
         */
        public int run(byte[] id, int type, byte[] data, int size) {
            LOGGER.error("in aiengine_callback...");
            if (type == AIENGINE_MESSAGE_TYPE_JSON) {
                callbackResult = new String(data, 0, size, Charset.forName("UTF-8")).trim();
                //转化驰声评测结果json数据为口语考试卷bean
                saveOralTest(callbackResult, subTopic);
                // 保存评测数据到数据库
                saveOralTestCallback.save(chivoxOralTestDetail);
            } else if (type == AIENGINE_MESSAGE_TYPE_BIN) {
                if (size == 0) {
                    LOGGER.error("0 means end of this binary stream");
                }
            }
            if (doWait) {
                synchronized (this) {
                    if (doWait) {
                        this.notifyAll();
                        doWait = false;
                    }
                }
            }
            return 0;
        }
    };
    private SubTopic subTopic;
    public interface SaveOralTestCallback {
        public void save(OralTestDetail chivoxOralTestDetail);
    }

    public AIEngine(SubTopic subTopic, OralTestDetail detail, SaveOralTestCallback saveOralTestCallback) {
        this.subTopic=subTopic;
        this.chivoxOralTestDetail=detail;
        this.saveOralTestCallback=saveOralTestCallback;
    }

    public void run(String audioFilePath) {
        doWait = true;
        int rv;
        int bytes;
        final byte[] buf = new byte[1024];
        final byte[] id = new byte[64];
        final java.lang.String json = getParams(subTopic);

        FileInputStream fis = null;
        final String param = this.getAiengine();
        // 创建引擎实例
        engine = aiengine_new(param, null);
        // 如果引擎为0，则创建失败，返回实例指针，则创建成功
        if (engine == 0) {
            LOGGER.error("create new engine failed");
            return;
        }
        final File file = new File(audioFilePath);
        if (!file.exists()) {
            LOGGER.error("文件不存在：" + audioFilePath);
        }
        // 启动本次请求
        rv = aiengine_start(engine, json, id, callback, this);
        // 0成功 -1代表失败
        if (rv == 0) {
            try {
                fis = new FileInputStream(audioFilePath);
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            try {
                while ((bytes = fis.read(buf, 0, 1024)) > 0) {
                    // 执行指定动作，例如向引擎引入音频数据(音频数据必须已经除头信息)
                    if ((rv = aiengine_feed(engine, buf, bytes)) != 0)
                        break;
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
        }else {
            doWait = false;
        }
        // 结束引擎当前次请求，结果会在aiengine_start时设置的callback回调函数返回
        rv = aiengine_stop(engine);
        if (rv != 0) {
            doWait = false;
        }

        try {
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        if (doWait) {
            synchronized (this) {
                if (doWait) {
                    try {
                        this.wait(8000); // 最多等 8000 毫秒
                    } catch (InterruptedException e) {
                        // e.printStackTrace(); // do nothing
                    }
                }
            }
        }
        // 销毁引擎实例
        aiengine_delete(engine);
        engine = 0;
    }

    /**
     * 引擎相关参数封装
     * 
     */
    private String getAiengine() {
        final Map<String, Object> aiengine = new HashMap<>();
        aiengine.put("appKey", appKey);
        aiengine.put("secretKey", secretKey);
        try {
            aiengine.put("provision", ResourceUtils.getURL(Constants.AIENGINE_PRO_SO).getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final Map<String, Object> cloud = new HashMap<>();
        cloud.put("enable", 1);
        cloud.put("server", "ws://cloud.chivox.com:8080");
        aiengine.put("cloud", cloud);
        final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        final String aiengineJson = gson.toJson(aiengine);
        return aiengineJson;
    }

    /**
     * 评测参数的封装
     * 
     * @param subTopic
     *            口语考试卷小题
     * @return 返回封装的json数据
     */
    private String getParams(SubTopic subTopic) {
        // 评测语音路径
        // 引擎评测参数
        final Map<String, Object> aiengineStartParam = new HashMap<>();
        // 必须，在线评测需配置为“cloud”
        aiengineStartParam.put("coreProvideType", "cloud");
        // part1: 应用相关信息
        final Map<String, String> appMap = new HashMap<>();
        // 可选,应用中的用户标识
        appMap.put("userId", "aidemo_sx");
        aiengineStartParam.put("app", appMap);
        // part2: 音频格式参数
        final Map<String, Object> audioMap = new HashMap<>();
        // 必须, 音频编码格式
        audioMap.put("audioType", "mp3");
        // 必须, 目前只支持单声道,只能填1
        audioMap.put("channel", 1);
        // 必须, 每采样字节数, 支持：1 (单字节，8位) 和 2 (双字节，16位)
        audioMap.put("sampleBytes", 2);
        // 必须, 采样率, 要与实际音频一致
        audioMap.put("sampleRate", 16000);
        aiengineStartParam.put("audio", audioMap);
        // part3: 语音服务参数
        final Map<String, Object> requestMap = new HashMap<>();
        // 内核参数类型
        switch (subTopic.coreType) {
        case 0:
            // subTopic.type==0表示en.word.score
            requestMap.put("coreType", "en.sent.score");
            // 存放传输过来的文本
            requestMap.put("refText", subTopic.refText);
            requestMap.put("rank", 100);
            break;
        case 1:
            // subTopic.type==1表示en.pred.exam
            requestMap.put("coreType", "en.pred.exam");
            final Map<String, Object> refText = new HashMap<>();
            // 存放传输过来的文本
            refText.put("lm", subTopic.refText);
            requestMap.put("refText", refText);
            requestMap.put("rank", 100);
            final Map<String, Object> client_params = new HashMap<>();
            client_params.put("ext_subitem_rank4", 0);
            client_params.put("ext_word_details", 1);
            requestMap.put("client_params", client_params);
            requestMap.put("precision", 0.5);
            break;
        case 2:
            // subTopic.type==2表示en.sent.recscore
            requestMap.put("coreType", "en.sent.recscore");
            // 存放传输过来的文本
            requestMap.put("refText", subTopic.refText);
            // 存放传输过来的关键词
            requestMap.put("keyWords", subTopic.keyWords);
            final Map<String, Object> result = new HashMap<>();
            // 设置为 1 表示在识别准确的情况下会显示每个单词得分
            result.put("use_details", 1);
            requestMap.put("result", result);
            break;
        default:
            LOGGER.error("未知评测类型");
            break;
        }
        aiengineStartParam.put("request", requestMap);
        final Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        final String aiengineStartParamJson = gson.toJson(aiengineStartParam);
        return aiengineStartParamJson;
    }

    /**
     * 转化驰声评测结果json数据为口语考试卷bean
     * 
     * @param result
     *            评测的结果
     * @param subTopic
     *            评测的小题
     */
    private void saveOralTest(String result, SubTopic subTopic) {
        if (result != null) {
            switch (subTopic.coreType) {
            case 0:
                final ChivoxSentEvalResult chivoxEvalResult = Constants.GSON.fromJson(result,
                        ChivoxSentEvalResult.class);
                if (chivoxEvalResult != null) {
                    // 获取单词和文本 驰声返回的单词变量为char,无法用bean封装,用Json API来进行获取
                    final java.lang.String words = ChivoxResult.getWords(result);
                    this.SaveChivoxOralTestDetail(chivoxEvalResult.result.overall,
                            chivoxEvalResult.result.fluency.overall, 0, chivoxEvalResult.result.integrity,
                            chivoxEvalResult.result.accuracy, words);
                }

                break;
            case 1:
                final ChivoxPredEvalResult chivoxPredEvalResult = Constants.GSON.fromJson(result,
                        ChivoxPredEvalResult.class);
                if (chivoxPredEvalResult != null) {
                    ChivoxPredEvalResult.Detail[] details = chivoxPredEvalResult.result.details;
                    final ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
                    for (ChivoxPredEvalResult.Detail detail : details) {
                        final ChivoxPredEvalResult.Word[] words = detail.words;
                        for (ChivoxPredEvalResult.Word word : words) {
                            final Map<String, Object> wordDatas = new HashMap<String, Object>();
                            wordDatas.put("text", word.text);
                            wordDatas.put("score", word.score);
                            arrayList.add(wordDatas);
                        }
                    }
                    final String json = new Gson().toJson(arrayList);
                    this.SaveChivoxOralTestDetail(chivoxPredEvalResult.result.overall,
                            chivoxPredEvalResult.result.fluency, 0, chivoxPredEvalResult.result.integrity,
                            chivoxPredEvalResult.result.pron, json);
                }
                break;
            case 2:
                ChivoxRecscoreResult chivoxRecscoreResult = Constants.GSON.fromJson(result, ChivoxRecscoreResult.class);
                if (chivoxRecscoreResult != null) {
                    final java.lang.String words = ChivoxResult.getWords(result);
                    this.SaveChivoxOralTestDetail(chivoxRecscoreResult.result.conf,
                            chivoxRecscoreResult.result.fluency.overall, chivoxRecscoreResult.result.accuracy,
                            chivoxRecscoreResult.result.integrity, chivoxRecscoreResult.result.accuracy, words);
                }
                break;

            default:
                LOGGER.error("未知评测类型");
                break;
            }
        }

    }
    /**
     * 保存数据到chivoxOralTestDetail对象中
     */
    private void SaveChivoxOralTestDetail(double score, double fluency, double accuracy, double integrity,
            double pronunciation, String recognizeTxt) {
        // 总分
        chivoxOralTestDetail.setScore(score / 100);
        chivoxOralTestDetail.setIsDeal(1);
        // 流利度
        chivoxOralTestDetail.setFluency(fluency);
        // 正确性
        chivoxOralTestDetail.setPronunciation(pronunciation);
        // 完整性
        chivoxOralTestDetail.setIntegrity(integrity);
        // 单词分数、单词文本
        chivoxOralTestDetail.setRecognizeTxt(recognizeTxt);
       
    }
}

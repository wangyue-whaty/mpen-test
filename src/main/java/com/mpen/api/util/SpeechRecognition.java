package com.mpen.api.util;

import org.json.JSONObject;

import com.baidu.aip.speech.AipSpeech;
import com.mpen.api.common.Constants;

/**
 * 语音识别
 * @author wangyue
 *
 */
public class SpeechRecognition {
    // 设置APPID/AK/SK
    public static final String APP_ID = "10677982";
    public static final String API_KEY = "AVZeHfzEGUMNiZYVqjl1cPry";
    public static final String SECRET_KEY = "wvihKmg3IGW2A9ezB0uo4st8glxbzx6H";

    public static String recognize(String filePath) {
        // 初始化一个AipSpeech
        AipSpeech client = new AipSpeech(APP_ID, API_KEY, SECRET_KEY);

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 调用接口
        final JSONObject res = client.asr(filePath, "wav", 16000, null);
        final Result result = Constants.GSON.fromJson(res.toString(2), Result.class);
        if (result.err_no == 0) {
            return result.result[0];
        }
        return null;
    }

    public static final class Result {
        public String[] result;
        public String err_msg;
        public String sn;
        public String corpus_no;
        public int err_no;
    }
}

package com.mp.shared.record;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

/**
 * 
 * 驰声口语考试评测转换结果类
 *
 */
public class ChivoxResult {
    /*
     * 对内核为en.pred.exam的结果封装
     * 参数URL:http://doc.api.chivox.com/api/index.php?s=/26&page_id=272
     */
    public static class ChivoxPredEvalResult {
        public Result result;

        public static final class Result {
            // 流利度
            public double fluency;
            // 完整度
            public double integrity;
            // 总分
            public double overall;
            // 发音得分
            public double pron;
            public Detail[] details;
        }

        public static final class Detail {
            // 单词
            public Word[] words;
        }

        public static final class Word {
            // 单词文本
            public String text;
            // 单词分数
            public String score;
        }

    }

    /*
     * Aitalk内核内核评测结果封装bean
     * 参数URL:http://doc.api.chivox.com/api/index.php?s=/26&page_id=274
     *
     */
    public static class ChivoxRecscoreResult {

        public Result result;

        public static final class Result {
            // 流利度
            public Fluency fluency;
            // 完整度
            public double integrity;
            // 发音准确度
            public double accuracy;
            // 总分
            public double conf;
        }

        public static final class Fluency {
            // 分数
            public double overall;
        }
    }

    /*
     * 对内核为en.sent.exam的结果封装
     * 参数URL：http://doc.api.chivox.com/api/index.php?s=/26&page_id=271
     */
    public static class ChivoxSentEvalResult {
        public Result result;

        public static final class Result {
            // 完整度
            public double integrity;
            // 总分
            public double overall;
            // 流利度
            public Fluency fluency;
            // 发音准确度
            public double accuracy;

        }

        public static final class Fluency {
            // 分数
            public double overall;
        }
    }

    /**
     * 驰声返回的单词的变量名为char，单独对此封装
     */
    public static String getWords(String result) {
        final ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        final org.json.JSONObject jsonObject = new org.json.JSONObject(result);
        final org.json.JSONObject resultjson = jsonObject.getJSONObject("result");
        final org.json.JSONArray jsonArray = resultjson.getJSONArray("details");
        for (int j = 0; j < jsonArray.length(); j++) {
            final org.json.JSONObject jsonObject2 = jsonArray.getJSONObject(j);
            final Map<String, Object> wordDatas = new HashMap<String, Object>();
            final String wordText = jsonObject2.getString("char");
            final double score = jsonObject2.getDouble("score");
            wordDatas.put("text", wordText);
            wordDatas.put("score", score);
            arrayList.add(wordDatas);
        }
        return new Gson().toJson(arrayList);
    }
    
}

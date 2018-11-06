package com.mpen.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mp.shared.common.Exam.SubTopic;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.OralTestDetail;
import com.mpen.api.exception.SdkException;

public final class OralEvaluationUtil {
    private static final String OPUS_YUNZHISHENG_URL = "http://edu.hivoice.cn/eval/opus";
    private static final String MP3_YUNZHISHENG_URL = "http://edu.hivoice.cn/eval/mp3";
    // session id，表示该次评测的唯一表示，必须上传，使用 uuid
    private static final String HEADER_SESSION_ID = "session-id";
    private static final String HEADER_APP_KEY = "appkey";
    private static final String APP_KEY = "zlzwmq5457i62dsfnor6wiutluzku5sunqyrdvas";
    // score-coefficient分数调整定制参数，可以对同样质量的语音调整得分高低，范围是0.6~1.9，默认情况下是1.0，设置越低，打分越严格，设置系数越高，打分越松，一般小学生设置系数偏高，采取鼓励模式
    private static final String HEADER_SCORE_COEFFICIENT = "score-coefficient";
    private static final float DEFAULT_SCORE_COEFFICIENT = 1.0f;
    // 设备或用户的id标识。一个客户应该保证每个用户拥有唯一的id，建议上传，方便用户的数据上传
    private static final String HEADER_DEVICE_ID = "device-id";
    // 需要评测的文本
    private static final String BODY_TEXT = "text";
    /*
     * 评测模式（包含A B C D E G H，A B G H 是常用模式） A：最简单模式，结果只有单词打分没有音素信息
     * B：有音素信息，但是没有音素打分 C：跟A一样，区别是最外层有Score总分 D：在B的基础上，有音素打分
     * E：返回words字段里的值，有空格和标点符号，但是没有音素打分(针对个别客户需求) G：跟D一样 H：选择题打分模式
     */
    private static final String BODY_MODE = "mode";
    private static final String DEFAULT_MODE = "E";
    private static final String A_MODE = "A";
    private static final String BODY_VOICE = "voice";
    public static EvalResult evaluation(OralTestDetail detail,SubTopic subTopic ) throws SdkException {
        final Map<String, String> headerMap = new HashMap<>();
        headerMap.put(HEADER_SESSION_ID, UUID.randomUUID().toString());
        headerMap.put(HEADER_APP_KEY, APP_KEY);
        headerMap.put(HEADER_SCORE_COEFFICIENT, String.valueOf(DEFAULT_SCORE_COEFFICIENT));
        headerMap.put(HEADER_DEVICE_ID, detail.getLoginId());
        final Map<String, String> bodyMap = new HashMap<>();
        //两种评测模式 口语考试主观题 A模式  客观题 E模式
        bodyMap.put(BODY_MODE, subTopic.getStaAnswer()!=null?A_MODE:DEFAULT_MODE);
        bodyMap.put(BODY_TEXT, subTopic.getAnswer());
        final String url = detail.getRecordingUrl().endsWith("mp3") ? MP3_YUNZHISHENG_URL : OPUS_YUNZHISHENG_URL;
        try {
            final String result = CommUtil.post(url, headerMap, bodyMap, BODY_VOICE,
                FileUtils.getFileSaveRealPath(detail.getRecordingUrl(), false));
            if (StringUtils.isBlank(result)) {
                return null;
            }
            return Constants.GSON.fromJson(result, EvalResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 云之声返回的评测结果
     * https://github.com/oraleval/FAQ-Docs/blob/master/Json%20Description.md
     *
     */
    public static final class EvalResult {
        public String EvalType;
        public double score;
        public String version;
        public Line[] lines;
    }

    public static final class Line {
        public double begin;
        public double end;
        // 流利度
        public double fluency;
        // 完整度
        public double integrity;
        // 标准度
        public double pronunciation;
        public String sample;
        public double score;
        public String usertext;
        public Word[] words;
    }

    public static final class Word {
        public double score;
        public String text;
        public int type;
    }

}

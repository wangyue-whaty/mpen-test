package com.mpen.api.bean;

import java.io.Serializable;
import java.util.ArrayList;

public final class ExamResult {
    // 试卷名称
    public String examName;
    // 用户名称
    public String userName;
    // 用户得分
    public double userTotalPoints;
    // 用户排名
    public double userRank;
    // 答题日期（时间戳）
    public long userDate;
    // 班级平均分
    public double avgTotalPoints;

    public ArrayList<TopicResult> topic;

    public static final class TopicResult implements Serializable {
        private static final long serialVersionUID = 6725410462561433010L;
        // 大题标题
        public String title;
        public ArrayList<SubTopicResult> subTopic;
    }

    public static final class SubTopicResult implements Serializable {
        private static final long serialVersionUID = 2850990710903722563L;
        // 用户得分
        public double userPoints;
        // 流利度
        public double fluency;
        // 完整度
        public double integrity;
        // 标准度
        public double pronunciation;
        // 用户录音
        public String userVoice;
        // 用户录音识别结果
        public String userRecognizeTxt;
        // 问题
        public String question;
        // 参考答案
        public ArrayList<String> refAnswer;
        // 题目类型 0：朗读题；1：对话题
        public int type;
        // 口语考试卷小题图片URL
        public String imageUrl;
        // 口语考试卷重音关键字
        public ArrayList<String> accentKey;
        // 班级每个小题最大分
        public double maxScore;
        // 班级每个小题最小分
        public double minScore;
        // 班级每个小题平均分
        public double avgScore;
        // 班级每个小题平均流利度
        public double avgFluency;
        // 班级每个小题平均标准度
        public double avgIntegrity;
        // 班级每个小题平均标准度
        public double avgPronunciation;
    }
}

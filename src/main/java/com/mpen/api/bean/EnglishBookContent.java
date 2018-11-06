package com.mpen.api.bean;

import java.util.List;

/**
 * 教师端以及APP2.0所涉及:
 * 英语书籍内容以及学生作业分数情况 bean
 */

public class EnglishBookContent {
    // 模块
    public String model;
    // 单元
    public String unit;
    // 单元名称
    public String name;
    // 单元包含的activities
    public List<Activity> activities;

    public final static class Activity {
        public String id;
        // activity标识
        public String sort;
        // activity名称
        public String name;
        // 平均点读次数
        public int avgCountTimes;
        // 最小点读次数
        public int minCountTimes;
        // 最大点读次数
        public int maxCountTimes;
        public int countTimes;
        // 最大分数
        public double maxScore;
        // 最小分数
        public double minScore;
        // 平均分数
        public double avgScore;
        // 句子内容
        public List<Sentence> sentences;
    }

    public final static class Sentence {
        // 句子内容
        public String title;
        // 平均分数
        public double avgScore;
        // 最小分数
        public double minScore;
        // 最大分数
        public double maxScore;
        public double score;
        // 优秀
        public double excellentProportion;
        // 良好
        public double favorableProportion;
        // 一般
        public double generalProportion;
        // 不及格
        public double flunkProportion;
    }
}

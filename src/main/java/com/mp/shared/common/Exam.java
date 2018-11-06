package com.mp.shared.common;

import java.io.Serializable;
import java.util.ArrayList;

import com.mp.shared.utils.Json;

public final class Exam implements Serializable {
    /*
     * 云知声与驰声返回数据区别：返回参数变量名可能不一样，但代表的意思有的是一样的。 口语考试卷评测主要获取信息：总分、完整度分数、标准度分数、流利度分数、单词文本和分数。
     * 用云知声和驰声评测都可以获取到这些分数。如果需要其他参数，可以参考：
     * 云知声URL：https://github.com/oraleval/FAQ-Docs/blob/master/Json%
     * 20Description.md
     * 驰声URL：http://doc.api.chivox.com/api/index.php?s=/17&page_id=286
     */
    private static final long serialVersionUID = 1184171126499764147L;
    public int num;
    // 试卷总分
    public int totalPoints;
    // 试卷名称
    public String name;
    // 试题
    public ArrayList<Topic> topic;

    public static final class Topic implements Serializable {
        private static final long serialVersionUID = 6725410462561433010L;
        // 大题标题
        public String title;
        public ArrayList<SubTopic> subTopic;
    }

    public static final class SubTopic implements Serializable {
        private static final long serialVersionUID = 2850990710903722563L;
        public int examNum;
        public int num;
        // 分数
        public double points;
        // 问题
        public String question;
        // 答题限制时间
        public int limitTimeSec;
        // 参考答案
        public ArrayList<String> refAnswer;
        // 标准答案
        public StaAnswer staAnswer;
        // 题目类型 0：朗读题；1：对话题
        public int type;
        /*
         * 驰声内核类型 0：句子内核 1 朗读内核   2 Aitalk内核 
         * 0:句子内核主要针对单词个数在2~15个单词之间。如"I want to know the past and present of Hong Kong.",属于封闭题型
         * 1:朗读内核主要针对句子来进评测，属于封闭题型
         * 2：Aitalk内核主要针对开放题型，与句子内核和朗读内核不同的是需要发送关键字keyWords。
         */
        public int coreType;
        /*
         * 驰声口语考试卷评测文本，格式与云知声不一样。如： 驰声文本："refText":"I usually ride my bike. | I usually ride a bike. | I usually ride the bike."
         *  云知声文本："staAnswer": { "Version":"1", "DisplayText":"Jsgf Grammar Tool Generated", "GrammarWeight":
         * "{\"weight_struct\":[[{\"weight\":0.5,\"key\":\"my bike\"}],[{\"weight\":0.5,\"key\":\"ride a bike\"}],[{\"weight\":0.5,\"key\":\"ride the bike\"}]]}"
         * , "Grammar":
         * "#JSGF V1.0 utf-8 cn;\ngrammar main;\npublic <main> = \"<s>\"(i usually <keyword1>| i usually <keyword2> |i usually <keyword2>)\"</s>\";\n<keyword1> = (my bike);\n<keyword2> = (ride a bike);\n<keyword3> = (ride the bike);\n"
         * },
         */
        public String refText;
        /*
         * 驰声Aitalk内核评测关键词
         * 关键词设置规则：“#” 是第一层分隔符， 以 “#” 分隔开的各部分表示各种表达法， 之间是 OR 的关系；“|” 是第二层分隔符， 以 “|” 将表达法内的各个关键字或词组分隔开， 之间是 AND 关系
         * 如： keyWords: "go to |school | on foot # walk to | school",
         */
        public String keyWords;
        //口语考试卷小题图片url
        public String imageUrl;
        //口语考试卷重音关键字
        public ArrayList<String> accentKey;
        /*
         * staAnswer主观题型文本，refAnswer客观题型文本，云知声两种评测的方式不同，发送的文本不同。
         * 云知声评测模式：主观题 A模式  客观题 E模式
         * 文本区别如："refText":"sunny hurry"
         * "staAnswer":{"Version":"1","DisplayText":"Jsgf Grammar Tool Generated","GrammarWeight":"{\"weight_struct\":[]}","Grammar":"#JSGF V1.0 utf-8 cn;\ngrammar main;\npublic <main> = \"<s>\"(6 it's <keywords2> <keywords1>)\"</s>\";\n<keywords1> = (the park);\n<keywords2> = (next to);\n"},
         * getStaAnswer()方法获取主观题型文本
         * getAnswer()方法获取客观题型文本或者主观题型文本
         */
        public String getStaAnswer() {
            return staAnswer == null ? null : Json.GSON.toJson(staAnswer);
        }
        public String getAnswer() {
            return staAnswer == null ? refAnswer.get(0) : Json.GSON.toJson(staAnswer);
        }

    }
    
    /**
     * 云之声对话题要求数据格式， 由工具http://101.231.106.182:5000/#/生成
     *
     */
    public static final class StaAnswer implements Serializable {
        private static final long serialVersionUID = 555236596899040330L;
        public String Version;
        public String DisplayText;
        public String GrammarWeight;
        public String Grammar;
    }

}

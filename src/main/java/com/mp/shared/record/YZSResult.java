package com.mp.shared.record;

import java.util.ArrayList;

/*
 *  云知声原始数据封装类
 *  样例：{"EvalType" : "general","lines" : [{"begin" : 0.0,"end" : 3.761,"fluency"
 *  : 74.552,"integrity" : 100.0,"pronunciation" : 71.748,"sample" : "Do you 
 *  want to go to Chinatown?","score" : 71.888,"usertext" : "Do you want to go 
 *  to Chinatown","words" : [{"begin" : 0.081,"end" : 0.211,"score" : 5.265,"text" 
 *  : "sil","type" : 4,"volume" : 1.535},{"StressOfWord" : 1,"begin" : 0.211,"end" 
 *  : 0.321,"phonetic" : "'duː","score" : 4.114,"text" : "Do","type" : 2,"volume" :
 *  7.861},{"StressOfWord" : 0,"begin" : 0.321,"end" : 0.441,"phonetic" : "jə","sco
 *  re" : 9.206,"text" : "you","type" : 2,"volume" : 9.0},{"StressOfWord" : 1,"begi
 *  n" : 0.441,"end" : 0.801,"phonetic" : "'wɒnt","score" : 8.637,"text" : "want","
 *  type" : 2,"volume" : 6.725},{"begin" : 0.801,"end" : 0.831,"score" : 3.301,"text"
 *  : "sil","type" : 4,"volume" : 2.464},{"StressOfWord" : 1,"begin" : 0.831,"end" : 
 *  0.961,"phonetic" : "'tuː","score" : 6.692,"text" : "to","type" : 2,"volume" : 7.983
 *  },{"StressOfWord" : 1,"begin" : 0.961,"end" : 1.241,"phonetic" : "'gəʊ","score" : 
 *  9.139,"text" : "go","type" : 2,"volume" : 7.541},{"StressOfWord" : 1,"begin" : 1.24
 *  1,"end" : 1.541,"phonetic" : "'tuː","score" : 7.554,"text" : "to","type" : 2,"volume" 
 *  : 7.137},{"StressOfWord" : 1,"begin" : 1.541,"end" : 2.441,"phonetic" : "'tʃaɪnəˌtaʊn",
 *  "score" : 3.694,"text" : "Chinatown","type" : 2,"volume" : 7.477},{"begin" : 2.441,"end"
 *  : 3.761,"score" : 4.479,"text" : "sil","type" : 4,"volume" : 1.698}]}],"score" : 71.888,
 *  "version" : "full 1.0"}  
 */
public class YZSResult {
    // 题的类型
    String evalType;
    // 总分
    float score;
    // 版本
    String version;
    // 行数
    ArrayList<Line> lines;

    public static final class Line {
        // 音频开始时间
        float begin;
        // 音频结束时间
        float end;
        // 流利度
        float fluency;
        // 完整度
        float integrity;
        // 标准度
        float pronunciation;
        // 分数
        float score;
        // 评测文本
        String sample;
        // 识别的用户文本
        String usertext;
        // 单词的详细信息
        ArrayList<Word> words;
    }

    public static final class Word {
        // 开始时间
        float begin;
        // 结束时间
        float end;
        // 分数
        float score;
        // 音量大小
        float volume;
        // 单词重音标志
        int streeOfWord;
        // 类型
        int type;
        // 文本
        String text;
        // 单词音标
        String phonetic;
    }

}

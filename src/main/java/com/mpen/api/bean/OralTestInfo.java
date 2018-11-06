package com.mpen.api.bean;

import java.util.ArrayList;

/**
 * 用户口语考试卷信息
 *
 */

public class OralTestInfo {
    public String id;
    // 口语考试书籍名字
    public String name;
    // 平均得分
    public double averageScore;
    // 口语考试卷
    public ArrayList<OralTestPaper> papers;

    public static final class OralTestPaper {
        // 卷子名字
        public String name;
        //卷子编号
        public int num;
        // 总分
        public double score;

    }
}

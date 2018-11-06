package com.mp.tools;

import java.util.Arrays;

import com.mp.shared.common.GameInfo;

/**
 * 游戏分析结果.
 * 
 * @author zyt
 *
 */
public final class GameAnalysisResult {
    public GameResult[] gameResult;
    public ErrorFile[] errorFile;

    @Override
    public String toString() {
        return "{gameResult=" + Arrays.toString(gameResult) + "}";
    }

    public static class GameResult {
        public int gameNum;
        public Type gameType;
        public QuestionAnswerResult[] result;

        @Override
        public String toString() {
            return "{gameNum=" + gameNum + ", gameType=" + gameType + ", result=" + Arrays.toString(result) + "}";
        }

    }

    public static class QuestionAnswerResult {
        // 问题个数（1个，2个，3个......）
        public int questionNum;
        // 游戏个数
        public int gameNum;
        // 游戏GameInfo对象
        public GameInfo[] games;

        @Override
        public String toString() {
            return "{questionNum=" + questionNum + ", gameNum=" + gameNum + "}";
        }

    }

    /**
     * 游戏类型（QUESTION：问答题；LINK：连线题）
     * 
     * @author zyt
     *
     */
    public enum Type {
        QUESTION, LINK
    };

    public static class ErrorFile {
        // 错误文件名
        public String fileName;
        // 错误信息
        public String errorMsg;
    }

}

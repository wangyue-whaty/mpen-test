package com.mp.shared.common;

import java.io.Serializable;

/**
 * 定义从资源文件中解析出来的游戏格式，游戏逻辑中用到的数据大概可以分为这几类：
 * - -1.顺序问题对应顺序答案
 * - -2.顺序问题对应乱序答案
 * - -3.乱序问题对应顺序答案（可以概括连线题）
 *      连线题的一组的"问"和"答"可以看作是：一个问题有两个必须按顺序回答的答案
 *      其中第一个答案对应"问"，第二个答案对应"答"
 * 以上三种，通过isQuestionOrdered 来控制问题的随机性，isAnswerOrdered
 * 来控制答案的随机性 注意：顺序问题和乱序问题可通过gametype进行区分，1的为顺序问题，3的为乱序问题，1和3表示二进制文件中解析出来的结果
 */
public class GameInfo implements Serializable {
    private static final long serialVersionUID = 5949213229395626543L;

    /**
     * AnswerCode 定义 "一个" 答案。 ＊ 答案会有多种形式：
     * －－ 松翰码：包括 shCode 和 shSubType；可能会有多个松翰码对应一个答案（二代码，三代码，等等）
     * －－ 一些热区 （hotspot）：多个热区组成一个完整热区
     * 答对答案后，是不是要发点读码本身的发音
     */
    public static class AnswerCode implements Serializable {
        private static final long serialVersionUID = -9104308308613676754L;
        public HotArea.RelatedCodes relatedCodes; // 正确答案的（松翰）码值
        public HotArea.Area hotarea; // 热区坐标，用于MP码判断
        public boolean playCodeOwnVoice; // 答对答案后，是不是要发点读码本身的发音
    }

    /**
     * QuestionAnswer 定义一个问题和对应的答案，包括
     * ＊ 问题的发声：questionVoice
     * ＊ 全部回答正确后的发音： correctAnwseredVoice
     * ＊ 答案：answerCodes：可能是单选或者多选
     * ＊ 对于多选题，答案顺序是否重要：isAnswerOrdered
     *
     * 对于连线题的一组，相当于：有2个answerCodes，numGoodAnswersToPass ＝ 2， isAnswerOrdered ＝ true
     */
    public static class QuestionAnswer implements Serializable {
        private static final long serialVersionUID = 8889438515144429188L;
        public String questionVoice; // 问题的内容，实质上为播放问题的语音，如果是本地，那么赋值为本地URL，如果是云点读，那么赋值为网络URL
        public AnswerCode[] answerCodes; // 对应答案
        public int numGoodAnswersToPass; // 要答对多少个答案才通过本题
        public boolean isAnswerOrdered; // 答案是否是随机的，true则为顺序答案（对应顺序回答的问题），false为乱序答案（对应乱序回答的问题）

        public String correctAnwseredVoice; // 全部回答正确后的发音
        public String[] correctVoices; // 回答正确的语音，从中随机选出一个作为正确答案语音
        public String[] wrongVoices; // 回答错误的语音，从中随机选出一个作为错误答案语音
        public String[] choosenVoices; // 已经选过答案的语音
        public int allowWrongNum; // 允许错误次数


        /**
         * 大概检查一下数据结构没有问题（有内容的地方该有内容）
         */
        public final boolean isQuestionAnswerOk(boolean isFirstQuestion) {
            return (questionVoice != null || isFirstQuestion)
                    && !Utils.isEmpty(answerCodes)
                    && (correctAnwseredVoice != null || !Utils.isEmpty(correctVoices))
                    && !Utils.isEmpty(wrongVoices)
                    && !Utils.isEmpty(choosenVoices)
                    && allowWrongNum > 0
                    && numGoodAnswersToPass > 0 && numGoodAnswersToPass <= answerCodes.length;
        }

        /**
         * 检查是否是答案之一
         */
        public int inAnswers(ShCode shCode, MpCode.Point mpPoint) {
            for (int idx = 0; idx < answerCodes.length; ++idx) {
                final AnswerCode answerCode = answerCodes[idx];
                if (shCode != null) {
                    if (answerCode.relatedCodes.hasIn(shCode)) {
                        return idx;
                    }
                } else if (mpPoint != null) {
                    if (answerCode.hotarea.hasIn(mpPoint)) {
                        return idx;
                    }
                }
            }
            return -1;
        }
    }

    /******************************************************
     * 下面定义 GameInfo 具体的变量
     */
    public QuestionAnswer[] questionAnswers;
    public int numQuestionsToAnswer;  // 需要答对多少道题
    public boolean isQuestionOrdered;
    public boolean isUserDefinedOrder;  // 连线题，每个问题的顺序是用户点击决定的


    public String welcomeVoice; // 进入游戏的语音 (单问题模式questionVoice为null）
    public String successEndVoice; // 退出游戏的语音，正确退出，
    public String failureEndVoice; // 退出游戏的语音，不正确退出
    public String leaveGameVoice;  // 离开游戏提示 －－ 在 successEndVoice／failureEndVoice 之后

    /**
     * 大概检查一下数据结构没有问题（有内容的地方该有内容）
     */
    public final boolean isGameInfoOk() {
        final boolean isOk = !Utils.isEmpty(questionAnswers)
                && numQuestionsToAnswer > 0 && numQuestionsToAnswer <= questionAnswers.length
                && !Utils.isEmpty(welcomeVoice)
                && !Utils.isEmpty(successEndVoice) && !Utils.isEmpty(failureEndVoice)
                && !Utils.isEmpty(leaveGameVoice);
        if (!isOk) {
            return false;
        }
        final int numQuestions = questionAnswers.length;
        for (int idx = 0; idx < numQuestions; ++idx) {
            if (!questionAnswers[idx].isQuestionAnswerOk(idx == 0)) {
                return false;
            }
        }
        return true;
    }
}

package com.mp.shared.common;

/**
 * 角色扮演的数据模型
 * Created by ZSC on 2018/1/2.
 */

public class CosplayModule implements IsValid {
    /**
     * 对应的每个角色的句子
     */
    public final static class Sentence {
        public String coser;
        public String content;
        public int relatedCode;
    }

    /**
     * 对应的每个对话
     */
    public final static class Dialogue {
        public Sentence[] sentences;
        public int nameNum;
    }

    public Dialogue[] dialogues;
    public String name;

    @Override
    public boolean isValid() {
        return !(name == null || name.length() == 0 || dialogues == null);
    }
}

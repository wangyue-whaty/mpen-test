package com.mp.shared.common;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import com.mp.shared.common.Exam.StaAnswer;
import com.mp.shared.common.Exam.SubTopic;
import com.mp.shared.common.Exam.Topic;
import com.mpen.api.common.Constants;

public class ExamTest {
    private Exam exam;
    private String[] expectResult = new String[5];

    @Before
    public void prepare() {
        exam = new Exam();
        exam.num = 1;
        exam.name = "第一单元口语综合测试卷A";
        exam.totalPoints = 15;
        exam.topic = new ArrayList<>();
        Topic topic = new Topic();
        topic.title = "一、朗读（满分5分）";
        topic.subTopic = new ArrayList<>();
        SubTopic subTopic = new SubTopic();
        subTopic.examNum = 1;
        subTopic.num = 5;
        subTopic.points = 5;
        subTopic.limitTimeSec = 80;
        subTopic.question = "用正确的语音语调，在80秒钟内朗读下面短文一遍";
        subTopic.refAnswer = new ArrayList<>();
        subTopic.refAnswer.add(
            "The Monkey King is not just a normal monkey. In fact, he sometimes does not even look like a monkey! This is because he can make 72 changes to his shape and size, turning himself into different animals and objects. But unless he can hide his tail, he cannot turn himself into a person. To fight bad people, the Monkey King uses a magic stick. Sometimes he can make the stick so small that he can keep it in his ear. At other times, he is able to make it big and long.");
        expectResult[0] = subTopic.refAnswer.get(0);
        subTopic.type = 0;
        topic.subTopic.add(subTopic);
        exam.topic.add(topic);
        topic = new Topic();
        topic.title = "二 、情景问答（共3小题，每小题2分，满分6分）";
        topic.subTopic = new ArrayList<>();
        subTopic = new SubTopic();
        subTopic.examNum = 1;
        subTopic.num = 9;
        subTopic.points = 2;
        subTopic.limitTimeSec = 30;
        subTopic.question = "What's Jenny's pen pal's name?";
        subTopic.refAnswer = new ArrayList<>();
        subTopic.refAnswer.add("Jenny's pen pal's name is Mary.");
        subTopic.refAnswer.add("Her name is Mary.");
        subTopic.refAnswer.add("Mary.");
        subTopic.refAnswer.add("Jenny's new pen pal's name is Mary.");
        subTopic.type = 1;
        subTopic.staAnswer = new StaAnswer();
        subTopic.staAnswer.Version = "1";
        subTopic.staAnswer.DisplayText = "Jsgf Grammar Tool Generated";
        subTopic.staAnswer.GrammarWeight = "{\"weight_struct\":[[{\"weight\":0.5,\"key\":\"Mary\"}]]}";
        subTopic.staAnswer.Grammar = "#JSGF V1.0 utf-8 cn;\ngrammar main;\npublic <main> = \"<s>\"(<whose> name is mary|mary)\"</s>\";\n<whose> = (jenny's pen pal's |her name| jenny's new pen pal's);\n";
        expectResult[1] = Constants.GSON.toJson(subTopic.staAnswer);
        topic.subTopic.add(subTopic);
        subTopic = new SubTopic();
        subTopic.examNum = 1;
        subTopic.num = 13;
        subTopic.points = 2;
        subTopic.limitTimeSec = 30;
        subTopic.question = "Where is her pen pal from?";
        subTopic.refAnswer = new ArrayList<>();
        subTopic.refAnswer.add("She is from the United States.");
        subTopic.refAnswer.add("The United States.");
        subTopic.refAnswer.add("Her pen pal is from the United States.");
        subTopic.refAnswer.add("She comes from the United States.");
        subTopic.type = 1;
        subTopic.staAnswer = new StaAnswer();
        subTopic.staAnswer.Version = "1";
        subTopic.staAnswer.DisplayText = "Jsgf Grammar Tool Generated";
        subTopic.staAnswer.GrammarWeight = "{\"weight_struct\":[[{\"weight\":0.5,\"key\":\"the united states\"}]]}";
        subTopic.staAnswer.Grammar = "#JSGF V1.0 utf-8 cn;\ngrammar main;\npublic <main> = \"<s>\"(<who> is from <where>|<who> comes from <where>|the united states)\"</s>\";\n<who> = (she|her pen pal);\n<where> = (the united states);\n";
        expectResult[2] = Constants.GSON.toJson(subTopic.staAnswer);
        topic.subTopic.add(subTopic);
        subTopic = new SubTopic();
        subTopic.examNum = 1;
        subTopic.num = 17;
        subTopic.points = 2;
        subTopic.limitTimeSec = 30;
        subTopic.question = "Where does her pen pal live?";
        subTopic.refAnswer = new ArrayList<>();
        subTopic.refAnswer.add("She lives in New York.");
        subTopic.refAnswer.add("Her pen pal lives in New York.");
        subTopic.refAnswer.add("New York.");
        subTopic.refAnswer.add("Jenny's pen pal lives in New York.");
        subTopic.type = 1;
        subTopic.staAnswer = new StaAnswer();
        subTopic.staAnswer.Version = "1";
        subTopic.staAnswer.DisplayText = "Jsgf Grammar Tool Generated";
        subTopic.staAnswer.GrammarWeight = "{\"weight_struct\":[[{\"weight\":0.5,\"key\":\"new york\"}]]}";
        subTopic.staAnswer.Grammar = "#JSGF V1.0 utf-8 cn;\ngrammar main;\npublic <main> = \"<s>\"(<who> lives in <where>|new york)\"</s>\";\n<who> = (she|her pen pal|jenny's pen pal);\n<where> = (new york);\n";
        expectResult[3] = Constants.GSON.toJson(subTopic.staAnswer);
        topic.subTopic.add(subTopic);
        exam.topic.add(topic);
        topic = new Topic();
        topic.title = "三、简短说话 （满分4分）";
        topic.subTopic = new ArrayList<>();
        subTopic = new SubTopic();
        subTopic.examNum = 1;
        subTopic.num = 21;
        subTopic.points = 4;
        subTopic.limitTimeSec = 80;
        subTopic.question = "根据提示用六个以上的英语句子作简短说话（注意不要逐句翻译内容要点）\n有位外国人想去广州博物馆，你给她指路。 要点提示：\n1、向前走到西江路向左转，在拐弯处有一间医院;\n2、再往前走约20米，向右转到丽艳路;\n3、沿着丽艳路走，就可以在左边看到广州博物馆;\n4、广州博物馆里有各种各样的东西, 是个有趣的地方。\n5、会去博物馆、公园、商店、学校、餐馆等。她在这里定会过得很愉快。";
        subTopic.refAnswer = new ArrayList<>();
        subTopic.refAnswer.add("She lives in New York.");
        subTopic.refAnswer.add("Her pen pal lives in New York.");
        subTopic.refAnswer.add("New York.");
        subTopic.refAnswer.add("Jenny's pen pal lives in New York.");
        subTopic.type = 1;
        subTopic.staAnswer = new StaAnswer();
        subTopic.staAnswer.Version = "1";
        subTopic.staAnswer.DisplayText = "Jsgf Grammar Tool Generated";
        subTopic.staAnswer.GrammarWeight = "{\"weight_struct\":[[{\"weight\":0.5,\"key\":\"new york\"}]]}";
        subTopic.staAnswer.Grammar = "#JSGF V1.0 utf-8 cn;\ngrammar main;\npublic <main> = \"<s>\"(<who> lives in <where>|new york)\"</s>\";\n<who> = (she|her pen pal|jenny's pen pal);\n<where> = (new york);\n";
        expectResult[4] = Constants.GSON.toJson(subTopic.staAnswer);
        topic.subTopic.add(subTopic);
        exam.topic.add(topic);
    }

    @Test
    public void getAnswerTest() {
        int index = 0;
        for (Topic topic : exam.topic) {
            for (SubTopic subTopic : topic.subTopic) {
                assertEquals(subTopic.getAnswer(), expectResult[index]);
                index++;
            }
        }
    }
}

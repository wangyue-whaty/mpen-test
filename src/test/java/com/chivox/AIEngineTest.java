package com.chivox;


import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mp.shared.common.Exam.SubTopic;
import com.mpen.TestBase;
import com.mpen.api.domain.OralTestDetail;



/**
 * 口语考试卷对接驰声测试类
 */
public class AIEngineTest extends TestBase {
   
    private static final Logger LOGGER = LoggerFactory.getLogger(AIEngineTest.class);
  
    private static int flag=0;
    /**
     * 驰声口语考试卷测试 对驰声句子内核，朗读内核，以及Aitalk内核进行测试,该单元测试需要在linux系统系进行测试，利用命令：mvn -Dtest=AIEngineTest test 
     * 经过多次测试同一个文本，同一段音频，通过Assert.assertEquals（）方法，总分，流利度，正确度，准确度返回结果跟预期是一样的，没有一点偏差。现在设置为误差
     * 在10%，可以通过测试。
     * 根据log日志看驰声返回每次的单词文本与分数,经过多次测试，根据同一个音频文件，返回测试结果跟预期是一样的，
     * 对于同一个文本，同一段音频，不管发送多少次请求，驰声返回的评测结果一样的
     * 第一个：[{"score":3.0,"text":"sunny"},{"score":58.0,"text":"hurry"}]
     * 第二个：[{"score":"100","text":"my"},{"score":"100","text":"name"},{"score":"2","text":"is"},{"score":"95.5","text":"lisa"},{"score":"100","text":"i"},{"score":"67.5","text":"m"},{"score":"98","text":"at"},{"score":"58.5","text":"the"},{"score":"99","text":"park"},{"score":"97.5","text":"with"},{"score":"100","text":"my"},{"score":"99.5","text":"family"},{"score":"0","text":"we"},{"score":"0","text":"re"},{"score":"0","text":"having"},{"score":"0","text":"a"},{"score":"0","text":"picnic"},{"score":"0","text":"my"},{"score":"0","text":"mother"},{"score":"0","text":"is"},{"score":"0","text":"flying"},{"score":"0","text":"a"},{"score":"0","text":"kite"},{"score":"0","text":"my"},{"score":"0","text":"father"},{"score":"0","text":"is"},{"score":"0","text":"reading"},{"score":"0","text":"a"},{"score":"0","text":"newspaper"},{"score":"0","text":"my"},{"score":"0","text":"little"},{"score":"0","text":"brother"},{"score":"0","text":"is"},{"score":"0","text":"singing"},{"score":"0","text":"a"},{"score":"0","text":"song"},{"score":"0","text":"my"},{"score":"0","text":"grandma"},{"score":"0","text":"is"},{"score":"0","text":"sleeping"},{"score":"0","text":"and"},{"score":"0","text":"i"},{"score":"0","text":"m"},{"score":"0","text":"drawing"},{"score":"0","text":"a"},{"score":"0","text":"picture"},{"score":"0","text":"of"},{"score":"0","text":"my"},{"score":"0","text":"family"},{"score":"0","text":"the"},{"score":"0","text":"weather"},{"score":"0","text":"is"},{"score":"0","text":"good"},{"score":"0","text":"it"},{"score":"0","text":"s"},{"score":"0","text":"sunny"},{"score":"0","text":"and"},{"score":"0","text":"warm"}]
     * 第三个: [{"score":100.0,"text":"it"},{"score":100.0,"text":"s"},{"score":85.0,"text":"hot"}]
     */
    @Test
    public void OralTest() {
        List<OralTestDetail> oralTestDetails = this.getOralTestDetail();
        List<SubTopic> subTopics = this.getSubTopics();
        List<Map<String, Double>> score1 = getScore();
        for (int idx = 0; idx < 3; idx++) {
            OralTestDetail chivoxOralTestDetail = oralTestDetails.get(idx);
            SubTopic subTopic = subTopics.get(idx);
            new AIEngine(subTopic, chivoxOralTestDetail, new AIEngine.SaveOralTestCallback() {
                public void save(OralTestDetail chivoxOralTestDetail) {
                    Assert.assertEquals(score1.get(flag).get("score"), chivoxOralTestDetail.getScore(), 0.1);
                    Assert.assertEquals(score1.get(flag).get("fluency"), chivoxOralTestDetail.getFluency(), 10);
                    Assert.assertEquals(score1.get(flag).get("integrity"), chivoxOralTestDetail.getIntegrity(), 10);
                    Assert.assertEquals(score1.get(flag).get("pronunciation"), chivoxOralTestDetail.getPronunciation(),10);
                    LOGGER.error("单词文本与分数" + chivoxOralTestDetail.getRecognizeTxt());
                }
            }).run(getClass().getResource(chivoxOralTestDetail.getRecordingUrl()).getPath());
            ++flag;
        }
    }
}

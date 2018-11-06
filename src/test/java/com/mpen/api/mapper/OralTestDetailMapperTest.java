package com.mpen.api.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.chivox.AIEngine;
import com.mpen.TestBase;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.OralTestDetail;

/**
 * 口语考试卷测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OralTestDetailMapperTest extends TestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(OralTestDetailMapperTest.class);
    @Autowired
    OralTestDetailMapper oralTestDetailMapper;

    /**
     * 口语考试卷mapper单元测试
     */
    @Test
    public void oralTestDetailTest() {
        this.oralTestDetailTestInternal(Constants.AIENGINE_YZS);
        this.oralTestDetailTestInternal(Constants.AIENGINE_CHIVOX);
    }

    private void oralTestDetailTestInternal(int engine) {
        OralTestDetail oralTestDetail = this.getOralTest(engine);
        oralTestDetailMapper.deleteData(oralTestDetail.getLoginId(), oralTestDetail.getFkBookId(),
                oralTestDetail.getNum(), oralTestDetail.getAssessmentType());
        OralTestDetail oralTestDetail2 = oralTestDetailMapper.get(oralTestDetail.getLoginId(),
                oralTestDetail.getFkBookId(), oralTestDetail.getNum(), oralTestDetail.getAssessmentType());
        Assert.assertEquals(null, oralTestDetail2, null);
        // 保存需评测的数据到数据库
        oralTestDetailMapper.save(oralTestDetail);
        // 得到评测的数据
        List<OralTestDetail> notDealOralTestDetail = oralTestDetailMapper.getNotDeal(0,
                oralTestDetail.getAssessmentType());
        Assert.assertEquals(notDealOralTestDetail != null, true);
        // 更新评测次数
        oralTestDetailMapper.updateTimes(1, notDealOralTestDetail.get(0).getId());
        // 将云之声或驰声的评测数据更新到数据库
        oralTestDetailMapper.update(this.updateOralTestDetail(notDealOralTestDetail.get(0), engine));
        List<String> loginIds=new ArrayList<>();
        loginIds.add(notDealOralTestDetail.get(0).getLoginId());
        // 得到评测数据
        OralTestDetail detail = oralTestDetailMapper.getDetail(loginIds, notDealOralTestDetail.get(0).getFkBookId(),
                notDealOralTestDetail.get(0).getNum(), engine);
        switch (engine) {
        case Constants.AIENGINE_YZS:
            // 平均分
            Assert.assertEquals(detail.getAvgScore(), 1, 0);
            // 最高分
            Assert.assertEquals(detail.getMaxScore(), 1, 0);
            // 最低分
            Assert.assertEquals(detail.getMinScore(), 1, 0);
            // 平均流利度
            Assert.assertEquals(detail.getAvgFluency(), 91.078, 0);
            // 平均准确度
            Assert.assertEquals(detail.getAvgIntegrity(), 73.304, 0);
            // 平均标准度
            Assert.assertEquals(detail.getAvgPronunciation(), 100.0, 0);
            break;
        case Constants.AIENGINE_CHIVOX:
            // 平均分
            Assert.assertEquals(detail.getAvgScore(), 1, 0);
            // 最高分
            Assert.assertEquals(detail.getMaxScore(), 1, 0);
            // 最低分
            Assert.assertEquals(detail.getMinScore(), 1, 0);
            // 平均流利度
            Assert.assertEquals(detail.getAvgFluency(), 91.8, 0);
            // 平均准确度
            Assert.assertEquals(detail.getAvgIntegrity(), 74.4, 0);
            // 平均标准度
            Assert.assertEquals(detail.getAvgPronunciation(), 99.0, 0);
            break;
        default:
            break;
        }
        // 得到数据
        OralTestDetail oralTestDetail1 = oralTestDetailMapper.get(notDealOralTestDetail.get(0).getLoginId(),
                notDealOralTestDetail.get(0).getFkBookId(), notDealOralTestDetail.get(0).getNum(),
                notDealOralTestDetail.get(0).getAssessmentType());
        Assert.assertEquals(oralTestDetail1 != null, true);
        // 判断是否评测过
        Assert.assertEquals(oralTestDetail1.getTimes(), 1);
        double examSum = oralTestDetailMapper.getExamSumByLoginIdAndBookId(oralTestDetail1.getLoginId(),
                oralTestDetail1.getFkBookId(), oralTestDetail1.getAssessmentType());
        Assert.assertEquals(oralTestDetail1.getScore() == examSum, true);
        int oralTestPaperCount = oralTestDetailMapper.getOralTestPaperCount(oralTestDetail1.getLoginId(),
                oralTestDetail1.getFkBookId(), oralTestDetail1.getSerialNumber(), oralTestDetail1.getAssessmentType());
        Assert.assertEquals(oralTestPaperCount, 1);
        double oralTestPaperSum = oralTestDetailMapper.getOralTestPaperSum(oralTestDetail1.getLoginId(),
                oralTestDetail1.getFkBookId(), oralTestDetail1.getSerialNumber(), oralTestDetail1.getAssessmentType());
        Assert.assertEquals(oralTestPaperSum == oralTestDetail1.getScore(), true);
        // 本书籍评测的次数
        int examCount = oralTestDetailMapper.getExamCountByLoginIdAndBookId(oralTestDetail1.getLoginId(),
                oralTestDetail1.getFkBookId(), oralTestDetail1.getAssessmentType());
        Assert.assertEquals(examCount, 1);

    }
}

package com.mpen.api.mapper;

import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.domain.DdbLearnLogBookTrace;

/**
 * 测试某本书日学习时间轨迹Mapper
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbLearnLogBookTraceMapperTest extends TestBase {

    @Autowired
    DdbLearnLogBookTraceMapper ddbLearnLogBookTraceMapper;

    /**
     * 用户某本书学习时间轨迹sql测试
     */
    @Test
    public void testDdbLearnLogBookTrace() {
        final DdbLearnLogBookTrace ddbLearnLogBookTrace = this.getDdbLearnLogBookTrace();
        ddbLearnLogBookTraceMapper.delete(ddbLearnLogBookTrace.getId());
        final int saveDdbLearnLogBookTrace = ddbLearnLogBookTraceMapper.saveDdbLearnLogBookTrace(ddbLearnLogBookTrace);
        Assert.assertEquals(saveDdbLearnLogBookTrace, 1);
        final List<DdbLearnLogBookTrace> byLoginId = ddbLearnLogBookTraceMapper
                .getByLoginId(ddbLearnLogBookTrace.getFkLoginId(), ddbLearnLogBookTrace.getFkBookId());
        Assert.assertEquals(byLoginId.size(), 1);
        // 学习时间是否与预设一致
        Assert.assertEquals(byLoginId.get(0).getBookStudyTime(), 20, 0);
        // 点读页数是否与预设一致
        Assert.assertEquals(byLoginId.get(0).getLearnPage(), "P2,P3");
        // 口语评测页数是否与预设一致
        Assert.assertEquals(byLoginId.get(0).getSpeakPage(), "P2");
        // 口语评测时间是否与预设一致
        Assert.assertEquals(byLoginId.get(0).getSpokenTestTime(), 30, 0);

    }

    @Test
    public void batchUpdateTest() {
        final List<DdbLearnLogBookTrace> logBookTraces = listLogBookTraces();
        ddbLearnLogBookTraceMapper.batchUpdate(logBookTraces);
        Assert.assertTrue(true);
    }
}

package com.mpen.api.mapper;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.domain.DdbLearnLogDayTrace;

/**
 * 测试用户学情每日轨迹统计Mapper
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbLearnLogDayTraceMapperTest extends TestBase {

    @Autowired
    DdbLearnLogDayTraceMapper ddbLearnLogDayTraceMapper;

    /**
     * 用户学情每日轨迹统计sql测试
     */
    @Test
    public void TestDdbLearnLogDayTrace() {
        final DdbLearnLogDayTrace ddbLearnLogDayTrace = this.getDdbLearnLogDayTrace();
        ddbLearnLogDayTraceMapper.delete(ddbLearnLogDayTrace.getId());
        final int saveDdbLearnLogDayTrace = ddbLearnLogDayTraceMapper.saveDdbLearnLogDayTrace(ddbLearnLogDayTrace);
        Assert.assertEquals(saveDdbLearnLogDayTrace, 1);
        // 本月每天学习状况
        final List<DdbLearnLogDayTrace> byLoginAndDate = ddbLearnLogDayTraceMapper
                .getByLoginAndDate(ddbLearnLogDayTrace.getFkLoginId(), "2018", "10");
        Assert.assertEquals(byLoginAndDate.size(), 1);
        // 总时间是否与预设一致
        Assert.assertEquals(byLoginAndDate.get(0).getCountTime(), 60f, 0);
        // 课本学习时间是否与预设一致
        Assert.assertEquals(byLoginAndDate.get(0).getBookStudyTime(), 50f, 0);
        // 口语评测时间是否与预设一致
        Assert.assertEquals(byLoginAndDate.get(0).getSpokenTestTime(), 40f, 0);
        // 课后练习时间是否与预设一致
        Assert.assertEquals(byLoginAndDate.get(0).getExercisesTime(), 40f, 0);
        Assert.assertEquals(byLoginAndDate.get(0).getReadTime(), 50f, 0);
        // 其他时间是否与预设一致
        Assert.assertEquals(byLoginAndDate.get(0).getOtherTime(), 0f, 0);
        // 学习总天数是否与预设一致
        final Integer countByLoginId = ddbLearnLogDayTraceMapper.getCountByLoginId(ddbLearnLogDayTrace.getFkLoginId());
        Assert.assertEquals((int) countByLoginId, 1);
    }
    
    @Test
    public void batchUpdateTest() {
        final List<DdbLearnLogDayTrace> logDayTraces = listLogDayTraces();
        ddbLearnLogDayTraceMapper.batchUpdate(logDayTraces);
        Assert.assertTrue(true);
    }
}

package com.mpen.api.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.domain.DdbLearnLogBookSumTrace;
import org.junit.Assert;

/**
 * 用户某本书书籍学习总时间mapper测试
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbLearnLogBookSumTraceMapperTest extends TestBase {

    @Autowired
    DdbLearnLogBookSumTraceMapper ddbLearnLogBookSumTraceMapper;

    /**
     * 用户某本书课本学习，口语评测总时间 ，最近点读时间，sql测试
     */
    @Test
    public void testDdbLearnLogBookSumTrace() {
        final DdbLearnLogBookSumTrace ddbLearnLogBookSumTrace = this.getDdbLearnLogBookSumTrace();
        ddbLearnLogBookSumTraceMapper.delete(ddbLearnLogBookSumTrace.getId());
        // 保存测试数据
        final int savedDdbLearnLogBookSumTrace = ddbLearnLogBookSumTraceMapper
                .savedDdbLearnLogBookSumTrace(ddbLearnLogBookSumTrace);
        Assert.assertEquals(savedDdbLearnLogBookSumTrace, 1);
        // 根据loginId查找出该用户读过的书籍信息
        final List<DdbLearnLogBookSumTrace> byloginId = ddbLearnLogBookSumTraceMapper
                .getByloginId(ddbLearnLogBookSumTrace.getFkLoginId());
        Assert.assertEquals(byloginId.size(), 1);
        // 是否与预设的课本学习时间一致
        Assert.assertEquals(byloginId.get(0).getSumTime(), 22, 0);
        // 是否与预设的口语评测时间一致
        Assert.assertEquals(byloginId.get(0).getSpokenTestTime(), 11, 0);
        // 根据loginId和bookId查找某本书读过的信息
        final DdbLearnLogBookSumTrace byloginIdAndBookId = ddbLearnLogBookSumTraceMapper
                .getByloginIdAndBookId(ddbLearnLogBookSumTrace.getFkLoginId(), ddbLearnLogBookSumTrace.getFkBookId());
        Assert.assertEquals(byloginIdAndBookId != null, true);
        Assert.assertEquals(byloginIdAndBookId.getSumTime(), 22, 0);
        Assert.assertEquals(byloginIdAndBookId.getSpokenTestTime(), 11, 0);
    }
    
    @Test
    public void batchUpdateTest() {
        final List<DdbLearnLogBookSumTrace> logBookSumTraces = listLogBookSumTraces();
        ddbLearnLogBookSumTraceMapper.batchUpdate(logBookSumTraces);
        Assert.assertTrue(true);
    }
}

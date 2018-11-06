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
import com.mpen.api.domain.DdbLearnLogBookDetailTrace;

/**
 * 某本书学习内容详情页表轨迹表Mapper测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbLearnLogBookDetailTraceMapperTest extends TestBase {
    @Autowired
    private DdbLearnLogBookDetailTraceMapper ddbLearnLogBookDetailTraceMapper;
    
    /**
     * 测试获取点读或口语评测数据
     */
    @Test
    public void testDdbLearnLogBookDetailTrace() {
        final List<DdbLearnLogBookDetailTrace> ddbLearnLogBookDetailTraces = this.getDdbLearnLogBookDetailTraces();
        for (int i = 0; i < ddbLearnLogBookDetailTraces.size(); i++) {
            ddbLearnLogBookDetailTraceMapper.deleteById(ddbLearnLogBookDetailTraces.get(i).getId());
            // 保存课本点读或口语评测测试数据
            final int saveDdbLearnLogBookDetailTrace = ddbLearnLogBookDetailTraceMapper
                    .saveDdbLearnLogBookDetailTrace(ddbLearnLogBookDetailTraces.get(i));
            Assert.assertEquals(saveDdbLearnLogBookDetailTrace, 1);
            switch (i) {
            case 0:
                // 获取课本点读数据
                final List<DdbLearnLogBookDetailTrace> readLearnDetail = ddbLearnLogBookDetailTraceMapper
                        .getReadLearnDetail(ddbLearnLogBookDetailTraces.get(i).getFkLoginId(),
                                ddbLearnLogBookDetailTraces.get(i).getFkBookId());
                Assert.assertEquals(readLearnDetail != null, true);
                // 点读次数是否与预设一致
                Assert.assertEquals(readLearnDetail.get(i).getNumber(), 15, 0);
                break;
            case 1:
                // 获取口语评测数据
                final List<DdbLearnLogBookDetailTrace> oralLearnDetail = ddbLearnLogBookDetailTraceMapper
                        .getOralLearnDetail(ddbLearnLogBookDetailTraces.get(i).getFkLoginId(),
                                ddbLearnLogBookDetailTraces.get(i).getFkBookId());
                Assert.assertEquals(oralLearnDetail != null, true);
                // 口语评测次数是否与预设一致
                Assert.assertEquals(oralLearnDetail.get(0).getScore(), 90, 0);
                break;
            default:
                break;
            }
        }
    }
    
    @Test
    public void batchUpdateTest() {
        final List<DdbLearnLogBookDetailTrace> logBookDetailTraces = listLogBookDetailTraces();
        ddbLearnLogBookDetailTraceMapper.batchUpdate(logBookDetailTraces);
        Assert.assertTrue(true);
    }
}

package com.mpen.api.mapper;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.api.domain.DdbObtainIntegralInstruction;

/**
 * 积分字典表Mapper测试
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbObtainIntegralInstructionMapperTest {
    @Autowired
    private DdbObtainIntegralInstructionMapper ddbObtainIntegralInstructionMapper;

    /**
     * 积分字典sql测试
     */
    @Test
    public void TestDdbObtainIntegralInstructionMapper() {
        List<DdbObtainIntegralInstruction> findAll = ddbObtainIntegralInstructionMapper.findAll();
        Assert.assertEquals(findAll != null, true);
    }

}

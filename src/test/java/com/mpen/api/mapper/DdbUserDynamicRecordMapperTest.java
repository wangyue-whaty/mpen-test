package com.mpen.api.mapper;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.domain.DdbUserDynamicRecord;

/**
 * 动态Mapper测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserDynamicRecordMapperTest extends TestBase {

    @Autowired
    private DdbUserDynamicRecordMapper dynamicRecordMapper;

    /**
     * 动态sql测试
     */
    @Test
    public void testDynamic() {
        // 测试保存及查询
        final DdbUserDynamicRecord dynamicRecord = getDynamicRecord();
        dynamicRecordMapper.save(dynamicRecord);
        final DdbUserDynamicRecord record = dynamicRecordMapper.getById(dynamicRecord.getId());
        Assert.assertEquals(dynamicRecord.getId(), record.getId());
        // 测试根据loginIds获取总记录数
        final List<String> loginIds = new ArrayList<>();
        loginIds.add(dynamicRecord.getFkLoginId());
        final int totalCount = dynamicRecordMapper.getTotalCount(loginIds);
        Assert.assertTrue(totalCount >= 1);
        // 测试根据loginIds获取动态记录
        final List<DdbUserDynamicRecord> listDynamics = dynamicRecordMapper.listDynamics(0, 10, loginIds);
        Assert.assertTrue(listDynamics.size() >= 1);
    }
}

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
import com.mpen.api.domain.DdbUserClass;
import com.mpen.api.domain.DdbUserMedalRecord;

/**
 * 用户勋章记录Mapper测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserMedalRecordMapperTest extends TestBase {

    @Autowired
    private DdbUserMedalRecordMapper ddbUserMedalRecordMapper;

    /**
     * 用户勋章记录sql测试类
     */
    @Test
    public void testMedalRecordInfo() {
        final DdbUserMedalRecord ddbUserMedalRecord = getddbUserMedalRecord();
        // 新增
        final int save = ddbUserMedalRecordMapper.save(ddbUserMedalRecord);
        Assert.assertEquals(save, 1);
        // 查询用户勋章记录
        final List<DdbUserMedalRecord> list = ddbUserMedalRecordMapper.getUserMedalRecord(ddbUserMedalRecord.getFkLoginId());
        Assert.assertEquals(list != null, true);
        // 根据用户查询勋章数量
        final int sum = ddbUserMedalRecordMapper.getMedalSum(ddbUserMedalRecord.getFkLoginId());
        Assert.assertEquals(sum != 0, true);
        // 更新勋章状态
        final  int updateMedal = ddbUserMedalRecordMapper.updateMedal(ddbUserMedalRecord);
        Assert.assertEquals(updateMedal, 1);
        // 更新用户勋章穿着状态
        final  int updateMedalWear= ddbUserMedalRecordMapper.updateMedalWear(ddbUserMedalRecord.getFkLoginId(),
                ddbUserMedalRecord.getFkMedalDicId());
        Assert.assertEquals(updateMedalWear, 1);
        // 更新用户勋章为佩戴中
        final DdbUserMedalRecord medalWear = ddbUserMedalRecordMapper.getMedalWear(ddbUserMedalRecord.getFkLoginId());
        Assert.assertEquals(medalWear != null, true);
        // 更新用户勋章为已获得
        final int RemoveMedal = ddbUserMedalRecordMapper.RemoveMedal(ddbUserMedalRecord.getFkLoginId(),
                ddbUserMedalRecord.getFkMedalDicId());
        Assert.assertEquals(RemoveMedal, 1);
        // 查询用户已获得未佩戴的勋章
        final DdbUserMedalRecord recMedalRecord = ddbUserMedalRecordMapper
                .getRecMedalRecord(ddbUserMedalRecord.getFkLoginId());
        Assert.assertEquals(recMedalRecord != null, true);
        // 查询用户勋章
        final DdbUserMedalRecord byLoginIdMedalId = ddbUserMedalRecordMapper
                .getByLoginIdMedalId(ddbUserMedalRecord.getFkLoginId(), ddbUserMedalRecord.getFkMedalDicId());
        Assert.assertEquals(byLoginIdMedalId != null, true);
        // 根据id更新勋章数量
        final int num = ddbUserMedalRecordMapper.updateMedalNum(ddbUserMedalRecord);
        Assert.assertEquals(num, 1);
        // 删除
        final int delete = ddbUserMedalRecordMapper.delete(ddbUserMedalRecord);
        Assert.assertEquals(delete, 1);

    }
}

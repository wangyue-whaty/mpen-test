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
import com.mpen.api.domain.DdbUserMedalDictionary;

/**
 * 勋章字典Mapper测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserMedalDictionaryMapperTest extends TestBase {

    @Autowired
    private DdbUserMedalDictionaryMapper ddbUserMedalDictionaryMapper;

    /**
     * 勋章字典sql测试类
     */
    @Test
    public void testMedalDictionaryInfo() {
        final DdbUserMedalDictionary ddbUserMedalDictionary = getddbUserMedalDictionary();
        // 新增
        final int save = ddbUserMedalDictionaryMapper.save(ddbUserMedalDictionary);
        Assert.assertEquals(save, 1);
        // 查询全部
        final List<DdbUserMedalDictionary> all = ddbUserMedalDictionaryMapper.getAll();
        Assert.assertEquals(all != null, true);
        // 根据MEDAL_TYPE查询
        final DdbUserMedalDictionary byMedalType = ddbUserMedalDictionaryMapper
                .getByMedalType(ddbUserMedalDictionary.getMedalType());
        Assert.assertEquals(byMedalType != null, true);
        // 删除
        final int delete = ddbUserMedalDictionaryMapper.delete(ddbUserMedalDictionary);
        Assert.assertEquals(delete, 1);
    }

}

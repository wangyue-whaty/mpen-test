package com.mpen.api.mapper;

import java.util.ArrayList;
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
import com.mpen.api.domain.DdbUserDynamicPraise;

/**
 * 动态点赞Mapper测试
 *
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserDynamicPraiseMapperTest extends TestBase {
    @Autowired
    private DdbUserDynamicPraiseMapper ddbUserDynamicPraiseMapper;

    /**
     * 动态sql测试
     */
    @Test
    public void testDdbUserDynamicPraiseMapper() {
        final DdbUserDynamicPraise ddbUserDynamicPraise = getDdbUserDynamicPraise();
        final int save = ddbUserDynamicPraiseMapper.save(ddbUserDynamicPraise);
        Assert.assertEquals(save, 1);
        final List<String> dynamicIds = new ArrayList<>();
        dynamicIds.add(ddbUserDynamicPraise.getFkDynamicId());
        final List<DdbUserDynamicPraise> listByDynamicIds = ddbUserDynamicPraiseMapper.listByDynamicIds(dynamicIds);
        Assert.assertEquals(listByDynamicIds != null, true);
        final DdbUserDynamicPraise byDynamicIdLoginId = ddbUserDynamicPraiseMapper
                .getByDynamicIdLoginId(ddbUserDynamicPraise.getFkLoginId(), ddbUserDynamicPraise.getFkDynamicId());
        Assert.assertEquals(byDynamicIdLoginId != null, true);
        final int delete = ddbUserDynamicPraiseMapper.delete(ddbUserDynamicPraise.getFkDynamicId());
        Assert.assertEquals(delete, 1);
    }

}

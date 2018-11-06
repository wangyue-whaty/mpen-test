package com.mpen.api.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.domain.DdbUserPraiseRelationship;

/**
 * 点赞测试Mapper
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserPraiseRelationshipMapperTest extends TestBase{
    
    @Autowired
    private DdbUserPraiseRelationshipMapper ddbUserPraiseRelationshipMapper;
    
    /**
     * 
     * 点赞测试sql
     */
    @Test
    public void DdbUserPraiseRelationshipMapperTest01() {
        final String beforeDate = "2018-10-20 18:00:41";
        final String todayDate = "2018-10-22 18:00:41";
        final DdbUserPraiseRelationship ddbuserPraiseRelationship = getDdbuserPraiseRelationship();
        final int save = ddbUserPraiseRelationshipMapper.save(ddbuserPraiseRelationship);
        Assert.assertEquals(save, 1);
        final int praiseCount = ddbUserPraiseRelationshipMapper.getPraiseCount(ddbuserPraiseRelationship.getByPraiseLoginId(), beforeDate, todayDate);
        Assert.assertEquals(praiseCount, 1);
    }

}

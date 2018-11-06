package com.mpen.api.mapper;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.api.domain.DdbUserComment;

/**
 * 教师固定字典测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserCommentsMapperTest {

    @Autowired
    private DdbUserCommentsMapper DdbUserCommentsMapper;

    /**
     * 学生班级关系sql测试
     */
    @Test
    public void testDdbUserComments() {
        List<DdbUserComment> allComments = DdbUserCommentsMapper.getAllComments();
        Assert.assertEquals(allComments != null, true);

    }
}

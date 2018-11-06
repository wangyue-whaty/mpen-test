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
import com.mpen.api.bean.HomeWorks;
import com.mpen.api.bean.Student;
import com.mpen.api.domain.DdbUserHomework;

/**
 * 作业Mapper测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserHomeworkMapperTest extends TestBase {
    @Autowired
    private DdbUserHomeworkMapper ddbUserHomeworkMapper;

    /**
     * 作业sql测试
     */
    @Test
    public void testDdbUserHomework() {
        for (DdbUserHomework ddbUserHomework : getDdbUserHomework()) {
            // 保存作业，课前导学，课后作业
            final int save = ddbUserHomeworkMapper.save(ddbUserHomework);
            Assert.assertEquals(save, 1);
            // 根据作业ID查询
            final DdbUserHomework byId = ddbUserHomeworkMapper.getById(ddbUserHomework.getId());
            Assert.assertEquals(byId != null, true);
            // 根据作业ID和班级ID查询作业
            final List<DdbUserHomework> homeWorks = ddbUserHomeworkMapper.getHomeWorks(ddbUserHomework.getFkLoginId(),
                    getDdbUserHomework().get(0).getFkClassId());
            Assert.assertEquals(homeWorks != null, true);
            // 根据作业类型，班级ID，开始时间，截止时间查询作业信息
            final HomeWorks homeWorks2 = getHomeWorks();
            homeWorks2.setType(ddbUserHomework.getType());
            homeWorks2.setFkClassId(ddbUserHomework.getFkClassId());
            final List<DdbUserHomework> listHomeWorks = ddbUserHomeworkMapper.listHomeWorks(homeWorks2);
            Assert.assertEquals(listHomeWorks != null, true);
            // 获取班级作业总记录数
            final int totalCount = ddbUserHomeworkMapper.getTotalCount(homeWorks2);
            Assert.assertEquals(totalCount, 0);
            // 获取班级未做作业总人数
            final List<Student> studentList = ddbUserHomeworkMapper.getStudentList(ddbUserHomework.getId());
            Assert.assertEquals(studentList.size(), 0);
            // 获取班级已经做人数
            final List<Student> subStudentList = ddbUserHomeworkMapper.getSubStudentList(ddbUserHomework.getId());
            Assert.assertEquals(subStudentList.size(), 0);
            final int delete = ddbUserHomeworkMapper.delete(ddbUserHomework.getId());
            Assert.assertEquals(delete, 1);
        }
    }
}

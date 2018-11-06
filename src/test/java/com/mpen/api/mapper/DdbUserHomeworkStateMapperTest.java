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
import com.mpen.api.bean.Student;
import com.mpen.api.domain.DdbUserHomeworkState;

/**
 * 学生作业状态Mapper测试
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserHomeworkStateMapperTest extends TestBase {
    @Autowired
    private DdbUserHomeworkStateMapper ddbUserHomeworkStateMapper;

    /**
     * 学生作业状态sql测试
     */
    @Test
    public void testDdbUserHomeworkState() {
        DdbUserHomeworkState ddbUserHomeworkState = getDdbUserHomeworkState();
        // 保存学生的作业
        final int save = ddbUserHomeworkStateMapper.save(ddbUserHomeworkState);
        Assert.assertEquals(save, 1);
        // 获取已经批阅学生数量
        final int hasReviewNum = ddbUserHomeworkStateMapper.getHasReviewNum(ddbUserHomeworkState.getFkHomeworkId());
        Assert.assertEquals(hasReviewNum, 0);
        // 获取未批阅学生数量
        final int hasNoReviewNum = ddbUserHomeworkStateMapper.getHasNoReviewNum(ddbUserHomeworkState.getFkHomeworkId());
        Assert.assertEquals(hasNoReviewNum, 1);
        // 获取未评论学生信息
        final DdbUserHomeworkState homework = ddbUserHomeworkStateMapper
                .getHomework(ddbUserHomeworkState.getFkLoginId(), ddbUserHomeworkState.getFkHomeworkId());
        Assert.assertEquals(homework != null, true);
        // 获取未提交的学生数量
        final int noSubmitNum = ddbUserHomeworkStateMapper.getNoSubmitNum(ddbUserHomeworkState.getFkHomeworkId());
        Assert.assertEquals(noSubmitNum, 0);
        // 根据是否评论是否查询学生作业信息
        final List<DdbUserHomeworkState> DdbUserHomeworkStates = ddbUserHomeworkStateMapper
                .getByBookIdAndType(ddbUserHomeworkState.getFkHomeworkId(), ddbUserHomeworkState.getIsMarking());
        Assert.assertEquals(DdbUserHomeworkStates.size(), 1);
        final int updateStudentHomeWork = ddbUserHomeworkStateMapper
                .updateStudentHomeWork(getStudent01(ddbUserHomeworkState));
        Assert.assertEquals(updateStudentHomeWork, 1);
        // 获取学生作业信息
        final DdbUserHomeworkState sudentHomeworkDetails = ddbUserHomeworkStateMapper
                .getSudentHomeworkDetails(ddbUserHomeworkState.getFkLoginId(), ddbUserHomeworkState.getFkHomeworkId());
        Assert.assertEquals(sudentHomeworkDetails != null, true);
       
        // 根据作业id获取学生logInId
        final List<String> loginId = ddbUserHomeworkStateMapper.getLoginId(ddbUserHomeworkState.getFkHomeworkId());
        Assert.assertEquals(loginId.size(), 1);
        // 获取提交作业的数量
        final int submitNum = ddbUserHomeworkStateMapper.getSubmitNum(ddbUserHomeworkState.getFkHomeworkId());
        Assert.assertEquals(submitNum, 1);
        // 某个学生的作业信息
        List<String> homeworkId = new ArrayList<>();
        homeworkId.add(ddbUserHomeworkState.getFkHomeworkId());
        final List<DdbUserHomeworkState> listByLoginId = ddbUserHomeworkStateMapper.listByLoginId(homeworkId,
                ddbUserHomeworkState.getFkLoginId());
        Assert.assertEquals(listByLoginId.size(), 1);
        // 获取某个学生单个作业信息
        final DdbUserHomeworkState byWorkId = ddbUserHomeworkStateMapper
                .getByWorkId(ddbUserHomeworkState.getFkHomeworkId(), ddbUserHomeworkState.getFkLoginId());
        Assert.assertEquals(byWorkId != null, true);
    }
}

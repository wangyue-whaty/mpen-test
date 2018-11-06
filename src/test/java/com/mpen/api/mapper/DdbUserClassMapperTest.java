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
import com.mpen.api.domain.DdbUserClassRela;

/**
 * 班级Mapper测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserClassMapperTest extends TestBase {

    @Autowired
    private DdbUserClassMapper ddbUserClassMapper;

    @Autowired
    private DdbUserClassRelaMapper ddbUserClassRelaMapper;
    /**
     * 操作班级sql测试类
     */
    @Test
    public void testClassInfo() {
        List<DdbUserClass> ddbUserClass = getDdbUserClass();
        // 班级编号
        final String classNumber = ddbUserClassMapper.getClassNumber();
        if (classNumber == null) {
            ddbUserClass.get(0).setClassNumber(String.format("%06d", 1));
        } else {
            ddbUserClass.get(0).setClassNumber(String.format("%06d", Long.parseLong(classNumber) + 1));
        }
        // 老师新增班级
        final int saveClass = ddbUserClassMapper.saveClass(ddbUserClass.get(0));
        Assert.assertEquals(saveClass, 1);
        // 学生新增班级
        final int create = ddbUserClassMapper.create(ddbUserClass.get(1));
        Assert.assertEquals(create, 1);
        // 根据老师loginId查询出班级信息
        final List<DdbUserClass> byLoginId = ddbUserClassMapper.getByLoginId(ddbUserClass.get(0).getFkLoginId());
        Assert.assertEquals(byLoginId != null, true);
        // 根据老师loginId，班级name，年级查询出班级ID
        final String classId = ddbUserClassMapper.getFkClassId(ddbUserClass.get(0).getFkLoginId(),
                ddbUserClass.get(0).getClassName());
        // 往该班级新增学生
        DdbUserClassRela userClassRela01 = getUserClassRela01(classId);
        int create2 = ddbUserClassRelaMapper.create(userClassRela01);
        Assert.assertEquals(create2, 1);
        // 查询该学生是否在该班级
        DdbUserClass classByLoginId = ddbUserClassMapper.getClassByLoginId(userClassRela01.getFkLoginId());
        Assert.assertEquals(classByLoginId != null, true);
        // 删除该学生
        int deleteById = ddbUserClassRelaMapper.deleteById(userClassRela01.getId());
        Assert.assertEquals(deleteById, 1);
        final String StudentclassId = ddbUserClassMapper.getFkClassId(ddbUserClass.get(1).getFkLoginId(),
                ddbUserClass.get(1).getClassName());
        Assert.assertEquals(classId != null, true);
        // 根据loginId查询出班级ID
        final List<String> classId2 = ddbUserClassMapper.getClassId(ddbUserClass.get(0).getFkLoginId(), null);
        Assert.assertEquals(classId2 != null, true);
        // 根据loginId、班级、年级查询出班级ID
        final List<String> classId3 = ddbUserClassMapper.getClassId(ddbUserClass.get(0).getFkLoginId(),
                ddbUserClass.get(0).getClassName());
        Assert.assertEquals(classId3 != null, true);
        // 根据classId查询出班级信息
        final DdbUserClass byClassId = ddbUserClassMapper.getByClassId(classId);
        Assert.assertEquals(byClassId != null, true);
        // 根据班级id删除班级信息
        final int delete = ddbUserClassMapper.delete(classId);
        final int deleteStu = ddbUserClassMapper.delete(StudentclassId);
        ddbUserClassMapper.delete(classId);
        ddbUserClassMapper.delete(StudentclassId);
        Assert.assertEquals(delete, 1);
        Assert.assertEquals(deleteStu, 1);
    }
}

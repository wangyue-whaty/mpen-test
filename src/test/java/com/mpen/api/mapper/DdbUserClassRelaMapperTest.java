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
import com.mpen.api.domain.DdbUserClassRela;
import com.mpen.api.util.CommUtil;

/*
*
 * 学生班级关系Mapper测试
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserClassRelaMapperTest extends TestBase {
    @Autowired
    private DdbUserClassRelaMapper ddbUserClassRelaMapper;

    /**
     * 学生班级关系sql测试
     */
    @Test
    public void testUserClassReal() {
        final DdbUserClassRela userClassRela = getUserClassRela();
        // 创建学生班级关系记录
        final int create = ddbUserClassRelaMapper.create(userClassRela);
        Assert.assertEquals(create, 1);
        // 根据班级id查询信息
        final List<DdbUserClassRela> listByClassId = ddbUserClassRelaMapper
                .getDdbUserClassRelaByClassId(userClassRela.getFkClassId());
        Assert.assertEquals(listByClassId.size(), 1);
        // 根据班级id查询loginId
        final List<String> byClassId = ddbUserClassRelaMapper.getByClassId(userClassRela.getFkClassId());
        Assert.assertEquals(byClassId.size(), 1);
        // 根据loginId查询信息
        final DdbUserClassRela ddbUserClassRela0 = ddbUserClassRelaMapper.getByLoginId(userClassRela.getFkLoginId());
        Assert.assertEquals(ddbUserClassRela0 != null, true);
        // 根据班级id集合查询信息
        final List<String> ddbUserClassRelas = new ArrayList<>();
        ddbUserClassRelas.add(userClassRela.getFkClassId());
        final List<DdbUserClassRela> ddbUserClassRela = ddbUserClassRelaMapper.getDdbUserClassRela(ddbUserClassRelas);
        Assert.assertEquals(ddbUserClassRela.size(), 1);
        final int deleteById = ddbUserClassRelaMapper.deleteById(ddbUserClassRela0.getId());
        Assert.assertEquals(deleteById, 1);
    }
   
}

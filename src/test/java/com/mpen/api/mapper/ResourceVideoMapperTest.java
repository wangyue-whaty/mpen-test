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
import com.mpen.api.domain.DdbResourceVideo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ResourceVideoMapperTest  extends TestBase{
    @Autowired
    private ResourceVideoMapper resourceVideoMapper;
    
    /**
     * 测试无老师班级推送视频
     */
    @Test
    public void getNoTeacherClassVideoTest() {
        final String model = "M10";
        final String bookId = "ff808081581deb4101581e74ac7d0088";
        final List<DdbResourceVideo> noClassVideo = resourceVideoMapper.getNoTeacherClassVideo(model, bookId);
        Assert.assertEquals(noClassVideo.size() > 0, true);
    }
}

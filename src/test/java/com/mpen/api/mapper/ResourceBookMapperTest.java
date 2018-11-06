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
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbResourceBook;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ResourceBookMapperTest extends TestBase{
    
    @Autowired
    private ResourceBookMapper resourceBookMapper;
    
    /** 支持云点读图书类型: 1 */
    public static final int CLOUD_READ_TYPE = 1;
    /** 仅支持本地点读图书类型: 2 */
    public static final int LOCAL_READ_TYPE = 2;
    
    @Test
    public void testGetValidBooks() {
        final int[] TYPES = { -1, CLOUD_READ_TYPE, LOCAL_READ_TYPE };
        for (int lineReadType: TYPES) {
            // 先查询数据库中ddb_resource_book表中的数据总条数
            List<DdbResourceBook> list = resourceBookMapper.getValidBooks();
            // 创建一条数据
            DdbResourceBook book = getDdbResourceBook(lineReadType);
            resourceBookMapper.create(book);
            // 再次查询ddb_resource_book表中的数据总条数
            List<DdbResourceBook> lastList = resourceBookMapper.getValidBooks();
            if (lineReadType == -1) {
                // 校验两次总条数是否相同,相同则测试通过
                Assert.assertEquals(list.size(),lastList.size());
            } else {
                // 校验第二次查询是否比第一次多一条,相同则测试通过
                Assert.assertEquals(list.size() + 1, lastList.size());
            }
            // 测试完成删除新添加的数据
            resourceBookMapper.delete(book.getId());
        }
    }
    
    @Test
    public void testCreateBook() {
        final int[] TYPES = { -1, CLOUD_READ_TYPE, LOCAL_READ_TYPE };
        for (int lineReadType: TYPES) {
            // 先创建一条数据
            DdbResourceBook book = getDdbResourceBook(lineReadType);
            book.setIsLineRead(1);
            resourceBookMapper.create(book);
            // 根据id查询该条数据
            DdbResourceBook dbook = resourceBookMapper.getId(book.getId());
            // 能查询到,测试通过
            Assert.assertEquals(dbook != null, true);
            // 测试完成删除新添加的数据
            resourceBookMapper.delete(book.getId());
        }
    }
    
    @Test
    public void testUpdateBook() {
        // 先创建一条数据
        final DdbResourceBook book = getDdbResourceBook(CLOUD_READ_TYPE);
        resourceBookMapper.create(book);
        // 根据id查询该条数据
        final DdbResourceBook dbook = resourceBookMapper.getId(book.getId());
        final String testMd5Str = "4sn420znoqaqiwen120cfzmn31f01";
        // 能查询到,测试通过
        Assert.assertEquals(dbook != null, true);
        // 更新mppLink,mpvLink,teachLink
        dbook.setMppLink("123");
        dbook.setMpvLink("456");
        dbook.setTeachLink("798");
        // 更新mpLinkMd5,mppLinkMd5,mpvLinkMd5,teachLinkMd5
        dbook.setMppLinkMd5(testMd5Str);
        dbook.setMpvLinkMd5(testMd5Str);
        dbook.setMppLinkMd5(testMd5Str);
        dbook.setTeachLinkMd5(testMd5Str);
        resourceBookMapper.update(dbook);
        // 再次查询,校验是否更新成功
        final DdbResourceBook dbook1 = resourceBookMapper.getId(book.getId());
        Assert.assertEquals(dbook1.getMppLink(), dbook.getMppLink());
        Assert.assertEquals(dbook1.getMpvLink(), dbook.getMpvLink());
        Assert.assertEquals(dbook1.getTeachLink(), dbook.getTeachLink());
        
        Assert.assertEquals(dbook1.getMppLinkMd5(), dbook.getMppLinkMd5());
        Assert.assertEquals(dbook1.getMpvLinkMd5(), dbook.getMpvLinkMd5());
        Assert.assertEquals(dbook1.getMppLinkMd5(), dbook.getMppLinkMd5());
        Assert.assertEquals(dbook1.getTeachLinkMd5(), dbook.getTeachLinkMd5());
        // 测试完成删除新添加的数据
        resourceBookMapper.delete(book.getId());
    }
    

}

package com.mpen.api.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.domain.DdbBookCoreDetail;


/**
 * 存储bookListsql测试类
 * @author wangyue
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookCoreDetailMapperTest extends TestBase{

    @Autowired
    private BookCoreDetailMapper bookCoreDetailMapper;
    
    /**
     * 测试BookCoreDetailMapper中的所有方法
     * 步骤:1.先将正式版本信息查询出来,之后恢复数据使用(恢复版本)
     * 2.创建bookList
     * 3.将创建的bookList设置成正式版本
     * 4.将正式版本信息查询出来,校验version是否为创建的,IsActive是否为true
     * 5.查询出来的正式版本设置为非正式版本
     * 6.再次查询正式版本,校验不是第一次创建的版本
     * 7.恢复正式版本
     * 8.将除指定版本(恢复版本)外的版本设置为非正式版本
     * 9.再次查询,与恢复版本比较
     */
    @Test
    public void testBookCoreDetailMapper() {
        // 先将正式版本信息查询出来,之后恢复数据使用(恢复版本)
        DdbBookCoreDetail rollbackDetail = bookCoreDetailMapper.get(true);
        DdbBookCoreDetail bookDetail = getDdbBookCoreDetail();
        // 创建bookList
        bookCoreDetailMapper.save(bookDetail);
        // 设置成正式版本
        bookCoreDetailMapper.activate(bookDetail.getVersion(), true);
        // 将正式版本信息查询出来,校验version是否为创建的,IsActive是否为true
        DdbBookCoreDetail ddbBookCoreDetail = bookCoreDetailMapper.get(true);
        Assert.assertEquals(ddbBookCoreDetail.getIsActive(), true);
        Assert.assertEquals(ddbBookCoreDetail.getVersion(), bookDetail.getVersion());
        // 查询出来的正式版本设置为非正式版本
        bookCoreDetailMapper.activate(ddbBookCoreDetail.getVersion(), false);
        // 再次查询正式版本,校验不是第一次创建的版本
        DdbBookCoreDetail ddbBookCoreDetail1 = bookCoreDetailMapper.get(true);
        Assert.assertNotEquals(ddbBookCoreDetail1.getVersion(), bookDetail.getVersion());
        // 恢复正式版本
        bookCoreDetailMapper.activate(rollbackDetail.getVersion(), true);
        // 将除指定版本(恢复版本)外的版本设置为非正式版本
        bookCoreDetailMapper.inActivateOtherThan(rollbackDetail.getVersion());
        // 再次查询,与恢复版本比较
        DdbBookCoreDetail ddbBookCoreDetail3 = bookCoreDetailMapper.get(true);
        Assert.assertEquals(ddbBookCoreDetail3.getVersion(),rollbackDetail.getVersion());
    }
}


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
import com.mpen.api.domain.DdbPageDetail;

/**
 * pageInfo单元测试
 * @author wangyue
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PageDetailMapperTest extends TestBase{

    @Autowired
    private PageDetailMapper pageDetailMapper;
    
    /**
     * 测试BookCoreDetailMapper中的所有方法
     * 步骤:
     * 1.查询所有书的正式版本pageInfo,测试步骤具体看测试环境(本地存在正式版本pageInfo信息,所以能查询到信息)
     * 2.创建一个pageInfo,默认为非正式版本
     * 3.将创建的pageInfo标记为正式版本
     * 4.查询此书的正式版本信息,校验是否为正式版本,是否为创建的书
     * 5.再次查询所有书的正式版本pageInfo,校验是否包含标记的正式版本
     * 6.校验pageInfoList集合中是否包含刚创建的pageInfo
     * 7. 查询出来的正式版本设置为非正式版本
     * 8.再次查询,查不到
     * 9.将除指定版本外的版本设置为非正式版本
     * 10.查询此书的正式版本是否不存在
     * 11.再次查询所有书的正式版本pageInfo,校验pageInfoList集合中是否不包含刚创建的pageInfo
     */
    @Test
    public void testBookCoreDetailMapper() {
        DdbPageDetail pageDetail = getDdbPageDetail();
        // 查询所有书的正式版本pageInfo,测试步骤具体看测试环境(本地存在正式版本pageInfo信息,所以能查询到信息)
        List<DdbPageDetail>  pageDetails = pageDetailMapper.getDdbPageDetailList();
        Assert.assertNotEquals(pageDetails, null);
        // 创建一个pageInfo,默认为非正式版本
        pageDetailMapper.save(pageDetail);
        // 将创建的pageInfo标记为正式版本
        pageDetailMapper.activate(pageDetail.getVersion(), true, pageDetail.getBookId());
        // 查询此书的正式版本信息,校验是否为正式版本,是否为创建的书
        DdbPageDetail ddbPageDetail = pageDetailMapper.get(pageDetail.getBookId(), true);
        Assert.assertEquals(ddbPageDetail.getIsActive(), true);
        Assert.assertEquals(ddbPageDetail.getVersion(), pageDetail.getVersion());
        // 再次查询所有书的正式版本pageInfo,校验是否包含标记的正式版本
        pageDetails = pageDetailMapper.getDdbPageDetailList();
        Assert.assertNotEquals(pageDetails, null);
        // 校验pageInfoList集合中是否包含刚创建的pageInfo
        boolean isExits = false;
        for (DdbPageDetail detail : pageDetails) {
            if (detail.getVersion().equals(pageDetail.getVersion())) {
                isExits = true;
            }
        }
        Assert.assertEquals(isExits, true);
        // 查询出来的正式版本设置为非正式版本
        pageDetailMapper.activate(ddbPageDetail.getVersion(), false, pageDetail.getBookId());
        // 再次查询,查不到
        DdbPageDetail ddbPageDetail1 = pageDetailMapper.get(pageDetail.getBookId(), true);
        Assert.assertEquals(ddbPageDetail1, null);
        // 将除指定版本外的版本设置为非正式版本
        pageDetailMapper.inActivateOtherThan(pageDetail.getVersion(), pageDetail.getBookId());
        // 查询此书的正式版本是否不存在
        DdbPageDetail ddbPageDetail2 = pageDetailMapper.get(pageDetail.getBookId(), true);
        Assert.assertEquals(ddbPageDetail2,null);
        // 再次查询所有书的正式版本pageInfo,校验pageInfoList集合中是否不包含刚创建的pageInfo
        pageDetails = pageDetailMapper.getDdbPageDetailList();
        Assert.assertNotEquals(pageDetails, null);
        boolean isNotExits = true;
        for (DdbPageDetail detail : pageDetails) {
            if (detail.getVersion().equals(pageDetail.getVersion())) {
                isNotExits = false;
            }
        }
        Assert.assertEquals(isNotExits, true);
    }
}

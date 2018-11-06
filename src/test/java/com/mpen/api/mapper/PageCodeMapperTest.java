package com.mpen.api.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.domain.DdbResourcePageCode;
import com.mpen.api.domain.DdbResourcePageScope;

/**
 * PageCodeMapper测试类
 * @author wangyue
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PageCodeMapperTest extends TestBase{

    @Autowired
    private PageCodeMapper pageCodeMapper;
    @Autowired
    private PageScopeMapper pageScopeMapper;
    
    /**
     * pageCode与pageScope联合测试,主要测试方法为getPageCodeByPageId
     */
    @Test
    public void testPageCodeMapper() {
        final DdbResourcePageCode page = getDdbResourcePageCode();
        final DdbResourcePageScope pageScope = getDdbResourcePageScope();
        pageScope.setFkPageId(page.getId());
        // 添加:查询语句是关联查询,所以需要在两张关联表中分别添加一条数据
        pageCodeMapper.save(page);
        pageScopeMapper.save(pageScope);
        // 查询
        DdbResourcePageCode ddbResourcePageCode = pageCodeMapper.getPageCodeByPageId(pageScope.getId());
        Assert.assertEquals(true, ddbResourcePageCode != null);
        // 删除
        pageCodeMapper.deleteById(page.getId());
        pageScopeMapper.deleteById(pageScope.getId());
        // 查询
        ddbResourcePageCode = pageCodeMapper.getPageCodeByPageId(page.getId());
        Assert.assertEquals(true, ddbResourcePageCode == null);
    }
}

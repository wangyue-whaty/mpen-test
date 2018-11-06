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

import com.mpen.api.domain.DdbPeCustom;

/**
 * 笔用户mapper测试
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PeCustomMapperTest {
    private  final static String loginId="18931334240"; 
    private final static String cover="https://inside1.mpen.com.cn/incoming/ddb/coverFiles/20181031/1540985985268-2.jpg?auth_key=1541590785-0-0-7679c477b08ab00e53ef5ced16dd39c4";
    @Autowired
    private PeCustomMapper peCustomMapper;

    /**
     * 笔用户mapper测试
     */
    @Test
    public void peCustomMapperTest() {
        final List<String> loginIds = new ArrayList<>();
        loginIds.add(loginId);
        final List<DdbPeCustom> listByLoginIds = peCustomMapper.listByLoginIds(loginIds);
        Assert.assertEquals(listByLoginIds.size() == 1, true);
        peCustomMapper.updateCoverByLoginId(loginId, cover);
        Assert.assertEquals(listByLoginIds.size() == 1, true);
    }
}

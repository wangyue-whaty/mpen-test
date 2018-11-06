package com.mpen.api.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.bean.UserIntegralSet;

/**
 * 积分控制层测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntegralControllerTest extends TestBase {
    private static final String userName = "18931334240";
    private static final String password = "5455001765033-1540449365985-088c7c75d9bcd8854450390aa9d206df";
    private static final String INTEGRAL_URL="/v1/integral/userRecord";

    /**
     * 测试积分controlelr
     */
    @Test
    public void testIntegralController() {
        // 获取总排行榜
        this.getControllerTest(userName, password, INTEGRAL_URL+"?action=getRankingList&pageNo=1&pageSize=10");
        // 获取个人积分列表
        this.getControllerTest(userName, password, INTEGRAL_URL+"?action=integralList&pageNo=1&pageSize=10");
        // 获取好友排行榜
        this.getControllerTest(userName, password,
                INTEGRAL_URL+"?action=getFriendsList&pageNo=1&pageSize=10");
        // 获取总排行榜个人积分情况
        this.getControllerTest(userName, password, INTEGRAL_URL+"?action=getPersonalRanking&type=0");
        // 获取个人近期7天的积分
        this.getControllerTest(userName, password, INTEGRAL_URL+"?action=getRecentIntegral");
        UserIntegralSet userIntegralSet = new UserIntegralSet();
        userIntegralSet.setAction("updatePraiseNum");
        userIntegralSet.setPraiseLoginId(userName);
        userIntegralSet.setByPraiseLoginId(userName);
        userIntegralSet.setType("0");
        // 点赞
        this.postControllerTest(userName, password, userIntegralSet, INTEGRAL_URL);
    }
}

package com.mpen.api.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;

/**
 * 勋章测试类
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MedalControllerTest extends TestBase {

    private static final String MEDAL_USERECORD_URI = "/v1/medal/userRecord";

    private static final String userName = "18931334240";
    private static final String password = "2329403848055-1536284387332-59dba675cb32945849ffb6ff29e7ddbc";

    /**
     * 勋章测试
     */
    @Test
    public void testMedal() {
        // 获取用户佩戴的勋章信息的测试
        this.getControllerTest(userName, password, MEDAL_USERECORD_URI + "?action=integralList");
        // 用户佩戴测试
        this.postControllerTest(userName, password, myMedalWear(), MEDAL_USERECORD_URI);
        // 用户取下勋章的测试
        this.postControllerTest(userName, password, myMedalOff(), MEDAL_USERECORD_URI);
    }

}
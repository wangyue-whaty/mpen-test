/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mp.shared.common.SuccessResult;
import com.mpen.TestBase;
import com.mpen.api.bean.Message;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.mapper.PushRecordMapper;
import com.mpen.api.service.PushRecordService;

/**
 * PushRecordServiceImplTest.
 * 
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PushRecordServiceImplTest extends TestBase {
    @InjectMocks
    @Autowired
    PushRecordService pushRecordService;
    @Autowired
    PushRecordMapper pushRecordMapper;

    @BeforeClass
    public static void setUp() throws Exception {
        MockitoAnnotations.initMocks(PushRecordServiceImplTest.class);
    }

    @Test
    public void testSavePushBatterySuccess() throws Exception {
        UserSession userSession = getTestUserSession();
        Message message = new Message();
        SuccessResult result = pushRecordService.save(message, userSession);
        Assert.assertEquals(result.getSuccess(), true);
    }

    @Test
    public void testSavePushVideoSuccess() throws Exception {
        UserSession userSession = getTestUserSession();
        Message message = new Message();
        message.setType(Constants.ONE);
        message
            .setPath("videoForSeg/yunPan/201612/whatycloud/info%40mpen.com.cn/20161209100342/ff808081567761c20156920e3b7506c2/m10u2a4.mp4.VIDEOSEGMENTS/meta.json");
        SuccessResult result = pushRecordService.save(message, userSession);
        Assert.assertEquals(result.getSuccess(), true);
    }

    @Test
    public void testSavePushVideoWithoutPathError() throws Exception {
        UserSession userSession = getTestUserSession();
        Message message = new Message();
        message.setType(Constants.ONE);
        try {
            pushRecordService.save(message, userSession);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), Constants.PUSH_ERROR);
        }
    }
}

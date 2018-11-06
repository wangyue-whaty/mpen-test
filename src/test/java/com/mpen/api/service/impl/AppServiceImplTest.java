/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import org.apache.commons.lang3.StringUtils;
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

import com.mpen.TestBase;
import com.mpen.api.domain.DdbApp;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.exception.SdkException;
import com.mpen.api.service.AppService;

/**
 * AppServiceImplTest.
 * 
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AppServiceImplTest extends TestBase {
    @InjectMocks
    @Autowired
    AppService appService;

    @BeforeClass
    public static void setUp() throws Exception {
        MockitoAnnotations.initMocks(AppServiceImplTest.class);
    }

    @Test
    public void testUpgradeAppWithVersionSuccess() throws Exception {
        DdbPePen pen = getTestDdbPePen();
        DdbApp map = appService.getAppMessageByPenId(pen.getIdentifiaction(), pen.getAppVersion());
        Assert.assertEquals(StringUtils.isNotBlank(map.getId()), true);
    }

    @Test
    public void testUpgradeAppWithoutVersionSuccess() throws Exception {
        DdbPePen pen = getTestDdbPePen();
        try {
            appService.getAppMessageByPenId(pen.getIdentifiaction(), "");
        } catch (Exception exception) {
            Assert.assertEquals(exception.getMessage(), "Invalid parameter");
        }
    }

    @Test
    public void testUpgradeAppNoMachingPenError() throws SdkException {
        DdbPePen pen = getTestDdbPePen();
        try {
            appService.getAppMessageByPenId(pen.getIdentifiaction(), "");
        } catch (Exception exception) {
            Assert.assertEquals(exception.getMessage(), "Invalid parameter");
        }
    }

}

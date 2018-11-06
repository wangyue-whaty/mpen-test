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
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.PePenMapper;
import com.mpen.api.service.PrPenCustomService;

/**
 * PrPenCustomServiceImplTest.
 * 
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PrPenCustomServiceImplTest extends TestBase {
    @InjectMocks
    @Autowired
    PrPenCustomService prPenCustomService;
    @Autowired
    PePenMapper pePenMapper;

    @BeforeClass
    public static void setUp() throws Exception {
        MockitoAnnotations.initMocks(PrPenCustomServiceImplTest.class);
    }

    @Test
    public void testCheckBindRelationshipSuccess() throws SdkException {
        DdbPePen pen = getTestDdbPePen();
        UserSession user = getTestUserSession();
        SuccessResult result = prPenCustomService.checkBindRelationship(pen.getIdentifiaction(), user);
        Assert.assertEquals(result.getSuccess(), true);
    }

    @Test
    public void testCheckBindRelationshipNoMachingPenError() throws SdkException {
        DdbPePen pen = getTestNoMachingDdbPePen();
        UserSession user = getTestUserSession();
        try {
            prPenCustomService.checkBindRelationship(pen.getIdentifiaction(), user);
        } catch (Exception exception) {
            Assert.assertEquals(exception.getMessage(), Constants.NO_MACHING_PEN);
        }
    }
}

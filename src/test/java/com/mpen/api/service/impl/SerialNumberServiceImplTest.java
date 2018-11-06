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

import com.mpen.TestBase;
import com.mpen.api.bean.Pen;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.PenSerialNumberRelationshipMapper;
import com.mpen.api.service.SerialNumberService;

/**
 * AnrFileServiceImplTest.
 * 
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SerialNumberServiceImplTest extends TestBase {
    @InjectMocks
    @Autowired
    SerialNumberService serialNumberService;
    @Autowired
    PenSerialNumberRelationshipMapper penSerialNumberRelationshipMapper;

    @BeforeClass
    public static void setUp() throws Exception {
        MockitoAnnotations.initMocks(SerialNumberServiceImplTest.class);
    }

    @Test
    public void testSaveSuccess() throws SdkException {
        // 两种类型的笔测试:V8C,V9
        final String[] serialNum = { "V917060000078", "V8C18070000001" };
        for(String num : serialNum) {
            Pen pen = getTestPen(num);
            Boolean result = serialNumberService.saveRelationship(pen);
            Assert.assertEquals(result, true);
            // 删除测试数据
            penSerialNumberRelationshipMapper.deleteBySerialNumber(pen.getSerialNumber());
        }
    }

    @Test
    public void testSaveWithWrongPenIdError() throws SdkException {
        Pen pen = getTestOnePen();
        try {
            serialNumberService.saveRelationship(pen);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "该笔已绑定过序列号！");
        }
    }

    @Test
    public void testSaveWithWrongSerialNumberError() throws SdkException {
        Pen pen = getTestTwoPen();
        try {
            serialNumberService.saveRelationship(pen);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "该序列号无效！");
        }
    }

    @Test
    public void testSaveWithAlreadyUseSerialNumberError() throws SdkException {
        Pen pen = getTestThreePen();
        try {
            serialNumberService.saveRelationship(pen);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "该序列号已被占用！");
        }
    }

}

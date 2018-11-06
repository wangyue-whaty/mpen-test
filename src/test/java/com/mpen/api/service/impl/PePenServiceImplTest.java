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

import com.mp.shared.common.Page;
import com.mpen.TestBase;
import com.mpen.api.bean.PenInfo;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.PePenMapper;
import com.mpen.api.service.PePenService;
import com.mpen.api.util.CommUtil;

/**
 * PePenServiceImplTest.
 * 
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PePenServiceImplTest extends TestBase {
    @InjectMocks
    @Autowired
    PePenService pePenService;
    @Autowired
    PePenMapper pePenMapper;

    @BeforeClass
    public static void setUp() throws Exception {
        MockitoAnnotations.initMocks(PePenServiceImplTest.class);
    }

    @Test
    public void testSavePenSuccess() throws SdkException {
        DdbPePen pen = getTestNoMachingDdbPePen();
        PenInfo penInfo = new PenInfo();
        penInfo.setCode(CommUtil.getPenCode(pen.getIdentifiaction()));
        penInfo.setMacAddress(pen.getMacAddress());
        Boolean success = pePenService.savePen(pen.getIdentifiaction(), penInfo);
        Assert.assertEquals(success, true);
        pePenService.deleteByIdentification(pen.getIdentifiaction());
    }

    @Test
    public void testSavePenWrongIdentifiactionError() throws SdkException {
        DdbPePen pen = getTestDdbPePen();
        PenInfo penInfo = new PenInfo();
        penInfo.setCode("a");
        penInfo.setMacAddress(pen.getMacAddress());
        try {
            pePenService.savePen(pen.getIdentifiaction(), penInfo);
        } catch (SdkException e) {
            Assert.assertEquals(Constants.WRONG_IDENTIFIACTION, e.getMessage());
        }
    }
    
    /**
     * 测试生成教师笔版本信息与获取教师笔版本信息
     * @throws SdkException
     */
    @Test
    public void testCreateTeacherPenVersionInfo() throws SdkException {
        // 先生成教师笔信息
        boolean flag = pePenService.createTeacherPenVersionInfo();
        Assert.assertEquals(flag, true);
        // 再获取教师笔信息
        Page<DdbPePen> dpage = pePenService.getTeacherPenVersionInfo();
        Assert.assertEquals(dpage != null, true);
        // 看读取到的信息是否大于0条
        Assert.assertEquals(dpage.getTotalCount() > 0, true);
    }
    
    /**
     * 测试通过绑定手机号获取笔信息
     * @throws SdkException
     */
    @Test
    public void testGetPenInfoByMobile() throws SdkException {
        // 测试两种情况,第一个手机号是有笔绑定信息,第二个没有
        final String[][] bindMobiles = { { "13475673466", "success" }, { "15566663333", "fail" } };
        for (String[] bindMobile : bindMobiles) {
            try {
            final DdbPePen ddbPePen = pePenService.getPenInfoByBindMobile(bindMobile[0]);
            if (bindMobile[1].equals("success")) {
                Assert.assertEquals(ddbPePen != null, true);
            }
            } catch (Exception exception) {
                if (bindMobile[1].equals("fail")) {
                    Assert.assertEquals(exception.getMessage(), Constants.NO_MACHING_PEN);
                }
            }
        }

    }
    
    @Test
    public void testGetPenInfoBySerialNumOrMac() throws SdkException {
        // 测试两种情况,第一个手机号是有笔绑定信息,第二个没有
        final String[] serivalNumOrMacs = { "47:3F:2E:93:C3:8A", "V917060007313" };
        // 测试两种情况,第一种测试根据mac地址获取,第二次测根据序列号获取
        DdbPePen ddbPePen = null;
        // 通过mac地址查询
        ddbPePen = pePenService.getPenInfoBySerialNumOrMac(null, serivalNumOrMacs[0]);
        Assert.assertEquals(ddbPePen != null, true);
        // 通过序列号查询
        ddbPePen = pePenService.getPenInfoBySerialNumOrMac(serivalNumOrMacs[1], null);
        Assert.assertEquals(ddbPePen != null, true);

    }

}

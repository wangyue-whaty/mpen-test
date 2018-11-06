/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import java.util.List;

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
import com.mpen.api.bean.RomUpdate;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Constants.PenType;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.domain.DdbRomVersion;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.RomVersionMapper;
import com.mpen.api.service.RomUpdateService;

/**
 * RomUpdateServiceImplTest.
 * 
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RomUpdateServiceImplTest extends TestBase {
    @InjectMocks
    @Autowired
    RomUpdateService romUpdateService;
    @Autowired
    RomVersionMapper romVersionMapper;

    @BeforeClass
    public static void setUp() throws Exception {
        MockitoAnnotations.initMocks(RomUpdateServiceImplTest.class);
    }

    @Test
    public void testGetUpdateMessageWithVersionSuccess() throws SdkException {
        DdbPePen pen = getTestDdbPePen();
        List<RomUpdate> list = romUpdateService.getUpdateMessage(pen.getIdentifiaction(),
            "Mpen-V2.0FHWV1.5-20160829.113548");
        Assert.assertEquals(list.size() > 0, true);
    }

    @Test
    public void testUpgradeAppWithoutVersionSuccess() throws SdkException {
        DdbPePen pen = getTestDdbPePen();
        List<RomUpdate> list = romUpdateService.getUpdateMessage(pen.getIdentifiaction(), null);
        // TODO 测试逻辑或者mapper层可能出错，待验证
        Assert.assertEquals(list.size(), 0);
    }

    @Test
    public void testUpgradeAppNoMachingPenError() throws SdkException {
        DdbPePen pen = getTestNoMachingDdbPePen();
        try {
            romUpdateService.getUpdateMessage(pen.getIdentifiaction(), null);
        } catch (Exception exception) {
            Assert.assertEquals(exception.getMessage(), Constants.NO_MACHING_PEN);
        }
    }
    
    /**
     * 测试获取linux笔rom更新
     * @throws SdkException
     */
    @Test
    public void testGetLinuxPenUpdateMessageSuccess() throws SdkException {
        final String[][] testPens = {
                {"d2843811--0c120806-00000000-802a288f","V4.2AC_P151M_D3-20171102.174211","V9"},// V9笔
                {"53022330--0e528805-00000000-926114af","V0.36_MPENLS_AB_20180614.163000","AB"},// LINUX AB
                {"3e4a4949--1b523807-00000000-8a751caf","V0.36_MPENLS_AC_20180614.163000","AC"} // LINUX AC
        };
        for (final String[] testPen: testPens) {
            // 查询出升级信息(只返回一条数据)
            final List<RomUpdate> list = romUpdateService.getUpdateMessage(testPen[0],testPen[1]);
            // 校验是否存在返回信息
            Assert.assertEquals(list.size() > 0, true);
            final RomUpdate romUpdate =list.get(0);
            String version1 = null;
            String version2 = null;
            if("AB".equals(testPen[2])) {
                // 根据type查询对应的最新版本信息
                final List<DdbRomVersion> ddbRomVersions = romVersionMapper.getVersionById(PenType.LINUX,"AB");
                // 查询出最新的数据
                version1 = ddbRomVersions.get(0).getName();
                Assert.assertEquals(romUpdate.getVersionTo().equals(version1), true);
            }else if("AC".equals(testPen[2])){
                final List<DdbRomVersion> ddbRomVersions = romVersionMapper.getVersionById(PenType.LINUX,"AC");
                version2 = ddbRomVersions.get(0).getName();
                Assert.assertEquals(romUpdate.getVersionTo().equals(version2), true);
            }else {
                // V9笔失败
                Assert.assertEquals(romUpdate.getVersionTo().equals(version1) || romUpdate.getVersionTo().equals(version2), false);
            }
           
        }
    }
}

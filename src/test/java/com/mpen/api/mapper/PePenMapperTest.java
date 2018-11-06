package com.mpen.api.mapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.domain.DdbPePen;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PePenMapperTest extends TestBase {

    @Autowired
    private PePenMapper pePenMapper;
    private String serialNum;
    private String macAddress;

    @Before
    public void init() {
        serialNum = getTestDdbPePen().getSerialNumber();
        macAddress = getTestDdbPePen().getMacAddress();
    }
    @Test
    public void testGetPenInfoBySerialNumOrMacAddress() {
        DdbPePen pen1 = this.pePenMapper.getPenInfoBySerialNumOrMacAddress(serialNum, null);
        DdbPePen pen2 = this.pePenMapper.getPenInfoBySerialNumOrMacAddress(null, macAddress);
        DdbPePen pen3 = this.pePenMapper.getPenInfoBySerialNumOrMacAddress(serialNum, macAddress);
        Assert.assertEquals(macAddress, pen1.getMacAddress());
        Assert.assertEquals(macAddress, pen2.getMacAddress());
        Assert.assertTrue(pen3 != null);
    }
}

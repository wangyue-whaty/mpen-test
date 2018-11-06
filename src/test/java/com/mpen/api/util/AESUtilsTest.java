package com.mpen.api.util;

import org.junit.Assert;
import org.junit.Test;

import com.mpen.api.common.SSOKey;

public class AESUtilsTest {

    @Test
    public void TestAES() {
        final String[] testDataList = { "" , "12j3h-jh" , "12j3h_jh" , "hfdf-fh_ff" , "1212", "323dsfdf", "大幅度发多少", "232sdfs对方水电费", "J+K/+>*N$/%\"$66F" };
        for (String testData : testDataList) {
            //测试AES加密
            final String ciphertext = AESUtils.encrypt(SSOKey.SESSIONID_KEY, testData);
            final String cleartext = AESUtils.decrypt(SSOKey.SESSIONID_KEY, ciphertext);
            Assert.assertTrue(testData.equals(cleartext));
            //测试解密错误的密文
            final String wrongCiphertext = ciphertext+"t";
            Assert.assertNull(AESUtils.decrypt(SSOKey.SESSIONID_KEY, wrongCiphertext));
            //测试AES加密saltData
            String saltData = AESUtils.getSaltData(testData);
            final String ciphertext1 = AESUtils.encrypt(SSOKey.SESSIONID_KEY, saltData);
            final String cleartext1 = AESUtils.decrypt(SSOKey.SESSIONID_KEY, ciphertext1);
            Assert.assertEquals(saltData,cleartext1);
            String value = AESUtils.getValue(saltData);
            Assert.assertEquals(testData, value);
            //测试解密错误的密文
            final String wrongCiphertext1 = ciphertext1+"t";
            Assert.assertNull(AESUtils.decrypt(SSOKey.SESSIONID_KEY, wrongCiphertext1));
        }
    }
    
    @Test
    public void TestSaltData() throws InterruptedException {
        final String value = "18888888888";
        String saltData = AESUtils.getSaltData(value);
        Assert.assertTrue(AESUtils.validateSaltData(saltData));
        String value2 = AESUtils.getValue(saltData);
        Assert.assertEquals(value, value2);
        String saltData2 = AESUtils.getSaltData(value);
        Assert.assertNotEquals(saltData, saltData2);
        Thread.sleep(121000);
        Assert.assertFalse(AESUtils.validateSaltData(saltData2));
    }

}

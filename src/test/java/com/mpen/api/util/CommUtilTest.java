package com.mpen.api.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mpen.api.common.Constants;

public class CommUtilTest {
    String[] userAgents;
    String[] expectResult;

    @Before
    public void prepareData() {
        userAgents = new String[4];
        userAgents[0] = creatUserAgent(0);
        userAgents[1] = creatUserAgent(60);
        userAgents[2] = creatUserAgent(4000);
        userAgents[3] = new Random().nextInt(9999) + String.valueOf(System.currentTimeMillis() / 1000)
            + new Random().nextInt(9999);
        expectResult = new String[4];
        expectResult[0] = Constants.SUCCESS;
        expectResult[1] = Constants.SUCCESS;
        expectResult[2] = Constants.TIME_ERROR;
        expectResult[3] = Constants.WRONG_USERAGENT;
    }

    @Test
    public void testCheckUserAgent() throws Exception {
        for (int i = 0; i < 4; i++) {
            final String result = CommUtil.checkUserAgent(userAgents[i]);
            Assert.assertEquals(result, expectResult[i]);
        }
    }

    private String creatUserAgent(int time) {
        final String randomStr = String.format("%04d", new Random().nextInt(9999));
        final String timeStr = String.valueOf(System.currentTimeMillis() / 1000 + time);
        final String md5Str = DigestUtils.md5Hex(randomStr + timeStr);
        final int len = md5Str.length();
        final String md52IntResultStr = String.valueOf(Integer.valueOf(md5Str.substring(len - 4, len), 36));
        final String secret = md52IntResultStr.length() > 4 ? md52IntResultStr.substring(0, 4)
            : String.format("%04d", Integer.valueOf(md52IntResultStr));
        return randomStr + timeStr + secret;
    }
    
    /**
     * 测试日期时间差
     */
    @Test
    public void testDiffInSeconds() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +10);
        date = calendar.getTime();
        // 传入日期小于当前日期,返回值大于0
        long positiveNumber = CommUtil.diffInSeconds(new Date(1538314766000L));
        Assert.assertEquals(positiveNumber > 0, true);
        // 传入日期大于当前日期,返回值小于0
        long negativeNumber = CommUtil.diffInSeconds(date);
        Assert.assertEquals(negativeNumber < 0, true);
    }

}

package com.mpen.api.util;

import org.junit.Assert;
import org.junit.Test;

public class FileUtilsTest {
    final String url = "/incoming/course/testCdn/1~2!3/@45$6/7%5E8&0(a)/b_cd%7Be%7D/fgh%60i-j/=lmn'op/q,r.stv/w；x：y｛z｝/1［2］3｜/4、56！7/a.txt";

    @Test
    public void testGetCdnUrlWithNetwork() throws Exception {
        final String expectUrl = "/incoming/course/testCdn/1~2!3/@45$6/7%5E8&0(a)/b_cd%7Be%7D/fgh%60i-j/=lmn'op/q,r.stv/w%EF%BC%9Bx%EF%BC%9Ay%EF%BD%9Bz%EF%BD%9D/1%EF%BC%BB2%EF%BC%BD3%EF%BD%9C/4%E3%80%8156%EF%BC%817/a.txt";
        String key = FileUtils.urlEncode(url);
        Assert.assertEquals(expectUrl, key);
        String cdnUrl = "http://cdn.mpen.com.cn" + FileUtils.getCdnUrl(url, System.currentTimeMillis() / 1000 + 604800);
        String result = CommUtil.get(cdnUrl);
        Assert.assertEquals("1", result);
    }

    @Test
    public void testGetCdnUrlWithoutNetwork() throws Exception {
        final long time = 1495847822;
        final String expectUrl = "http://cdn.mpen.com.cn/incoming/course/testCdn/1~2!3/@45$6/7%5E8&0(a)/b_cd%7Be%7D/fgh%60i-j/=lmn'op/q,r.stv/w；x：y｛z｝/1［2］3｜/4、56！7/a.txt?auth_key=1495847822-0-0-37e34b67cf1ff5dac8c7cc362fe51872";
        String cdnUrl = "http://cdn.mpen.com.cn" + FileUtils.getCdnUrl(url, time);
        Assert.assertEquals(expectUrl, cdnUrl);
    }

}

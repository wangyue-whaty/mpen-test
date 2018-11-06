package com.mpen.api.util;

import java.io.IOException;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * 微信相关util测试类
 * @author wangyue
 *
 */
public class JsSignUtilTest {

    /**
     * 校验生成signature方法是否成功 
     * 
     * @throws IOException
     */
    @Test
    public void testJsSignUtil() throws IOException {
        final String[][] params = {{"http://test.123.com","c9b6c062-9dd9-4746-af26-5d27dc068c49","kgt8ON7yVITDhtdwci0qeWjkNj-K10fBLxpJOEn7Gj7qJ5z6yy4zq7hlAQTM_l4KU82aeVqFEqiS3w2M2_zUSA"},{null,null,null}};
        for (int index = 0;index <params.length;index++) {
            Map<String, String> signature = JsSignUtil.sign(params[index][0], params[index][1], params[index][2]);
            // 第一次:url,accesToken,jsapi_ticket都传值,所以会得到相应的返回信息
            if (index == 0) {
                Assert.assertEquals(signature.get("jsapi_ticket")!= null, true);
                Assert.assertEquals(signature.get("url")!= null, true);
                Assert.assertEquals(signature.get("nonceStr")!= null, true);
                Assert.assertEquals(signature.get("timestamp")!= null, true);
                Assert.assertEquals(signature.get("signature")!= null, true);
            } else {
                // 第二次:url,accesToken,jsapi_ticket都不传值,所以不会得到相应的返回信息
                Assert.assertEquals(signature.get("jsapi_ticket")== null, true);
                Assert.assertEquals(signature.get("url")== null, true);
                Assert.assertEquals(signature.get("nonceStr")!= null, true);
                Assert.assertEquals(signature.get("timestamp")!= null, true);
                Assert.assertEquals(signature.get("signature")!= null, true);
            }
        }
    }
}

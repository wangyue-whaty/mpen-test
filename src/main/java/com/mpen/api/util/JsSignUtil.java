package com.mpen.api.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator;
import com.mpen.api.common.Constants;
import com.mpen.api.service.impl.ProgramServiceImpl;

/**
 * 微信JSSDK加密signature工具类
 * 
 * @author wangyue
 *
 */
public class JsSignUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsSignUtil.class);

    /**
     * 获取微信公众号调取扫一扫功能时需要的signature签名. 
     * 签名生成规则如下：
     *      参与签名的字段包括noncestr（随机字符串）,
     *      有效的jsapi_ticket, timestamp（时间戳）, url（当前网页的URL，不包含#及其后面部分）。
     *      对所有待签名参数按照字段名的ASCII码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）
     *      拼接成字符串string1, 对string1进行sha1签名，得到signature：例如:0f9de62fce790f9a083d5c99e95740ceb90c27ed
     */
    public static Map<String, String> sign(String url, String accesToken, String jsapiTicket) throws IOException {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String signature = "";
        // 注意这里参数名必须全部小写，且必须有序
        final String string1 = String.format("jsapi_ticket=%s&noncestr=%s&timestamp=%s&url=%s", jsapiTicket, nonce_str,
                timestamp, url);
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        ret.put("url", url);
        ret.put("jsapi_ticket", jsapiTicket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        ret.put("appId", Constants.WECHAT_PUB_ID);
        return ret;
    }

    /**
     * 随机加密
     * 
     * @param hash
     * @return
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    /**
     * 产生随机串--由程序自己随机产生
     * 
     * @return
     */
    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    /**
     * 由程序自己获取当前时间
     * 
     * @return
     */
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

}

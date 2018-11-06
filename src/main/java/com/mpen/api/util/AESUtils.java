package com.mpen.api.util;


import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;

import com.mp.shared.utils.FUtils;

/**
 * AES加解密模块
 * TODO AESUtils,Base64公共加密模块提取为公共jar包放入现有maven私服;rest,mpsso统一通过jar包依赖调用
 */
public class AESUtils {

    private static final String HEX = "0123456789ABCDEF";

    //AES是加密方式 CBC是工作模式 PKCS5Padding是填充模式
    private  static final String CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";

    //AES 加密
    private  static final String AES = "AES";

    // SHA1PRNG 强随机种子算法, 要区别4.2以上版本的调用方法
    private  static final String  SHA1PRNG="SHA1PRNG";
    
    /**
     * 二进制转字符
     * @param buf
     * @return 16进制字符
     */
    public static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        final StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++) {
            appendHex(result, buf[i]);
        }
        return result.toString();
    }

    /**
     * 16进制添加
     * @param sb
     * @param b
     */
    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

    /**
     * 生成随机数，可以当做动态的密钥 加密和解密的密钥必须一致，不然将不能解密
     * @return 随机数
     */
    public static String generateKey() {
        try {
            SecureRandom localSecureRandom = SecureRandom.getInstance(SHA1PRNG);
            byte[] bytes_key = new byte[20];
            localSecureRandom.nextBytes(bytes_key);
            String str_key = toHex(bytes_key);
            return str_key;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对密钥进行处理
     * @param seed
     * @return
     * @throws Exception
     */
    private static byte[] getRawKey(byte[] seed) throws Exception {
        while(seed.length != 16) {
            if(seed.length < 16) {
                byte[] bt3 = new byte[seed.length+1];
                System.arraycopy(seed, 0, bt3, 0, seed.length);
                byte[] bs = Arrays.copyOfRange(seed, 0, 1);
                System.arraycopy(bs, 0, bt3, seed.length, bs.length);
                seed = bt3;
            }
            if(seed.length > 16) {
                seed = Arrays.copyOfRange(seed, 0, 16);
            }
        }
        return seed;
    }


    /**
     * 加密
     * @param key
     * @param cleartext
     * @return 加密后的数据
     */
    public static String encrypt(String key, String cleartext) {
        if (StringUtils.isEmpty(cleartext)) {
            return cleartext;
        }
        try {
            byte[] result = encrypt(key, cleartext.getBytes());
            return Base64.encode(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密
     * @param key
     * @param clear
     * @return 加密后的数据
     * @throws Exception
     */
    private static byte[] encrypt(String key, byte[] clear) throws Exception {
        final byte[] raw = getRawKey(key.getBytes());
        final SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        final Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        final byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    /**
     * 解密
     * @param key
     * @param ciphertext
     * @return 解密后的数据
     * @throws Exception
     */
    public static String decrypt(String key, String ciphertext) {
        if (StringUtils.isEmpty(ciphertext)) {
            return ciphertext;
        }
        try {
            byte[] enc = Base64.decode(ciphertext);
            byte[] result = decrypt(key, enc);
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     * @param key
     * @param cipherData
     * @return 解密后的数据
     * @throws Exception
     */
    private static byte[] decrypt(String key, byte[] cipherData) throws Exception {
        final byte[] raw = getRawKey(key.getBytes());
        final SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        final Cipher cipher = Cipher.getInstance(CBC_PKCS5_PADDING);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(new byte[cipher.getBlockSize()]));
        final byte[] decrypted = cipher.doFinal(cipherData);
        return decrypted;
    }
    
    /**
     * 获取加盐数据
     * @param value
     * @return
     */
    public static String getSaltData(String value) {
        //获取四位随机数
        final String randomStr = String.format("%04d", new Random().nextInt(9999));
        //获取当前时间戳
        final String timeStr = String.valueOf(System.currentTimeMillis() / 1000);
        //根据随机数+当前时间戳+value获取验证码
        final String secret = getSecret(randomStr, timeStr, value);
        return String.format("%s_%s_%s-%s", randomStr, timeStr, secret, value);
    }
    
    /**
     * 验证码及时间戳验证
     * @param saltData
     * @return
     */
    public static boolean validateSaltData(String saltData) {
        if(!saltData.contains("_") || !saltData.contains("-")) {
            return false;
        }
        //获取盐
        String salt = saltData.substring(0, saltData.indexOf("-"));
        //获取实际值
        String value = getValue(saltData);
        final String[] split = salt.split("_");
        if(split.length < 3) {
            return false;
        }
        //获取随机数、当前时间戳、实际值
        final String randomStr = split[0];
        final String timeStr = split[1];
        final String saltDataSecret = split[2];
        //验证时间是否合理
        long parseLong = Long.parseLong(timeStr);
        long time = System.currentTimeMillis()/1000;
        //验证请求时间是否在120秒内,否则返回false;
        long abs = Math.abs(time-parseLong);
        if(abs > 120) {
            return false;
        }
        //获取验证码
        final String secret = getSecret(randomStr, timeStr, value);
        return secret.equals(saltDataSecret);
    }
    
    /**
     * 获取实际值
     * @param saltData
     * @return
     */
    public static String getValue(String saltData) {
        return saltData.substring(saltData.indexOf("-")+1);
    }
    
    /**
     * 获取验证码
     * @param randomStr
     * @param timeStr
     * @param value
     * @return
     */
    private static String getSecret(String randomStr,String timeStr,String value) {
        //根据随机数+当前时间戳+value获取验证码
        final String md5Str = FUtils.MD5(randomStr + timeStr + value);
        final int len = md5Str.length();
        final String md52IntResultStr = String.valueOf(Integer.valueOf(md5Str.substring(len - 4, len), 36));
        final String secret = md52IntResultStr.length() > 4 ?md52IntResultStr.substring(0, 4) : String.format("%04d", Integer.valueOf(md52IntResultStr));
        return secret;
    }
}

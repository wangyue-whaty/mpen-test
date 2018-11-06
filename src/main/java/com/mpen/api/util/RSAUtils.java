package com.mpen.api.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by wutingyou on 2017/2/24.
 */
public final class RSAUtils {
    /**
     * 随机生成密钥对.
     *
     * @param filePath
     *            保存秘钥文件路径
     */
    public static void genKeyPair(String filePath) {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException exception) {
            // TODO Auto-generated catch block
            exception.printStackTrace();
        }
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        try {
            // 得到公钥字符串
            String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());
            // 得到私钥字符串
            String privateKeyString = Base64.encodeBase64String(privateKey.getEncoded());
            // 将密钥对写入到文件
            FileWriter pubfw = new FileWriter(filePath + "/publicKey.keystore");
            FileWriter prifw = new FileWriter(filePath + "/privateKey.keystore");
            BufferedWriter pubbw = new BufferedWriter(pubfw);
            BufferedWriter pribw = new BufferedWriter(prifw);
            pubbw.write(publicKeyString);
            pribw.write(privateKeyString);
            pubbw.flush();
            pubbw.close();
            pubfw.close();
            pribw.flush();
            pribw.close();
            prifw.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * 随机生成密钥对 同时返回秘钥对数组.
     *
     * @param filePath
     *            保存秘钥文件路径
     * @return 秘钥对字符数组 0 公钥， 1 私钥
     */

    public static String[] getKeyPairStringArray(String filePath) {
        String[] pairKeyArray = new String[2];
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException exception) {
            // TODO Auto-generated catch block
            exception.printStackTrace();
        }
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        // 得到私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        // 得到公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        try {
            // 得到公钥字符串
            String publicKeyString = Base64.encodeBase64String(publicKey.getEncoded());
            // 得到私钥字符串
            String privateKeyString = Base64.encodeBase64String(privateKey.getEncoded());
            pairKeyArray[0] = publicKeyString;
            pairKeyArray[1] = privateKeyString;
            // 将密钥对写入到文件
            FileWriter pubfw = new FileWriter(filePath + "/publicKey.keystore");
            FileWriter prifw = new FileWriter(filePath + "/privateKey.keystore");
            BufferedWriter pubbw = new BufferedWriter(pubfw);
            BufferedWriter pribw = new BufferedWriter(prifw);
            pubbw.write(publicKeyString);
            pribw.write(privateKeyString);
            pubbw.flush();
            pubbw.close();
            pubfw.close();
            pribw.flush();
            pribw.close();
            prifw.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return pairKeyArray;
    }

    /**
     * 从文件中输入流中加载公钥.
     *
     */
    public static String loadPublicKeyByFile(String path) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "/publicKey.keystore"));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException exception) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException exception) {
            throw new Exception("公钥输入流为空");
        }
    }

    /**
     * 从字符串中加载公钥.
     *
     * @param publicKeyStr
     *            公钥数据字符串
     * @throws Exception
     *             加载公钥时产生的异常
     */
    public static RSAPublicKey loadPublicKeyByStr(String publicKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decodeBase64(publicKeyStr);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException exception) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException exception) {
            throw new Exception("公钥非法");
        } catch (NullPointerException exception) {
            throw new Exception("公钥数据为空");
        }
    }

    /**
     * 从文件中加载私钥.
     *
     * @param path
     *            私钥文件名
     * @return 是否成功
     */
    public static String loadPrivateKeyByFile(String path) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "/privateKey.keystore"));
            String readLine = null;
            StringBuilder sb = new StringBuilder();
            while ((readLine = br.readLine()) != null) {
                sb.append(readLine);
            }
            br.close();
            return sb.toString();
        } catch (IOException exception) {
            throw new Exception("私钥数据读取错误");
        } catch (NullPointerException exception) {
            throw new Exception("私钥输入流为空");
        }
    }

    /**
     * 从字符串中加载私钥.
     *
     * @param privateKeyStr
     *            私钥串
     */
    public static RSAPrivateKey loadPrivateKeyByStr(String privateKeyStr) throws Exception {
        try {
            byte[] buffer = Base64.decodeBase64(privateKeyStr);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException exception) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException exception) {
            throw new Exception("私钥非法");
        } catch (NullPointerException exception) {
            throw new Exception("私钥数据为空");
        }
    }

    /**
     * 公钥加密过程.
     *
     * @param publicKey
     *            公钥
     * @param plainTextData
     *            明文数据
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static byte[] encrypt(RSAPublicKey publicKey, byte[] plainTextData) throws Exception {
        if (publicKey == null) {
            throw new Exception("加密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException exception) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException exception) {
            exception.printStackTrace();
            return null;
        } catch (InvalidKeyException exception) {
            throw new Exception("加密公钥非法,请检查");
        } catch (IllegalBlockSizeException exception) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException exception) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 私钥加密过程.
     *
     * @param privateKey
     *            私钥
     * @param plainTextData
     *            明文数据
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static byte[] encrypt(RSAPrivateKey privateKey, byte[] plainTextData) throws Exception {
        if (privateKey == null) {
            throw new Exception("加密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(plainTextData);
            return output;
        } catch (NoSuchAlgorithmException exception) {
            throw new Exception("无此加密算法");
        } catch (NoSuchPaddingException exception) {
            exception.printStackTrace();
            return null;
        } catch (InvalidKeyException exception) {
            throw new Exception("加密私钥非法,请检查");
        } catch (IllegalBlockSizeException exception) {
            throw new Exception("明文长度非法");
        } catch (BadPaddingException exception) {
            throw new Exception("明文数据已损坏");
        }
    }

    /**
     * 公钥加密.
     *
     * @param publicKeyStr
     *            公钥串
     * @param textData
     *            加密明文
     * @return 加密密文
     */
    public static String encrypt(String publicKeyStr, String textData) throws Exception {
        RSAPublicKey publicKey = loadPublicKeyByStr(publicKeyStr);
        byte[] cipherData = encrypt(publicKey, textData.getBytes());
        String str = Base64.encodeBase64String(cipherData);
        return str;
    }

    /**
     * 私钥解密过程.
     *
     * @param privateKey
     *            私钥
     * @param cipherData
     *            密文数据
     * @return 明文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
        if (privateKey == null) {
            throw new Exception("解密私钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException exception) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException exception) {
            exception.printStackTrace();
            return null;
        } catch (InvalidKeyException exception) {
            throw new Exception("解密私钥非法,请检查");
        } catch (IllegalBlockSizeException exception) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException exception) {
            throw new Exception("密文数据已损坏");
        }
    }

    /**
     * 公钥解密过程.
     *
     * @param publicKey
     *            公钥
     * @param cipherData
     *            密文数据
     * @return 明文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static byte[] decrypt(RSAPublicKey publicKey, byte[] cipherData) throws Exception {
        if (publicKey == null) {
            throw new Exception("解密公钥为空, 请设置");
        }
        Cipher cipher = null;
        try {
            // 使用默认RSA
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] output = cipher.doFinal(cipherData);
            return output;
        } catch (NoSuchAlgorithmException exception) {
            throw new Exception("无此解密算法");
        } catch (NoSuchPaddingException exception) {
            exception.printStackTrace();
            return null;
        } catch (InvalidKeyException exception) {
            throw new Exception("解密公钥非法,请检查");
        } catch (IllegalBlockSizeException exception) {
            throw new Exception("密文长度非法");
        } catch (BadPaddingException exception) {
            throw new Exception("密文数据已损坏");
        }
    }

    /**
     * 私钥解密.
     *
     * @param privateKeyStr
     *            私钥串
     * @param textData
     *            解密密文
     * @return 解密明文
     */
    public static String decrypt(String privateKeyStr, String textData) throws Exception {
        RSAPrivateKey privateKey = loadPrivateKeyByStr(privateKeyStr);
        byte[] res = decrypt(privateKey, Base64.decodeBase64(textData));
        String str = new String(res);
        return str;
    }
}

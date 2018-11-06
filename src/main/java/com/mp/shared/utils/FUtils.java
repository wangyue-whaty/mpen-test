package com.mp.shared.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Scanner;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mp.shared.common.Utils;

/**
 * Created by feng on 4/15/17.
 *
 * FileUtils shared
 */

public final class FUtils {

    /**
     * 字符串写入文件
     * TODO copied from com.mp.pen.module.FileUtils to avoid changing too many files. need to do a clean up later
     *
     * @param filePath 文件路径
     * @param content  写入内容
     * @param append   是否是追加的方式
     */
    public static boolean stringToFile(String filePath, String content, boolean append) {
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(filePath, append);
            bw = new BufferedWriter(fw);
            bw.write(content);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * get file size if the filename is a valid file
     */
    public static long getFileSize(String fileName) {
        if (Utils.isEmpty(fileName)) {
            return -1;
        }
        final File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            return file.length();
        }
        return -1;
    }

    /**
     * 从文本文件中读取内容
     * TODO copied from com.mp.pen.module.FileUtils to avoid changing too many files. need to do a clean up later
     *
     * @param filePath 要读取的文件
     */
    public static String fileToString(String filePath) {
        final File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return inputToString(is);
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            try {
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 从文本文件中读取内容
     * TODO copied from com.mp.pen.module.FileUtils to avoid changing too many files. need to do a clean up later
     *
     * @param filePath 要读取的文件
     */
    public static String fileToString(File file) {
        if (!file.exists()) {
            return null;
        }
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return inputToString(is);
        } catch (FileNotFoundException e) {
            return null;
        } finally {
            try {
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取InputStream内容到一个String。
     * TODO copied from com.mp.pen.module.FileUtils to avoid changing too many files. need to do a clean up later
     *
     * @param is
     * @return 读取的String
     */
    public static String inputToString(InputStream is) {
        try {
            // 空文件会造成 Scanner异常，所以这里先检查
            if (is.available() == 0) {
                return "";
            }
        } catch (IOException e) {
            return "";
        }
        final Scanner scanner = new Scanner(is, "UTF-8");
        final String str = scanner.useDelimiter("\\A").next();
        scanner.close();
        return str;
    }
    
    /**
     * TODO 使用第三方jar获取文件MD5值  
     */
    public static String getMD5ByFile(File file){
        FileInputStream input = null;
        try {
            input = new FileInputStream(file);
            return new String(Hex.encodeHex(DigestUtils.md5(input)));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(input != null){
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * 读取InputStream内容到一个File。
     * TODO 消除重复定义
     *
     * @param is
     * @return 读取的String
     */
    public static void inputToFile(InputStream is, String targetFile) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);
            byte[] b = new byte[1024];
            int tem = 0;
            while ((tem = is.read(b)) != -1) {
                fos.write(b, 0, tem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * 返回md5
     * TODO:需要与DeviceUtils里的getSHA1Code，getMD5Code 合并，暂时不改，避免MD5出错与服务器的不一致
     * @param s
     * @return
     */
    public static String MD5(String s) {
        final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            final byte[] strTemp = s.getBytes();
            final MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            final byte[] md = mdTemp.digest();
            final int j = md.length;
            final char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte b = md[i];
                str[k++] = hexDigits[b >> 4 & 0xf];
                str[k++] = hexDigits[b & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * 得到上传文件的md5值
     * @param file
     * @return
     */
    public static String getMd5(MultipartFile file) {  
        try {  
            byte[] uploadBytes = file.getBytes();  
            MessageDigest md5 = MessageDigest.getInstance("MD5");  
            byte[] digest = md5.digest(uploadBytes);  
            String hashString = new BigInteger(1, digest).toString(16);  
            return hashString;  
        } catch (Exception e) {  
            e.printStackTrace();
        }  
        return null;  
    }  
}

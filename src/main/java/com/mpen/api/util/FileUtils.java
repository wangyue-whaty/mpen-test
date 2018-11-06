/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.springframework.web.util.UriUtils;

import com.mp.shared.common.BookInfo;
import com.mpen.api.common.Constants;
import com.mpen.api.exception.SdkException;

/**
 * 文件工具类.
 * 
 * @author zyt
 */
public final class FileUtils {
    // 文件存放的根目录.
    public static final String ROOTPATH = "/incoming/ddb/";
    // 书资源存放根目录
    public static final String SOURCE_PATH = "/incoming/course/";
    // 项目部署绝对路径.
    public static String root;
    // 项目访问ip地址（域名）
    public static String domain;
    // CDN访问域名
    public static String cdnDomain;
    // 本地Ip
    public static String localIp = "127.0.0.1";
    // 服务num
    public static int shardNum = -1;
    public static int numOralEvalShards;
    // 教师端教师布置作业资源文件
    public static final String TEACHER_FILES = "teacherFiles";
    // app2.0学生资源文件
    public static final String STUDENT_FILES = "studentFiles";

    /**
     * 文档类型，用于创建文件储存目录，对文件进行分类.
     */
    public static final String DEFAULT = "default";// 文件类型（其它 ）
    public static final String CMD_FILE = "cmdFile";// 文件类型(开放式图片)
    public static final String BOOK_TIF = "course/tif";// 文件类型（tif）
    public static final String BOOK_JPG = "course/jpg";
    public static final String BOOK_ZIP = "course/zip";
    public static final String BOOK = "book";
    public static final String FILE_PART = "filePart";
    public static final String RECORDING = "recording";
    public static final String COVER_FILES = "coverFiles";
    
    public static final String BOOK_ZIP_FOLDER = "/incoming/course/zip/";
    /**
     * 根据文件获取后缀名.
     * 
     */
    public static String getExtension(File file) {
        return (file != null) ? getExtension(file.getName()) : "";
    }

    /**
     * 根据文件名获取后缀名.
     * 
     */
    public static String getExtension(String filename) {
        return getExtension(filename, "");
    }

    /**
     * 根据文件名获取后缀名（如果后缀名为空，返回默认后缀名）.
     * 
     */
    public static String getExtension(String filename, String defExt) {
        if ((filename != null) && (filename.length() > 0)) {
            int temp = filename.lastIndexOf('.');

            if ((temp > -1) && (temp < (filename.length() - 1))) {
                return filename.substring(temp + 1);
            }
        }
        return defExt;
    }

    /**
     * 根据文件名获取后缀名，带".".
     * 
     */
    public static String trimExtension(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int temp = filename.lastIndexOf('.');
            if ((temp > -1) && (temp < (filename.length()))) {
                return filename.substring(temp);
            }
        }
        return filename;
    }

    // public

    /**
     * 上传文件.
     * 
     */
    public static String uploadFile(File sourceFile, String sourceFileName) throws Exception {
        return uploadFile(sourceFile, sourceFileName, null, null);
    }

    /**
     * 上传文件.
     * 
     */
    public static String uploadFile(File sourceFile, String sourceFileName, String fileType) throws Exception {
        return uploadFile(sourceFile, sourceFileName, null, fileType);
    }

    /**
     * 上传文件.
     * 
     */
    public static String uploadFile(File sourceFile, String sourceFileName, String rootPath, String fileType)
        throws Exception {
        // 得到文件存放的相对目录
        final String fileSavePath = getFileSavePath(rootPath, fileType);
        // 得到文件存放的绝对目录
        final String fileSaveRealPath = getFileSaveRealPath(fileSavePath);
        // 得到文件newName
        final String fileNewName = ceateFileNewName(sourceFileName);
        if (!copyFile(sourceFile, fileSaveRealPath + fileNewName)) {
            return "";
        }
        return fileSavePath + fileNewName;
    }

    /**
     * 上传文件.
     * 
     */
    public static String uploadFile(byte[] sourceByte, String sourceFileName, String rootPath, String fileType)
        throws Exception {
        // 上传资源文件(出版系统上传书的资源文件)
        if (null == fileType) {
            // 得到文件存放的绝对目录
            final String fileSaveRealPath = getFileSaveRealPath(rootPath);
            if (!copyFile(sourceByte, fileSaveRealPath + sourceFileName)) {
                return "";
            }
            return rootPath + sourceFileName;
        }
        // 得到文件存放的相对目录
        final String fileSavePath = getFileSavePath(rootPath, fileType);
        // 得到文件存放的绝对目录
        final String fileSaveRealPath = getFileSaveRealPath(fileSavePath);
        // 得到文件newName
        final String fileNewName = ceateFileNewName(sourceFileName);
        if (!copyFile(sourceByte, fileSaveRealPath + fileNewName)) {
            return "";
        }
        return fileSavePath + fileNewName;
    }
    
    /**
     * 上传文件.
     * 
     */
    public static String uploadFileWithOldName(File sourceFile, String sourceFileName, String fileType)
        throws Exception {
        // 得到文件存放的相对目录
        final String fileSavePath = getFileSavePath(null, fileType);
        // 得到文件存放的绝对目录
        final String fileSaveRealPath = getFileSaveRealPath(fileSavePath);
        if (!copyFile(sourceFile, fileSaveRealPath + sourceFileName)) {
            return "";
        }
        return fileSavePath + sourceFileName;
    }

    /**
     * 复制文件.
     * 
     */
    public static boolean copyFile(File sourceFile, String targetPath) throws IOException {
        // 判断目标文件是否存在
        final File targetFile = new File(targetPath);
        if (!targetFile.getParentFile().exists()) {
            // 如果目标文件所在的目录不存在，则创建目录
            if (!targetFile.getParentFile().mkdirs()) {
                return false;
            }
        }
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            final byte[] temp = new byte[1024];
            int len;
            while ((len = inBuff.read(temp)) != -1) {
                outBuff.write(temp, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
        } finally {
            // 关闭流
            if (inBuff != null) {
                inBuff.close();
            }
            if (outBuff != null) {
                outBuff.close();
            }
        }
        return true;
    }

    /**
     * 复制文件.
     * 
     */
    public static boolean copyFile(byte[] bytes, String targetPath) throws IOException {
        // 判断目标文件是否存在
        final File targetFile = new File(targetPath);
        if (!targetFile.getParentFile().exists()) {
            // 如果目标文件所在的目录不存在，则创建目录
            if (!targetFile.getParentFile().mkdirs()) {
                return false;
            }
        }
        // BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            // inBuff = new BufferedInputStream(new
            // FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            /*
             * // 缓冲数组 byte[] b = new byte[1024]; int len; while ((len =
             * inBuff.read(b)) != -1) { outBuff.write(b, 0, len); } // 刷新此缓冲的输出流
             * outBuff.flush();
             */
            outBuff.write(bytes, 0, bytes.length);
        } finally {
            // 关闭流
            if (outBuff != null) {
                outBuff.close();
            }
        }
        return true;
    }

    /**
     * 得到照片存放路径（绝对路径），并创建目录.
     * 
     */
    public static String getFileSaveRealPath(String path) throws Exception {
        return getFileSaveRealPath(path, true);
    }

    /**
     * 得到照片存放路径（绝对路径），并创建目录.
     * 
     */
    public static String getFileSaveRealPath(String path, boolean createDir) throws Exception {
        path = path.replace(cdnDomain, "").replace(domain, "");
        final int index = path.indexOf("?");
        if (index > 0) {
            path = path.substring(0, index);
        }
        String realPath = "";
        if (StringUtils.isBlank(path)) {
            realPath = getFileSavePath();
        } else {
            realPath = path;
        }
        realPath = realPath.startsWith("/") ? realPath.substring(1) : realPath;
        realPath = root + realPath;
        if (createDir) {
            File dir = new File(realPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return realPath;
    }

    /**
     * 得到照片存放路径（相对路径） 路径：ROOTPATH + default +
     * dateStr(如/incoming/localhost/default/20140402/).
     * 
     */
    public static String getFileSavePath() {

        return getFileSavePath(null, null);
    }

    /**
     * 得到照片存放路径（相对路径） 路径：ROOTPATH + default +
     * dateStr(如/incoming/localhost/default/20140402/).
     * 
     */
    public static String getFileSavePath(String fileType) {

        return getFileSavePath(null, fileType);
    }

    /**
     * 得到照片存放路径（相对路径） 路径：ROOTPATH + fileType +
     * dateStr(如/incoming/localhost/image/20140402/).
     * 
     */
    public static String getFileSavePath(String rootPath, String fileType) {
        return getFileSavePath(rootPath, fileType, null);
    }

    /**
     * 得到照片存放路径（相对路径） 路径：ROOTPATH + fileType +
     * dateStr(如/incoming/localhost/image/20140402/).
     * 
     */
    public static String getFileSavePath(String rootPath, String fileType, String host) {
        String path = "";
        // 文件存放的根目录
        if (StringUtils.isNotBlank(rootPath)) {
            path = rootPath;
        } else {
            path = ROOTPATH;
        }

        // 用于文件分类为文件夹名
        String fileTypePath = "";
        if (StringUtils.isNotBlank(fileType)) {
            fileTypePath = fileType;
        } else {
            fileTypePath = DEFAULT;
        }
        path += fileTypePath + "/";

        // 时间，格式：yyyyMMdd
        final String date = getYmd();
        path += date + "/";
        return path;
    }

    /**
     * 得到时间串，格式：yyyyMMdd.
     * 
     */
    public static String getYmd() {
        final Calendar calendar = Calendar.getInstance();
        final Date date = calendar.getTime();
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * 创建文件名，格式：当前时间毫秒数 + '-' + 1位随机数.
     * 
     */
    public static String ceateFileNewName() {

        return ceateFileNewName(null, null);
    }

    /**
     * 创建文件名，格式：当前时间毫秒数 + '-' + 1位随机数 + 原文件后缀名.
     * 
     */
    public static String ceateFileNewName(String oldName) {

        return ceateFileNewName(oldName, null);
    }

    /**
     * 创建文件名，格式：当前时间毫秒数 + '-' + 1位随机数 + '-' + suffixName + 原文件后缀名.
     * 
     */
    public static String ceateFileNewName(String oldName, String suffixName) {
        String newName = "";
        newName += new Date().getTime() + "";
        final Random rnd = new Random();
        newName += "-" + rnd.nextInt(10);// 获取随机数
        if (StringUtils.isNotBlank(suffixName)) {
            newName += "-" + suffixName;

        }
        if (StringUtils.isNotBlank(oldName)) {
            String extensionName = getExtension(oldName);
            newName += "." + extensionName;
        }

        return newName;
    }
    
    /**
     * 判断文件是否存在.
     * 
     */
    public static boolean checkFileExists(String path) throws Exception {
        final File swfFile = new File(getFileSaveRealPath(path, false));
        if (!swfFile.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 删除文件.
     * 
     */
    public static boolean rmFile(String filePath, boolean isDirectory) {
        final File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory() && isDirectory) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    rmFile(files[i].getPath(), isDirectory);
                }
            }
            return file.delete();
        }
        return false;
    }

    /**
     * 删除指定目录下指定扩展名的文件.
     * 
     */
    public static boolean rmFile(String directory, String extensionName) {
        final File file = new File(directory);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile() && getExtension(files[i]).equals(extensionName)) {
                    files[i].delete();
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 复制目录.
     * 
     */
    public static boolean copyDirectiory(String sourceDir, String targetDir) throws IOException {
        boolean copyFinished = false;
        // 判断目标文件是否存在
        File targetRoot = new File(targetDir);
        if (!targetRoot.exists()) {
            // 如果目标文件所在的目录不存在，则创建目录
            if (!targetRoot.mkdirs()) {
                return false;
            }
            targetRoot = new File(targetDir);
        }

        File sourceRoot = new File(sourceDir);
        String targetPath = targetRoot.getAbsolutePath();
        String sourcePath = sourceRoot.getAbsolutePath();
        // 获取源文件夹当前下的文件或目录
        File[] files = sourceRoot.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {

                // 复制文件
                copyFinished = copyFile(files[i], targetPath + File.separator + files[i].getName());
            }
            if (files[i].isDirectory()) {
                // 复制目标文件夹
                copyFinished = copyDirectiory(sourcePath + File.separator + files[i].getName(),
                    targetPath + File.separator + files[i].getName());
            }
        }
        return copyFinished;
    }

    /**
     * 获得资源相关路径（相对路径） 路径：incoming/local/header/20151202/1449022702928-6.jpg.
     * 
     */
    public static String getBookFileSavePath(String fileType, String bookId, String fileName) {
        return ROOTPATH + fileType + "/" + bookId + "/" + fileName;
    }

    /**
     * 获得相关文件存储的文件夹.
     * 
     */
    public static String getBookFolderSavePath(String fileType, String bookId) {
        return ROOTPATH + fileType + "/" + bookId + "/";
    }

    /**
     * 获得相关文件存储的文件夹，方便线程里用.
     * 
     */
    public static String getBookFolderSavePath(String fileType, String bookId, String siteCode) {
        return ROOTPATH + siteCode + "/" + fileType + "/" + bookId + "/";
    }

    /**
     * 获取项目的域名路径 如：http://www.webtrn.cn:80/.
     * 
     */
    public static String getFullRequestPath(String url) {
        if (url == null || "".equals(url.trim())) {
            return "";
        }
        url = url.replace(FileUtils.root, "");
        if (url.startsWith("http") || (!url.startsWith("/incoming") && !url.startsWith("incoming"))) {
            return url;
        }
        try {
            // TODO 暂时设置为7天有效
            return FileUtils.cdnDomain
                + getCdnUrl(url.startsWith("/") ? url : "/" + url, System.currentTimeMillis() / 1000 + 604800);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 获取加密Url
     * 
     * @param url
     *            文件路劲
     * @param time
     *            有效时间
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String getCdnUrl(String url, long time) throws UnsupportedEncodingException {
        String key = url + "-" + time + "-0-0-" + Constants.CDN_SECRET;
        // TODO 解决编码问题，部分特殊字符在浏览器中不会被编码
        key = urlEncode(key);
        return url + "?auth_key=" + time + "-0-0-" + DigestUtils.md5Hex(key.getBytes());
    }

    /**
     * urlEncode方法
     * 
     */
    public static String urlEncode(String url) throws UnsupportedEncodingException {
        return UriUtils.encodeQuery(url, "utf-8").replace("%25", "%");
    }

    /**
     * 获得绝对路径.
     * 
     */
    public static String getAbsolutePath(String savePath, String root) {
        return root + savePath;
    }

    /**
     * 得到缓存文件夹.
     * 
     */
    public static String getTempFileFolder(BookInfo book, String type) {
        if (book.version != null) {
            return "/incoming/course/temp/" + book.coverCodes[0].code + type + book.id + "/" + book.version.toString();
        }
        return "/incoming/course/temp/" + book.coverCodes[0].code + type + book.id;
    }

    /**
     * 得到缓存文件路径.
     * 
     */
    public static String getTempFileSavePath(BookInfo book, String type) {
        String path = getTempFileFolder(book, type);
        String folder = "";
        try {
            folder = FileUtils.getFileSaveRealPath(path);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return folder;
    }

    /**
     * 将文件打包成zip
     * 
     * @param list
     *            文件绝对路径集合
     * @param zipPath
     *            zip的全路径
     * @throws SdkException
     * @throws IOException
     */
    public static void zipFile(List<String> list, String zipPath) throws SdkException {
        File zipFile = new File(zipPath);
        ZipOutputStream zipOut = null;
        FileOutputStream fous = null;
        FileInputStream in = null;
        try {
            if (!zipFile.exists()) {
                zipFile.createNewFile();
            }
            // 创建文件输出流
            fous = new FileOutputStream(zipFile);

            zipOut = new ZipOutputStream(fous);
            for (String str : list) {
                File file = new File(str);
                if (!file.exists()) {
                    continue;
                }
                // 创建文件输入流对象
                in = new FileInputStream(file);
                zipOut.putNextEntry(new ZipEntry(file.getName()));
                // 向压缩文件中输出数据
                int number = 0;
                byte[] buffer = new byte[1024];
                while ((number = in.read(buffer)) != -1) {
                    zipOut.write(buffer, 0, number);
                }
            }

        } catch (Exception e) {
            throw new SdkException(Constants.GET_INFO_FAILURE);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (zipOut != null) {
                    zipOut.close();
                }
                if (fous != null) {
                    fous.close();
                }
            } catch (IOException e) {
                throw new SdkException(Constants.GET_INFO_FAILURE);
            }
        }
    }

    /**
     * 获取切分后文件地址.
     * 
     */
    public static String getFilePartsSaveRealPath(String path) throws Exception {
        return getFileSaveRealPath(path.replace(trimExtension(new File(path).getName()), "").replace("/incoming/",
            "/incoming/" + FILE_PART + "/"));
    }
}

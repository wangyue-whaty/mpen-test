/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mp.lib.so;

/**
 * TODO 打包成自己格式的解析.
 */
public class SoundResourceDecoder {

    /**
     * 解析mp的核心方法
     *
     * @param code
     *            点读码值
     * @param num
     *            码值对应的第几个音频值，0表示第一个（英文），1表示第二个（中文）
     * @param savePath
     *            提取出来的文件保存在该目录下。sprintf(filename, "%s/%08X_%02d.mp3", savepath,
     *            code, num);
     */
    public static native byte[] mpParseMPByCode(int code, int num, int functionCode, String mpPath, String savePath);

    public static native byte[] mpParseMPByXY(int page, int x, int y, int num, int functionCode, String mpPath,
        String savePath);

    public static native byte[] mpGetFileInfo(String mpPath);

    /**
     * Mp解码方法（带缓存）.
     * 
     * @param page
     *            页数
     * @param x
     *            x轴千分比坐标
     * @param y
     *            y轴千分比坐标
     * @param num
     *            语言
     * @param functionCode
     *            功能值
     * @param mpPath
     *            mp包地址
     * @param savePath
     *            文件存放地址
     * @param bookId
     *            书籍id
     * @param utc
     *            书籍版本Utc
     * @return
     */
    public static native byte[] mpParseMPByXYCache(int page, int x, int y, int num, int functionCode, String mpPath,
        String savePath, String bookId, String utc);

}

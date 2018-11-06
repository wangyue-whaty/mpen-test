package com.mp.lib.so;

public class MPFileManager {
    /**
     * 功能说明：将大文件切成固定大小的多个小文件，供下载使用
     * 参数：jinfile: 需要切分的大文件名
     *     jsavepath: 切分成小文件后存放的目录
     *     jmode: 0表示jsavapath目录下有同名文件就不再重新生成该文件
     *      1 表示，不管有没有重名文件，全部重新重成文件
     * 返回值：返回值是一个字串，有二部分组成，中间用“___”隔开。
     * 返回值结构如下：“返回值数字___索引文件名”
     * 返回值数字说明：0表示切分文件失败， 数字：切成了多少个文件
     */
    public static native byte[] splitFile(String filePath, String savePath, int rebulid);
    
    /**
     * 功能说明：检测切分后生成的文件有没有问题。
     * 参数：jsavepath: 小文件存放的目录
     *     jlistfile: 小文件的索引文件名
     * 返回值：0检测失败    1：检测成功
     */
    public static native byte[] checkSpliteFile(String savePath, String fileName);
}

package com.mpen.api.util;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mpen.api.common.CompressionTools;

/**
 * 压缩解压工具测试类
 *
 */
public class CompressionToolsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompressionToolsTest.class);

    /**
     *测试zip压缩、解压工具
     */
    @Test
    public void zipCompressionTest() {
        /*
         * testStrings数组中testStrings[0]的字符串字节长度为4319，压缩后字节长度为：446，压缩率：0.10775549649673834
         * testStrings数组中testStrings[1]的字符串字节长度为112，压缩后字节长度为：111，压缩率：0.9910714285714286
         * testStrings数组中testStrings[2]的字符串字节长度为1272，压缩后字节长度为：203，压缩率：0.15959119496855345
         * testStrings数组中testStrings[3]的字符串字节长度为671，压缩后字节长度为：155，压缩率：0.23099850968703428
         * 说明：字符串长度越大，压缩率越高。
         */
        final String[] testStrings = {
                "[{\"score\":8.748,\"text\":\"My\",\"type\":2},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":8.556,\"text\":\"name\",\"type\":2},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":3.339,\"text\":\"is\",\"type\":2},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":7.186,\"text\":\"Lisa\",\"type\":2},{\"score\":0.0,\"text\":\". \",\"type\":7},{\"score\":8.878,\"text\":\"I\",\"type\":2},{\"score\":0.0,\"text\":\"’\",\"type\":7},{\"score\":8.155,\"text\":\"m\",\"type\":2},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":4.858,\"text\":\"at\",\"type\":2},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":1.797,\"text\":\"the\",\"type\":2},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":7.114,\"text\":\"park\",\"type\":2},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":9.162,\"text\":\"with\",\"type\":2},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":9.298,\"text\":\"my\",\"type\":2},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":7.803,\"text\":\"family\",\"type\":2},{\"score\":0.0,\"text\":\". \",\"type\":7},{\"score\":0.0,\"text\":\"We\",\"type\":1},{\"score\":0.0,\"text\":\"’\",\"type\":7},{\"score\":0.0,\"text\":\"re\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"having\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"a\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"picnic\",\"type\":1},{\"score\":0.0,\"text\":\".  \",\"type\":7},{\"score\":0.0,\"text\":\"My\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"mother\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"is\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"flying\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"a\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"kite\",\"type\":1},{\"score\":0.0,\"text\":\". \",\"type\":7},{\"score\":0.0,\"text\":\"My\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"father\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"is\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"reading\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"a\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"newspaper\",\"type\":1},{\"score\":0.0,\"text\":\".  \",\"type\":7},{\"score\":0.0,\"text\":\"My\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"little\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"brother\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"is\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"singing\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"a\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"song\",\"type\":1},{\"score\":0.0,\"text\":\". \",\"type\":7},{\"score\":0.0,\"text\":\"My\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"grandma\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"is\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"sleeping\",\"type\":1},{\"score\":0.0,\"text\":\". \",\"type\":7},{\"score\":0.0,\"text\":\"And\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"I\",\"type\":1},{\"score\":0.0,\"text\":\"’\",\"type\":7},{\"score\":0.0,\"text\":\"m\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"drawing\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"a\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"picture\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"of\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"my\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"family\",\"type\":1},{\"score\":0.0,\"text\":\"! \",\"type\":7},{\"score\":0.0,\"text\":\"The\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"weather\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"is\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"good\",\"type\":1},{\"score\":0.0,\"text\":\". \",\"type\":7},{\"score\":0.0,\"text\":\"It\",\"type\":1},{\"score\":0.0,\"text\":\"’\",\"type\":7},{\"score\":0.0,\"text\":\"s\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"sunny\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"and\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"warm\",\"type\":1},{\"score\":0.0,\"text\":\".\",\"type\":7}]",
                "[{\"score\":7.979,\"text\":\"play\",\"type\":2},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":8.542,\"text\":\"way\",\"type\":2}]",
                "{\"score\":0.0,\"text\":\"And\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"I\",\"type\":1},{\"score\":0.0,\"text\":\"’\",\"type\":7},{\"score\":0.0,\"text\":\"m\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"drawing\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"a\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"picture\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"of\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"my\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"family\",\"type\":1},{\"score\":0.0,\"text\":\"! \",\"type\":7},{\"score\":0.0,\"text\":\"The\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"weather\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"is\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"good\",\"type\":1},{\"score\":0.0,\"text\":\". \",\"type\":7},{\"score\":0.0,\"text\":\"It\",\"type\":1},{\"score\":0.0,\"text\":\"’\",\"type\":7},{\"score\":0.0,\"text\":\"s\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"sunny\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"and\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"warm\",\"type\":1},{\"score\":0.0,\"text\":\".\",\"type\":7}",
                "{\"score\":0.0,\"text\":\"! \",\"type\":7},{\"score\":0.0,\"text\":\"The\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"weather\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"is\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"good\",\"type\":1},{\"score\":0.0,\"text\":\". \",\"type\":7},{\"score\":0.0,\"text\":\"It\",\"type\":1},{\"score\":0.0,\"text\":\"’\",\"type\":7},{\"score\":0.0,\"text\":\"s\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"sunny\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"and\",\"type\":1},{\"score\":0.0,\"text\":\" \",\"type\":7},{\"score\":0.0,\"text\":\"warm\",\"type\":1},{\"score\":0.0,\"text\":\".\",\"type\":7}" };
        final byte[][] errorbytes = { { 3, 4, 5, 6, 123, 124, -124, 127, 123 }, { 5, 4, 5, 6, 123, 124, -124 },{ 5, 4, 44, 6, 123, 124, -124 },{ 5, 4, 44 } };
        for (int idx = 0; idx < testStrings.length; idx++) {
            zipCompression(testStrings[idx], errorbytes[idx]);
        }

    }

    /**
     * 测试zip方式解压，压缩文本的正确性以及压缩率
     * @param text
     * @param errorByte
     */
    private void zipCompression(String text, byte[] errorByte) {

        final byte[] userRecognizeTxtBytesAfter = CompressionTools.zip(text);
        final String userRecognizeTxtBytesunzip = CompressionTools.unzip(userRecognizeTxtBytesAfter);
        // 对比压缩前字符串与压缩、解压后字符串是否相同
        Assert.assertEquals(userRecognizeTxtBytesunzip, text);
        final byte[] userRecognizeTxtBytesBefore = text.getBytes();
        // 字符串压缩率
        final double compressionRatio = (double) userRecognizeTxtBytesAfter.length / userRecognizeTxtBytesBefore.length;
        LOGGER.info("原字符串字节长度为：" + userRecognizeTxtBytesBefore.length);
        LOGGER.info("压缩后字节长度为：" + userRecognizeTxtBytesAfter.length);
        LOGGER.info("压缩率：" + compressionRatio);
        String errorByteUnzip = CompressionTools.unzip(errorByte);
        // 错误的byte,解压为空
        Assert.assertEquals(errorByteUnzip, "");

    }

}

package com.mp.shared.common;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by wutingyou on 2017/9/25.
 * Utils 单元测试
 */
public class UtilsTest {

    @Test
    public void testIsContentValid() {
        final String[] NECESSARY_BOOK_ID_LIST = {"ff80808156ca3d900156cb19feff004c"/**新标准一起一上学生用书*/,
                "ff808081581deb4101581e74ac7d0088"/**新标准一起一下学生用书*/,
                "ff808081567761c2015691ef1d2e06a6"/**新标准一起二上学生用书*/, "ff808081567761c2015691fd171d06a9"/**新标准一起二下学生用书*/,
                "ff808081567761c2015691ff8b7e06ab"/**新标准一起三上学生用书*/, "ff808081567761c201569201cfde06ad"/**新标准一起三下学生用书*/,
                "ff808081567761c201569203814e06af"/**新标准一起四上学生用书*/, "ff808081567761c201569205449706b5"/**新标准一起四下学生用书*/,
                "ff808081567761c201569206d9d206b7"/**新标准一起五上学生用书*/, "ff808081567761c201569208005d06b9"/**新标准一起五下学生用书*/,
                "ff808081567761c201569209573106bb"/**新标准一起六上学生用书*/, "ff808081567761c20156920b17fb06bd"/**新标准一起六下学生用书*/,
                "ff808081567761c20156920d03f706c0"/**新标准三起三上学生用书*/, "ff8080815847b3010158489abb5600ae"/**新标准三起三下学生用书*/,
                "ff808081567761c20156920f6c5006c4"/**新标准三起四上学生用书*/, "ff808081567761c2015692106cfa06c6"/**新标准三起四下学生用书*/,
                "ff808081567761c2015692119de006c8"/**新标准三起五上学生用书*/, "ff808081567761c201569212caef06ca"/**新标准三起五下学生用书*/,
                "ff808081567761c201569213fdca06cc"/**新标准三起六上学生用书*/, "ff808081567761c2015692154c5506ce"/**新标准三起六下学生用书*/,
                "004737a884ec45a6ac42d7ce4afa615c"/**丽声教师版1级3*/, "0062fea5b75b41a3b9265fc521d07ebe"/**乐乐园儿童英语学生包3*/,
                "006e9d36312e49ffafb070ebe01d86bb"/**我爱小怪物*/, "fff811994fb045e68ea72369ed9d781e"/**衣服变变变*/,
                "ff8080815d0cec5b015d30e15ecc01dd"/**衣食住行一本通*/, "ff8080815d0cec5b015d30dfc51c01d5"/**看视频学英语口语观光旅游一本通*/,
                "a78bf7a2acd64fcdb43b83f1d9fc8fb7"/**励步贴纸4*/, "a7ab9fb4d9794369bfe4db9b99b3f94d"/**丽声冒险故事岛*/,
                "a7dd6ee61e0940c589e7212879e49558"/**中华传统故事双语绘本-成语故事1*/};

        final List<BookInfo> bookInfoList = new ArrayList<>();
        for (int i = 1; i < NECESSARY_BOOK_ID_LIST.length + 1; i ++) {
            for (int j = 0; j < i; j ++) {
                final BookInfo bookInfo = new BookInfo();
                bookInfo.id = NECESSARY_BOOK_ID_LIST[j];
                bookInfoList.add(bookInfo);
            }
            // 凑足2000个数据以上
            for (int k = 0; k < 2000; k ++) {
                final BookInfo bookInfo = new BookInfo();
                bookInfo.id = String.valueOf(k);
                bookInfoList.add(bookInfo);
            }
            if (i < NECESSARY_BOOK_ID_LIST.length - 1) {
                assertFalse(Utils.isContentValid(bookInfoList));
            } else {
                assertTrue(Utils.isContentValid(bookInfoList));
            }
            bookInfoList.clear();
        }
    }
}
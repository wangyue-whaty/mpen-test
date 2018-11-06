package com.mp.shared.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 3/6/17.
 *
 * 由于希望这里的代码最终能够和后台共享，所以把里面用的一些常用功能放在这里， 而不是全局的Utils
 */

public final class Utils {
    /**
     * @param byteStr
     *            假设byteStr是一个数字字符串比如 1234567890
     * @param radix
     *            4, 10, 16 进制
     * @return 解析的byte array
     */
    public static byte[] byteArrayFromStr(String byteStr, int radix) {
        if (byteStr == null) {
            return null;
        }
        final int len = byteStr.length();
        final byte[] byteVal = new byte[len];
        for (int idx = 0; idx < len; ++idx) {
            byteVal[idx] = (byte) Character.digit(byteStr.charAt(idx), radix);
        }
        return byteVal;
    }

    /**
     * 方便比较，考虑null
     */
    public static boolean equals(Object obj, Object other) {
        return obj == other || (obj != null && obj.equals(other));
    }

    /**
     * 方便比较，考虑null
     */
    public static <T extends IsEqual> boolean isEqual(T obj, T other) {
        return obj == other || (obj != null && obj.isEqual(other));
    }

    /**
     * 查看list是不是为空
     */
    public static <T> boolean isEmpty(T[] list) {
        return list == null || list.length == 0;
    }

    /**
     * 查看String是不是为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断数据列表是否合法
     * @param list 数据列表
     * @param <T>
     * @return true or false
     */
    public static <T extends IsValid> boolean isValid(List<T> list) {
        if (list == null || list.size() == 0) {
            return false;
        }
        for (T item:list) {
            if (!item.isValid()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 新标准教程资源，必要资源,以及bookList已存在的前3本中间3本和后3本资源
     */
    private static final String[] NECESSARY_BOOK_ID_LIST = {"ff80808156ca3d900156cb19feff004c"/**新标准一起一上学生用书*/,
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

    private static final int ERROR_TIMES = 2;
    private static final int ITEMS_SIZE_LIMIT = 2000;
    /**
     * 检查bookList 数据内容是否合法，
     * 主要判断依据是数据列表必须大于2000条数据并且必须存在指定的资源
     * @param bookInfoList 书本清单列表
     * @return true or false
     */
    public static boolean isContentValid(List<BookInfo> bookInfoList) {
        if (bookInfoList == null || bookInfoList.size() < ITEMS_SIZE_LIMIT) {
            return false;
        }
        final HashMap<String, Integer> foundCounts = new HashMap<>();
        for (String bookId:NECESSARY_BOOK_ID_LIST) {
            foundCounts.put(bookId, 0);
        }

        for (BookInfo bookInfo:bookInfoList) {
            final Integer count = foundCounts.get(bookInfo.id);
            if (count != null) {
                foundCounts.put(bookInfo.id, count + 1);
            }
        }

        // 同时检查了bookList如果重复的bookId也返回错误
        int errorTimes = 0;
        for (Map.Entry<String, Integer> entry : foundCounts.entrySet()) {
            final Integer count = entry.getValue();
            if (count == 0) {
                errorTimes ++;
            } else if (count > 1) {
                return false;
            }
        }
        return errorTimes < ERROR_TIMES;
    }
}

package com.mp.shared.common;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by feng on 3/9/17.
 * <p>
 * 定义了 MpCode, MpCode.Range, MpCode.Point
 * 以及一些相关操作
 * <p>
 * MpCode由下面几个部分组成
 * * baseCode, highCode:
 * -- byteValue从4进制转换成 long
 * -- 7X7，8X8， 低 MAX_BASECODE_BITS 转换为 baseCode，超过的转换为 highCode
 * -- 5X5, 6X6 highCode一直为0
 * * size: 5, 6, 7, 8
 * <p>
 * 操作：
 * ＊ static 方法： fromByteArray，fromRadix4Str
 * ＊ toHexStr：没有padding
 * ＊ toRadix4Leading0s： 有padding
 * ＊ toByteVal
 * ＊ Comparable<MpCode> interface
 * ＊ subtract： 计算两个码的间距
 */

public final class MpCode implements Serializable, Comparable<MpCode>, Comparator<MpCode> {
    private static final long serialVersionUID = 4569472404345533993L;

    public final long baseCode;
    public final long highCode;
    public final byte size;

    public MpCode(long baseCode, long highCode, byte size) {
        this.baseCode = baseCode;
        this.highCode = highCode;
        this.size = size;
    }

    /**
     * an efficient way to parse radix based byte array to long
     * significant bit first
     * -- assume byteArray doesn't overflow
     */
    public static long byteArrayToLong(byte[] byteVal, int startIdx, int endIdx,
                                       int radix) {
        long result = 0;
        for (int idx = startIdx; idx < endIdx; ++idx) {
            result = result * radix - byteVal[idx];
        }
        return -result;
    }

    private static final int MAX_BASECODE_BITS = 4 * 7;

    /**
     * 从4进制MP code到 MpCode。
     *
     * @param byteVal 4-进制 的 MP码的二进制表示
     * @return MpCode；null 如果有错误
     */
    public static MpCode fromByteArray(byte[] byteVal) {
        if (byteVal == null) {
            return null;
        }
        final int len = byteVal.length;
        switch (len) {
            case 16:
                return new MpCode(byteArrayToLong(byteVal, 0, 16, 4), 0, (byte) 5);
            case 25:
                return new MpCode(byteArrayToLong(byteVal, 0, 25, 4), 0, (byte) 6);
            case 36:
                return new MpCode(
                        byteArrayToLong(byteVal, len - MAX_BASECODE_BITS, len, 4),
                        byteArrayToLong(byteVal, 0, len - MAX_BASECODE_BITS, 4),
                        (byte) 7);
            case 49:
                return new MpCode(
                        byteArrayToLong(byteVal, len - MAX_BASECODE_BITS, len, 4),
                        byteArrayToLong(byteVal, 0, len - MAX_BASECODE_BITS, 4),
                        (byte) 8);
            default:
                return null;
        }
    }

    /**
     * @return hex string; no leading 0s
     */
    public String toHexStr() {
        if (size == 5 || size == 6) {
            return Long.toHexString(baseCode);
        } else {
            return String.format("%X%014X", highCode, baseCode);
        }
    }

    private static final String ZEROS = String.format("%060d", 0);

    /**
     * @return 0 padded radix 4 string
     */
    public String toRadix4Leading0s() {
        final int len = (size - 1) * (size - 1);
        switch (size) {
            case 5:
            case 6:
                final String radix4Str = Long.toString(baseCode, 4);
                return ZEROS.substring(0, len - radix4Str.length()) + radix4Str;
            case 7:
            case 8:
                final int highLen = len - MAX_BASECODE_BITS;
                final String baseStr = Long.toString(baseCode, 4);
                final String highStr = Long.toString(highCode, 4);
                return ZEROS.substring(0, highLen - highStr.length()) + highStr
                        + ZEROS.substring(0, MAX_BASECODE_BITS - baseStr.length())
                        + baseStr;
            default:
                return null;
        }
    }

    private static final String MP_PREFIX = "MP_";
    @Override
    public String toString() {
        return MP_PREFIX + toRadix4Leading0s();
    }

    public static MpCode fromString(String mpPrefxRadix4Leading0sStr) {
        if (mpPrefxRadix4Leading0sStr.startsWith(MP_PREFIX)) {
            return fromRadix4Str(mpPrefxRadix4Leading0sStr.substring(MP_PREFIX.length()));
        }
        return null;
    }

    /**
     * @return the byte[]
     */
    private static byte[] toByteVal(String radix4Str) {
        final int len = radix4Str.length();
        final byte[] byteVal = new byte[len];
        for (int idx = 0; idx < len; ++idx) {
            byteVal[idx] = (byte) Character.digit(radix4Str.charAt(idx), 10);
        }
        return byteVal;
    }

    /**
     * @return the byte[]
     * TODO improve efficiency
     */
    public byte[] toByteVal() {
        return toByteVal(toRadix4Leading0s());
    }

    /**
     * TODO improve efficiency
     *
     * @param radix4Str the radix 4 string with leading 0s
     * @return the MpCode, can be null
     */
    public static MpCode fromRadix4Str(String radix4Str) {
        return fromByteArray(toByteVal(radix4Str));
    }

    public static final long INVALID_DIFF = Long.MAX_VALUE;

    /**
     * 计算两个码的间距
     * 只有两个码有同样的size和highCode才有合适的比较
     *
     * @param another
     * @return 间距；INVALID_DIFF 如果不能比较
     */
    public long subtract(MpCode another) {
        if (another == null || size != another.size || highCode != another.highCode) {
            return INVALID_DIFF;
        }
        return baseCode - another.baseCode;
    }

    /**
     * 依次比较： size，highCode，baseCode
     */
    @Override
    public int compareTo(MpCode another) {
        if (size != another.size) {
            return size - another.size;
        } else if (highCode != another.highCode) {
            return highCode < another.highCode ? -1
                    : (highCode == another.highCode ? 0 : 1);
        } else {
            return baseCode < another.baseCode ? -1
                    : (baseCode == another.baseCode ? 0 : 1);
        }
    }

    /**
     * Compares the two specified objects to determine their relative ordering. The ordering
     * implied by the return value of this method for all possible pairs of
     * {@code (lhs, rhs)} should form an <i>equivalence relation</i>.
     * This means that
     * <ul>
     * <li>{@code compare(a,a)} returns zero for all {@code a}</li>
     * <li>the sign of {@code compare(a,b)} must be the opposite of the sign of {@code
     * compare(b,a)} for all pairs of (a,b)</li>
     * <li>From {@code compare(a,b) > 0} and {@code compare(b,c) > 0} it must
     * follow {@code compare(a,c) > 0} for all possible combinations of {@code
     * (a,b,c)}</li>
     * </ul>
     *
     * @param lhs an {@code Object}.
     * @param rhs a second {@code Object} to compare with {@code lhs}.
     * @return an integer < 0 if {@code lhs} is less than {@code rhs}, 0 if they are
     * equal, and > 0 if {@code lhs} is greater than {@code rhs}.
     * @throws ClassCastException if objects are not of the correct type.
     */
    @Override
    public int compare(MpCode lhs, MpCode rhs) {
        return lhs.compareTo(rhs);
    }

    /**
     * 表达一个MP码范围：一个有效范围假设 size和highCode是一样的
     */
    public static final class Range implements Serializable {
        private static final long serialVersionUID = -5709132503879437956L;
        public long startCode;
        public long endCode;
        public long highCode;
        public byte size;

        /**
         * @return 看看code是不是在范围里 （相同size，highCode）
         */
        public boolean checkInRange(MpCode code) {
            return (code != null && code.size == size && code.highCode == highCode
                    && code.baseCode >= startCode && endCode >= code.baseCode);
        }
    }

    public static final class FPoint {
        public float x;
        public float y;
        public int pageNum;

        public Point toPoint() {
            final Point point = new Point();
            point.x = (int)(x * 1000);
            point.y = (int)(y * 1000);
            point.pageNum = pageNum;
            return point;
        }
    }
    /**
     * 表达一个笔尖在的位置
     */
    public static final class Point implements Serializable, IsEqual<Point> {
        private static final long serialVersionUID = -4163741472654515201L;
        public static final int INVALID_XY = Integer.MIN_VALUE;
        public int x = INVALID_XY;
        public int y = INVALID_XY;
        public int pageNum = -1;

        /**
         * @return if the object has valid data
         */
        public boolean isValid() {
            return pageNum >= 0 && x != INVALID_XY && y != INVALID_XY;
        }

        /**
         * to see if two Point has the same value
         * -- TODO now compare exact float, consider whether to loose
         */
        @Override
        public boolean isEqual(Point other) {
            if (other == null) {
                return false;
            }
            return pageNum == other.pageNum && x == other.x && y == other.y;
        }

        private static final String MPPOINT_PREFIX = "MPPoint_";
        private static final String MPPOINT_INVALID = "MPPoint_INVALID";
        @Override
        public String toString() {
            if (isValid()) {
                return String.format("%s%d_%d_%d", MPPOINT_PREFIX, pageNum, x, y);
            } else {
                return "MPPoint_INVALID";
            }
        }
    }

}

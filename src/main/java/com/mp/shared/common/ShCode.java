package com.mp.shared.common;

import java.io.Serializable;

/**
 * Created by feng on 3/4/17.
 *
 * 用这个代表松翰码，包括二代码和OID3码
 */
public final class ShCode implements Serializable, IsEqual<ShCode> {
    private static final long serialVersionUID = 4569472404345533992L;
    public static final int OID3_BASE = 16;
    public static final int COVER_BASE = 0; // 封面码 （60000及以上）
    public static final int WYS_SUBTYPE = 1; //外研社的subType
    public static final int RJS_SUBTYPE = 2; //人教社的subType
    public static final int ZSS_SUBTYPE = 4; //中少社的subType
    public static final int WHITE_SPACE = 65535;
    public static final int SPECIAL_CODE_START = 60000;

    /***************************************************
     * 以下是具体数据（类成员变量）: code, subType, byteVal
     */
    public final int code;
    /*
     * For SH 加入松翰二代码的subType，值表示如下：
     * 0: 60000 以上码值正常返回
     * 1: 0-59999 码值，外研社的
     * 2: 0-59999 码值，别的出版社的（人教社用这个）
     * 3: 0-59999 码值，别的出版社的（未知）
     * 4: 0-59999 码值，中少社
     * 码值，别的出版社的（未知） 5，6，7，8: 60000以上码值，异常 －－ 如果发现这些返回，告诉我情况
     */
    public final int subType;

    public ShCode(int code, int subType) {
        this.code = code;
        this.subType = subType;
    }

    /**
     * 判断两个码值是否相同。 考虑兼容性：对比OID3码，或者OID3和二代码，只比较码值。
     *
     * @param other
     *            另外一个码
     * @return true 如果相同； false 如果不同
     */
    @Override
    public boolean isEqual(ShCode other) {
        if (other == null || code != other.code) {
            return false;
        }
        if (code <= 65535 && subType < OID3_BASE && other.subType < OID3_BASE) {
            return subType == other.subType;
        } else {
            return true;
        }
    }

    /**
     * @return 判断两个码是不是完全相同：code，subType，byteVal
     */
    public boolean isIdentical(ShCode other) {
        return code == other.code && subType == other.subType;
    }

    /**
     * 判断 shCodes是不是包括this
     */
    public boolean isIn(final ShCode[] shCodes) {
        if (shCodes == null) {
            return false;
        }
        for (final ShCode thisCode: shCodes) {
            if (this.isEqual(thisCode)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断 shCodes是不是包括shCode
     */
    public static boolean isIn(final ShCode[] shCodes, final ShCode shCode) {
        return shCode != null && shCode.isIn(shCodes);
    }

    /**
     * @return if the code is white space
     */
    public boolean isWhitespace() {
        return code == WHITE_SPACE;
    }

    /**
     * @return if the code *MAY* be special code
     */
    public boolean isPossibleSpecialCode() {
        return code >= SPECIAL_CODE_START && code <= 65535;
    }

    private static final String STR_PREFIX = "SH_";

    /**
     * @return more meaningful string description
     */
    @Override
    public String toString() {
        return toString(code, subType);
    }

    /**
     * @return more meaningful string description
     */
    public static String toString(int code, int subType) {
        StringBuilder sb = new StringBuilder(STR_PREFIX);
        sb.append(code).append('_').append(subType);
        return sb.toString();
    }

    /**
     *
     * @param codeStr
     *            应该是用 ShCode.toString生成的一个结果
     * @return 从codeStr来解析出一个ShCode
     */
    public static ShCode fromString(String codeStr) {
        if (codeStr == null || !codeStr.startsWith(STR_PREFIX)) {
            return null;
        }
        final String[] parts = codeStr.split("_");
        switch (parts.length) {
            case 3:
                try {
                    final int code = Integer.parseInt(parts[1]);
                    final int subType = Integer.parseInt(parts[2]);
                    return new ShCode(code, subType);
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }

    /**
     * 从Code对象来构建一个专门的ShCode
     *
     * @return null if not SH or OID3 code; otherwise created ShCode
     */
    public static ShCode fromCode(Code code) {
        return code.shCode;
    }

    /**
     * @return 构建的 Code 对象
     */
    public Code toCode() {
        final Code code = new Code();
        code.type = subType < OID3_BASE ? Code.Type.SH : Code.Type.OID3;
        code.shCode = this;
        return code;
    }

    /***************************************************
     * 以下是 Range 类
     */
    /**
     * 表示一个松翰码的范围
     */
    public static final class Range implements Serializable {
        private static final long serialVersionUID = 7326075298185934641L;
        public final int startCode;
        public final int endCode;
        public final int subType;

        public Range(int startCode, int endCode, int subType) {
            this.startCode = startCode;
            this.endCode = endCode;
            this.subType = subType;
        }

        /**
         * 判断一个码是不是在范围内 考虑兼容性：对比OID3码，或者OID3和二代码，只比较码值。
         *
         * @param shCode
         *            另外一个码
         * @return true 如果在里面； false 如果不在
         */
        public boolean checkInRange(ShCode shCode) {
            if (shCode == null || shCode.code < startCode || shCode.code > endCode) {
                return false;
            }
            if (subType < OID3_BASE && shCode.subType < OID3_BASE) {
                return subType == shCode.subType;
            } else {
                return true;
            }
        }
        
        public int compareTo(Range another) {
            return this.startCode - another.startCode;
        }
    }

}

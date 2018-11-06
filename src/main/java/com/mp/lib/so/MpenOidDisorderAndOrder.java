package com.mp.lib.so;

public class MpenOidDisorderAndOrder {

    /*
     * 功能说明：码值扰乱函数接口 参数：env: JNIEnv jc: jclass jsrc:
     * 码值的字串，需要将码值转成16进制字符的字串作为参数传进来（例如“30EF283C”）。 jdottype: 码值的类型（0~4），
     * 0：5x5点阵， 1：6x6点阵， 2：7x7点阵， 3：8x8点阵， 4：9x9阵
     * 返回值：返回值是一个字串，有两部分组成，中间用“___”隔开。 例如：1___7591 其中‘1’表示扰乱成功， 如为‘0’则表示扰乱失败
     * “5A2156DE”为扰乱后生成的码值，供铺码时使用。
     */
    public native static byte[] MPENOIDDisorder(String code, int type);

    /*
     * 功能说明：码值还原函数接口 参数：env: JNIEnv jc: jclass jsrc:
     * 码值的字串，需要将码值转成16进制字符的字串作为参数传进来（例如“5A2156DE”）。 jdottype: 码值的类型（0~4），
     * 0：5x5点阵， 1：6x6点阵， 2：7x7点阵， 3：8x8点阵， 4：9x9阵
     * 返回值：返回值是一个字串，有两部分组成，中间用“___”隔开。 例如：1___55EEFA32 其中‘1’表示还原成功， 如为‘0’则表示还原失败
     * “30EF283C”为还原后的码值，供应用程序使用。
     */
    public native static byte[] MPENOIDOrder(String code, int type);
    
    // TODO：当前7x7点阵及以上返回的数值是超过long的表达范围的。以后输入输出都应该是MpCode，而不是long
    public static long mpenDisturb(long code, int type) {
        final String mpenoidDisorder = new String(MPENOIDDisorder(String.valueOf(Long.toHexString(code)), type));
        final String[] split = mpenoidDisorder.split("___");
        if (split[0].equals("1")) {
            return Long.parseLong(split[1], 16);
        }
        return -1;
    }

    // TODO：当前7x7点阵及以上返回的数值是超过long的表达范围的。以后输入输出都应该是MpCode，而不是long  
    public static long mpenRestore(long code, int type) {
        final String mpenoidOrder = new String(MPENOIDOrder(String.valueOf(Long.toHexString(code)), type));
        final String[] split = mpenoidOrder.split("___");
        if (split[0].equals("1")) {
            return Long.parseLong(split[1], 16);
        }
        return -1;
    }

}

package com.mp.shared.service;

import com.mp.shared.common.SpecialMPArea;

public final class SpecialWifiMPCodes {
    public static final String PAGEINFO_ID = "ff8080815c73acad015c9a3e3b4802ca";

    private static final String TAG = "FactoryWifi";

    // 像素单位
    public static final float WIDTH_PIX = 9920;

    public static final float HEIGHT_PIX = 14030;

    public static final SpecialMPArea<Type>[] wifiArray = init();

    private static SpecialMPArea<Type>[] init() {
        final SpecialMPArea<Type>[] wifiArray = new SpecialMPArea[35];
        wifiArray[0] = new SpecialMPArea<Type>(1, 20, 27, 207, 155, Type.WIFI_01);
        wifiArray[1] = new SpecialMPArea<Type>(1, 211, 27, 398, 155, Type.WIFI_02);
        wifiArray[2] = new SpecialMPArea<Type>(1, 403, 27, 590, 155, Type.WIFI_03);
        wifiArray[3] = new SpecialMPArea<Type>(1, 595, 27, 782, 155, Type.WIFI_04);
        wifiArray[4] = new SpecialMPArea<Type>(1, 787, 27, 974, 155, Type.WIFI_05);
        wifiArray[5] = new SpecialMPArea<Type>(1, 20, 163, 207, 291, Type.WIFI_06);
        wifiArray[6] = new SpecialMPArea<Type>(1, 211, 163, 398, 291, Type.WIFI_07);
        wifiArray[7] = new SpecialMPArea<Type>(1, 403, 163, 590, 291, Type.WIFI_08);
        wifiArray[8] = new SpecialMPArea<Type>(1, 595, 163, 782, 291, Type.WIFI_09);
        wifiArray[9] = new SpecialMPArea<Type>(1, 787, 163, 974, 291, Type.WIFI_10);
        wifiArray[10] = new SpecialMPArea<Type>(1, 20, 302, 207, 431, Type.WIFI_11);
        wifiArray[11] = new SpecialMPArea<Type>(1, 211, 302, 398, 431, Type.WIFI_12);
        wifiArray[12] = new SpecialMPArea<Type>(1, 403, 302, 590, 431, Type.WIFI_13);
        wifiArray[13] = new SpecialMPArea<Type>(1, 595, 302, 782, 431, Type.WIFI_14);
        wifiArray[14] = new SpecialMPArea<Type>(1, 787, 302, 974, 431, Type.WIFI_15);
        wifiArray[15] = new SpecialMPArea<Type>(1, 20, 438, 207, 566, Type.WIFI_16);
        wifiArray[16] = new SpecialMPArea<Type>(1, 211, 438, 398, 566, Type.WIFI_17);
        wifiArray[17] = new SpecialMPArea<Type>(1, 403, 438, 590, 566, Type.WIFI_18);
        wifiArray[18] = new SpecialMPArea<Type>(1, 595, 438, 782, 566, Type.WIFI_19);
        wifiArray[19] = new SpecialMPArea<Type>(1, 787, 438, 974, 566, Type.WIFI_20);
        wifiArray[20] = new SpecialMPArea<Type>(1, 20, 577, 207, 705, Type.WIFI_21);
        wifiArray[21] = new SpecialMPArea<Type>(1, 211, 577, 398, 705, Type.WIFI_22);
        wifiArray[22] = new SpecialMPArea<Type>(1, 403, 577, 590, 705, Type.WIFI_23);
        wifiArray[23] = new SpecialMPArea<Type>(1, 595, 577, 782, 705, Type.WIFI_24);
        wifiArray[24] = new SpecialMPArea<Type>(1, 787, 577, 974, 705, Type.WIFI_25);
        wifiArray[25] = new SpecialMPArea<Type>(1, 20, 712, 207, 841, Type.WIFI_26);
        wifiArray[26] = new SpecialMPArea<Type>(1, 211, 712, 398, 841, Type.WIFI_27);
        wifiArray[27] = new SpecialMPArea<Type>(1, 403, 712, 590, 841, Type.WIFI_28);
        wifiArray[28] = new SpecialMPArea<Type>(1, 595, 712, 782, 841, Type.WIFI_29);
        wifiArray[29] = new SpecialMPArea<Type>(1, 787, 712, 974, 841, Type.WIFI_30);
        wifiArray[30] = new SpecialMPArea<Type>(1, 20, 850, 207, 978, Type.WIFI_31);
        wifiArray[31] = new SpecialMPArea<Type>(1, 211, 850, 398, 978, Type.WIFI_32);
        wifiArray[32] = new SpecialMPArea<Type>(1, 403, 850, 590, 978, Type.WIFI_33);
        wifiArray[33] = new SpecialMPArea<Type>(1, 595, 850, 782, 978, Type.WIFI_34);
        wifiArray[34] = new SpecialMPArea<Type>(1, 787, 850, 974, 978, Type.WIFI_35);
        return wifiArray;
    }

    public enum Type {
        WIFI_01, WIFI_02, WIFI_03, WIFI_04, WIFI_05, WIFI_06, WIFI_07, WIFI_08, WIFI_09, WIFI_10, WIFI_11, WIFI_12, WIFI_13, WIFI_14, WIFI_15, WIFI_16, WIFI_17, WIFI_18, WIFI_19, WIFI_20, WIFI_21, WIFI_22, WIFI_23, WIFI_24, WIFI_25, WIFI_26, WIFI_27, WIFI_28, WIFI_29, WIFI_30, WIFI_31, WIFI_32, WIFI_33, WIFI_34, WIFI_35
    }

}

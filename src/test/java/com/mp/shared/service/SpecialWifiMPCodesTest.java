package com.mp.shared.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.mp.shared.common.Code;
import com.mp.shared.common.MpCode;
import com.mp.shared.common.PageInfo;
import com.mp.shared.common.QuickCodeInfo;
import com.mp.shared.common.SpecialMPArea;

public class SpecialWifiMPCodesTest {
    QuickCodeInfo quickCodeInfo;

    @Before
    public void setQuickCodeInfo() {
        quickCodeInfo = new QuickCodeInfo();
        quickCodeInfo.pageInfo = new PageInfo();
        quickCodeInfo.pageInfo.pageNum = 1;
        quickCodeInfo.pageInfo.startCode = new MpCode(2183482198L, 0, new Byte("5"));
        quickCodeInfo.pageInfo.xCodeNum = 124;
        quickCodeInfo.pageInfo.yCodeNum = 175;
        quickCodeInfo.pageInfo.matrixGap = 0;
        quickCodeInfo.pageInfo.margin = new int[4];
        quickCodeInfo.pageInfo.margin[0] = 0;
        quickCodeInfo.pageInfo.margin[1] = 0;
        quickCodeInfo.pageInfo.margin[2] = 0;
        quickCodeInfo.pageInfo.margin[3] = 30;
        quickCodeInfo.pageInfo.matrixSize = 5;
        quickCodeInfo.pageInfo.dotSize = 4;
        quickCodeInfo.pageInfo.dotShift = 3;
        quickCodeInfo.code = new Code();
        quickCodeInfo.code.type = Code.Type.MP;
        quickCodeInfo.code.prx = 1;
        quickCodeInfo.code.pry = 1;
    }

    @Test
    public void testSpecialWifiMPCodes() {
        final long[] code = { 2183484445L, 2183483225L, 2183484245L, 2183484143L, 2183484164L, 2183486674L, 2183486948L,
            2183488092L, 2183486620L, 2183486158L, 2183489902L, 2183490795L, 2183491198L, 2183491462L, 2183491492L,
            2183494248L, 2183492645L, 2183492671L, 2183492941L, 2183492723L, 2183495723L, 2183495746L, 2183495773L,
            2183495798L, 2183495816L, 2183498576L, 2183498725L, 2183498511L, 2183498775L, 2183499165L, 2183501550L,
            2183501824L, 2183501976L, 2183502249L, 2183502522L };
        final SpecialWifiMPCodes.Type[] expectType = { SpecialWifiMPCodes.Type.WIFI_01, SpecialWifiMPCodes.Type.WIFI_02,
            SpecialWifiMPCodes.Type.WIFI_03, SpecialWifiMPCodes.Type.WIFI_04, SpecialWifiMPCodes.Type.WIFI_05,
            SpecialWifiMPCodes.Type.WIFI_06, SpecialWifiMPCodes.Type.WIFI_07, SpecialWifiMPCodes.Type.WIFI_08,
            SpecialWifiMPCodes.Type.WIFI_09, SpecialWifiMPCodes.Type.WIFI_10, SpecialWifiMPCodes.Type.WIFI_11,
            SpecialWifiMPCodes.Type.WIFI_12, SpecialWifiMPCodes.Type.WIFI_13, SpecialWifiMPCodes.Type.WIFI_14,
            SpecialWifiMPCodes.Type.WIFI_15, SpecialWifiMPCodes.Type.WIFI_16, SpecialWifiMPCodes.Type.WIFI_17,
            SpecialWifiMPCodes.Type.WIFI_18, SpecialWifiMPCodes.Type.WIFI_19, SpecialWifiMPCodes.Type.WIFI_20,
            SpecialWifiMPCodes.Type.WIFI_21, SpecialWifiMPCodes.Type.WIFI_22, SpecialWifiMPCodes.Type.WIFI_23,
            SpecialWifiMPCodes.Type.WIFI_24, SpecialWifiMPCodes.Type.WIFI_25, SpecialWifiMPCodes.Type.WIFI_26,
            SpecialWifiMPCodes.Type.WIFI_27, SpecialWifiMPCodes.Type.WIFI_28, SpecialWifiMPCodes.Type.WIFI_29,
            SpecialWifiMPCodes.Type.WIFI_30, SpecialWifiMPCodes.Type.WIFI_31, SpecialWifiMPCodes.Type.WIFI_32,
            SpecialWifiMPCodes.Type.WIFI_33, SpecialWifiMPCodes.Type.WIFI_34, SpecialWifiMPCodes.Type.WIFI_35 };
        for (int i = 0; i < code.length; i++) {
            quickCodeInfo.code.mpCode = new MpCode(code[i], 0, new Byte("5"));
            quickCodeInfo.code.mpPoint = null;
            final SpecialMPArea<SpecialWifiMPCodes.Type> wifiBean = SpecialMPArea.getSpecialMPArea(quickCodeInfo,
                SpecialWifiMPCodes.wifiArray);
            assertEquals(wifiBean.type, expectType[i]);
        }
    }
}

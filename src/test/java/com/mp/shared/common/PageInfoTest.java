package com.mp.shared.common;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.mp.shared.common.MpCode.Point;
import com.mp.shared.utils.FUtils;
import com.mp.shared.utils.Json;

public class PageInfoTest {
    PageInfo[] pageInfos;
    MpCode[] codes;

    @Before
    public void prepare() {
        final File file = new File(getClass().getResource("/pageInfos/pageInfos.json").getPath().replace("%20", " "));
        final String json = FUtils.fileToString(file);
        pageInfos = Json.GSON.fromJson(json, PageInfo[].class);
        codes = new MpCode[3];
        codes[0] = new MpCode(2150434367l, 0, new Byte("5"));
        codes[1] = new MpCode(2214950751l, 0, new Byte("5"));
        codes[2] = new MpCode(2214961336l, 0, new Byte("5"));
    }

    @Test
    public void checkInPageTest() {
        final int[][] expectCheckInPage = { { 79, 111, -1 }, { 5, 5, 1 }, { 29, 20, 2 } };
        for (int i = 0; i < pageInfos.length; i++) {
            final int[] checkInPage = pageInfos[i].checkInPage(codes[i]);
            for (int j = 0; j < 2; j++) {
                assertEquals(expectCheckInPage[i][j], checkInPage[j]);
            }
        }
    }

    @Test
    public void crtcTest() {
        final String[] expectPoint = { "MPPoint_1_975_988", "MPPoint_1_278_365", "MPPoint_1_710_787" };
        for (int i = 0; i < pageInfos.length; i++) {
            final Point point = pageInfos[i].crtc(codes[i], 0, 0);
            assertEquals(point.toString(), expectPoint[i]);
        }
    }
}

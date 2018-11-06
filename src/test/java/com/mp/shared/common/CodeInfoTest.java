package com.mp.shared.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by feng on 3/24/17.
 *
 * test checkInPage, crtc
 */

public final class CodeInfoTest {

    // TODO add more test cases
    @Test
    public void simpleTest() {
        final PageInfo pageInfo = new PageInfo();
        pageInfo.startCode = new MpCode(0, 0, (byte) 5);
        pageInfo.pageNum = 1;
        pageInfo.xCodeNum = 5;
        pageInfo.yCodeNum = 6;
        pageInfo.matrixGap = (byte) 0;
        pageInfo.matrixSize = (byte) 5;
        pageInfo.margin = new int[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, };

        // test invalid checkInPage
        assertTrue(null == pageInfo.checkInPage(new MpCode(30, 0, (byte) 5)));
        assertTrue(null == pageInfo.checkInPage(new MpCode(0, 0, (byte) 6)));
        // test valid checkInPage
        final MpCode code_1_2 = new MpCode(11, 0, (byte) 5);
        final int[] xy_1_2 = pageInfo.checkInPage(code_1_2);
        assertEquals(1, xy_1_2[0]);
        assertEquals(2, xy_1_2[1]);
        final float prx = 100.1f;
        final float pry = 200.2f;
        final MpCode.Point point_1_2 = pageInfo.crtc(code_1_2, prx, pry);
        assertEquals("MPPoint_1_469_776", point_1_2.toString());
    }
}

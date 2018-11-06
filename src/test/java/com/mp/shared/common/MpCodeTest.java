package com.mp.shared.common;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by feng on 3/9/17.
 *
 * 测试对比，parse
 * TODO 测试range
 */

public final class MpCodeTest {
    @Test
    public void testMpCode() {
        final String[][] testData = {
            // 5X5
            {"0000000000000000", "0", "0", "5"},
            {"0000000000000001", "1", "0", "5"},
            {"3333333333333333", "4294967295", "0", "5"},
            // 6X6
            {"0000000000000000000000000", "0", "0", "6"},
            {"0000000000000000000000001", "1", "0", "6"},
            {"3333333333333333333333333", "1125899906842623", "0", "6"},
            // 7X7
            {"000000000000000000000000000000000000", "0", "0", "7"},
            {"000000010000000000000000000000000001", "1", "1", "7"},
            {"333333333333333333333333333333333333", "72057594037927935", "65535", "7"},
            // 8X8
            {"0000000000000000000000000000000000000000000000000", "0", "0", "8"},
            {"0000000000000000000010000000000000000000000000001", "1", "1", "8"},
            {"3333333333333333333333333333333333333333333333333", "72057594037927935", "4398046511103", "8"},
        };
        final MpCode[] codes = new MpCode[testData.length];
        for (int idx = 0; idx < codes.length; ++idx) {
            //System.out.println("idx:"+idx);
            final String[] test = testData[idx];
            final MpCode code1 = MpCode.fromRadix4Str(test[0]);
            final MpCode code2 = new MpCode(Long.parseLong(test[1]),
                    Long.parseLong(test[2]), Byte.parseByte(test[3]));
            assertEquals(0, code1.compareTo(code2));
            assertEquals(test[0], code1.toRadix4Leading0s());
            codes[idx] = code1;
            for (int id2 = 0; id2 < idx; ++id2) {
                assertTrue(codes[idx].compareTo(codes[id2]) > 0);
                assertTrue(codes[id2].compareTo(codes[idx]) < 0);
            }
        }
    }
}

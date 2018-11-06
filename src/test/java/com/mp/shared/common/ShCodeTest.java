package com.mp.shared.common;

import org.junit.Test;
import static org.junit.Assert.*;
/**
 * Created by feng on 3/6/17.
 *
 * pure junit test
 * 因为这些定义可能会需要和后台共享
 *
 * 测试 toString fromString isEqual isIdentical
 */

public final class ShCodeTest {

    @Test
    public void testShCode() {
        // good parse
        final String[][] testData = { // code0, code1, isEqual, isIdentical
                // SH码，code和subType一样，byteVal有区别
                {"SH_60113_0", "SH_60113_0", "true", "true"},
                // SH码 code不一样，subType一样，不相等
                {"SH_60113_0", "SH_60114_0", "false", "false"},
                // SH OID3 code一样，subType不一样，相等
                {"SH_25017_1", "SH_25017_16", "true", "false"},
                // SH OID3 code不一样，不相等
                {"SH_25017_1", "SH_25018_16", "false", "false"},
        };
        for (final String[] test: testData) {
            final ShCode shCode0 = ShCode.fromString(test[0]);
            final ShCode shCode1 = ShCode.fromString(test[1]);
            // test toString fromString and isEqual and isIdentical
            assertEquals(test[0], shCode0.toString());
            assertEquals(test[1], shCode1.toString());
            assertEquals(shCode0.isEqual(shCode1), Boolean.parseBoolean(test[2]));
            assertEquals(shCode0.isIdentical(shCode1), Boolean.parseBoolean(test[3]));
            // test toCode, fromCode
            assertTrue(ShCode.fromCode(shCode0.toCode()).isIdentical(shCode0));
            assertTrue(ShCode.fromCode(shCode1.toCode()).isIdentical(shCode1));
        }
        // test in range
        final String[][] testRanges = { // startCode, endCode, subType, shCode, checkInRange
                // in range same subtype
                {"25017", "25018", "1", "SH_25017_1", "true"},
                {"25017", "25018", "1", "SH_25018_1", "true"},
                // in range oid3
                {"25017", "25018", "1", "SH_25017_16", "true"},
                {"25017", "25018", "16", "SH_25017_1", "true"},
                {"25017", "25018", "16", "SH_25017_2", "true"},
                {"25017", "25018", "16", "SH_25017_16", "true"},
                // not in range subtype
                {"25017", "25018", "1", "SH_25017_2", "false"},
                // not in range code
                {"25017", "25018", "1", "SH_25016_1", "false"},
                {"25017", "25018", "1", "SH_25019_1", "false"},
        };
        for (final String[] test: testRanges) {
            final ShCode.Range range = new ShCode.Range(
                    Integer.parseInt(test[0]), Integer.parseInt(test[1]), Integer.parseInt(test[2]));
            final ShCode shCode = ShCode.fromString(test[3]);
            assertEquals(Boolean.parseBoolean(test[4]), range.checkInRange(shCode));
            assertEquals(false, range.checkInRange(null));
        }
        // bad parse
        final String[] baseParses = {
                "MP_12345_67", // wrong prefix
                "SH_12345", // missing subType
                "SH_12345_1_123456789", // too many fields
                "SH_abc_p", // random wrong thing
        };
        for (final String bad: baseParses) {
            assertEquals(ShCode.fromString(bad), null);
        }
        // incompatible Code
        final Code nonShCode = new Code();
        assertEquals(null, ShCode.fromCode(nonShCode));

        // test String.split effect
        //final String[] parts = "_________".split("___", 3);
        //System.out.println("len:"+parts.length+"-->"+parts[0]+":"+parts[1]+":"+parts[2]);
        // output is: len:3-->::___

    }
}

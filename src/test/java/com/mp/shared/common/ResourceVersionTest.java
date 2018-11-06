package com.mp.shared.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by feng on 3/6/17.
 *
 * pure junit test
 * 因为这些定义可能会需要和后台共享
 *
 * 测试 toString fromString isEqual
 */

public final class ResourceVersionTest {
    @Test
    public void testResourceVersion() {
        // good parses
        final String[][] testData = { // version1, version2, compareTo result
                // equal
                {"ResVer_1_1", "ResVer_1_1", "0"},
                // version newer
                {"ResVer_1_2", "ResVer_1_1", "1"},
                // version newer even updateTime older
                {"ResVer_1_2", "ResVer_2_1", "1"},
                // version same, updateTime newer
                {"ResVer_2_1", "ResVer_1_1", "1"},
                // version older, even updateTime newer
                {"ResVer_2_1", "ResVer_1_2", "-1"},
                // version same, updateTime older
                {"ResVer_2_1", "ResVer_3_1", "-1"},
        };
        final ResourceVersion unique = new ResourceVersion(1, 0);
        for (final String[] test: testData) {
            final ResourceVersion rv1 = ResourceVersion.fromString(test[0]);
            final ResourceVersion rv2 = ResourceVersion.fromString(test[1]);
            final int compareTo = Integer.parseInt(test[2]);
            assertEquals(compareTo, rv1.compareTo(rv2));
            assertTrue(unique.compareTo(rv1) != 0);
        }
        // bad parses
        final String[] badParses = {
                "aaaa", "ResVer_", "ResVer_1", "ResVer_abc_dev",
        };
        for (final String bad: badParses) {
            assertEquals(null, ResourceVersion.fromString(bad));
        }
    }
}

package com.mp.shared.common;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by feng on 3/6/17.
 *
 * pure junit test
 * 因为这些定义可能会需要和后台共享
 *
 * 测试 Area.isIn,
 */

public final class HotAreaTest {
    @Test
    public void testArea() {
        final HotArea.Area area1 = new HotArea.Area(1,
                new int[][] {{10, 10, 30, 40}});
        final HotArea.Area area2 = new HotArea.Area(1,
                new int[][] {
                        {10, 10, 30, 40},
                        {5, 5, 9, 9}, {5, 41, 9, 50},
                        {31, 20, 40, 40}
                });
        final String[][] testData = {
                // pageNum, x, y, in area1, in area2
                // in area1, in area2
                {"1", "10", "10", "true", "true"},
                // not in area1, in area2
                {"1", "9", "9", "false", "true"},
                {"1", "6", "42", "false", "true"},
                {"1", "32", "22", "false", "true"},
                // not int area1, not in area2
                {"1", "6", "40", "false", "false"},
        };
        for (final String[] test: testData) {
            final int pageNum = Integer.parseInt(test[0]);
            final float x = Float.parseFloat(test[1]);
            final float y = Float.parseFloat(test[2]);
            final boolean isInArea1 = Boolean.parseBoolean(test[3]);
            final boolean isInArea2 = Boolean.parseBoolean(test[4]);
            assertEquals(isInArea1, area1.hasIn(pageNum, x, y));
            assertEquals(isInArea2, area2.hasIn(pageNum, x, y));
            assertFalse(area1.hasIn(100, x, y));
            assertFalse(area2.hasIn(100, x, y));
        }
    }
}

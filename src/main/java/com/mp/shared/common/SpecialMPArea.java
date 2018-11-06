package com.mp.shared.common;

import java.io.Serializable;

public class SpecialMPArea<T> implements Serializable {
    private static final long serialVersionUID = 5774558631552764324L;
    public HotArea.Area area;
    public T type;

    public SpecialMPArea(int page, int left, int top, int right, int bottom, T type) {
        super();
        int[][] rects = { { left, top, right, bottom } };
        area = new HotArea.Area(page, rects);
        this.type = type;
    }

    public static <T> SpecialMPArea<T> getSpecialMPArea(QuickCodeInfo quickCodeInfo, SpecialMPArea<T>[] wifiArray) {
        if (quickCodeInfo == null || !quickCodeInfo.isValid() || quickCodeInfo.pageInfo == null
            || !quickCodeInfo.mayComputeMp()) {
            return null;
        }
        for (SpecialMPArea<T> test : wifiArray) {
            if (test.area.hasIn(quickCodeInfo.code.mpPoint)) {
                return test;
            }
        }
        return null;
    }

    public static float getXPix(int width, int x) {
        return width / 1000 * x;
    }

    public static float getYPix(int height, int y) {
        return height / 1000 * y;
    }
}

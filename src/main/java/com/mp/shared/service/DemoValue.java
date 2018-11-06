package com.mp.shared.service;

import com.mp.shared.common.HotArea;
import com.mp.shared.common.QuickCodeInfo;

/**
 * 销售流程演示卡热区信息等
 * Created by zsc on 2017/8/14.
 */

public class DemoValue {
    public static final String DEMO_BOOK_ID = "ff8080815dc4de93015dcb3ea0ed0055";
    public static final Demo[] DEMO_ARRAY = getDemoArray();

    public enum Type {
        DOUDOU_DEMO,
        SPEECH_DEMO
    }

    public static Demo getDemo(QuickCodeInfo quickCodeInfo) {
        if (quickCodeInfo == null || !quickCodeInfo.isValid() || quickCodeInfo.pageInfo == null
                || !quickCodeInfo.mayComputeMp()) {
            return null;
        }
        for (Demo demo : DEMO_ARRAY) {
            if (demo.area.hasIn(quickCodeInfo.code.mpPoint)) {
                return demo;
            }
        }
        return null;
    }

    public static class Demo {
        public HotArea.Area area;
        public DemoValue.Type type;

        public Demo(int page, int left, int top, int right, int bottom, DemoValue.Type type) {
            super();
            int[][] rects = {{left, top, right, bottom}};
            area = new HotArea.Area(page, rects);
            this.type = type;
        }
    }

    public static Demo[] getDemoArray() {
        Demo[] demoArray = new Demo[2];
        demoArray[0] = new Demo(1, 391, 786, 490, 847, Type.SPEECH_DEMO);
        demoArray[1] = new Demo(1, 68, 383, 326, 611, Type.DOUDOU_DEMO);
        return demoArray;
    }
}

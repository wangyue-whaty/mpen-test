package com.mp.shared.service;

import com.mp.shared.common.HotArea;
import com.mp.shared.common.QuickCodeInfo;

/**
 * Created by zyt on 5/3/17.
 *
 * 出厂测试
 */
public final class QualityInspection {
    public static final Test[] testArray = getTestArray();
    
    public static final String PAGEINFO_ID = "ff8080815b8f9180015bcd01c13d0fc7";

    private static final String TAG = "FactoryTest";
    
    // 像素单位
    public static final float WIDTH_PIX = 9920;
    
    public static final float HEIGHT_PIX = 14030;

    /**
     * 测试类型 
     * IDENTIFICATION_TEST 识别测试; 
     * GET_WIFI_TEST 获取WIFI列表测试 
     * SET_WIFI_TEST 设置WIFI测试 
     * SCREEN_TEST LED显示屏测试 
     * LIGHT_TEST LED指示灯测试 
     * BUTTON_TEST 按键测试
     * GYRO_TEST 陀螺仪测试 
     * SPEAKER_TEST 喇叭测试 
     * HEADSET_TEST 耳机测试 
     * BLUETOOTH_TEST 蓝牙测试 
     * START_RECORDING_TEST 开始录音 
     * STOP_RECORDING_TEST 停止录音 
     * PLAY_RECORDING_TEST 播放录音 
     * STOP_PLAY_TEST 停止播放 
     * REMOVE_RECORDING_TEST 删除录音 
     * PASS 通过 
     * NOT_PASS 未通过 
     * ACTIVATION_TEST 激活 
     * NIB_CALIBRATION_TEST 笔尖校准 
     * MUSIC_CYCLE_TEST 音乐循环测试
     * REMOVE_TEMP_FILE 删除临时文件 
     * REGISTERED 出厂注册 
     * CALIBRATION_CIRCLE_ONE 校准圆1 
     * CALIBRATION_CIRCLE_TWO 校准圆2 
     * CALIBRATION_CIRCLE_THREE 校准圆3 
     * CALIBRATION_CIRCLE_FOUR 校准圆4
     *
     */
    public enum Type {
            IDENTIFICATION_TEST("识别测试", 1),
            GET_WIFI_TEST("获取WiFi列表测试", 2),
            SET_WIFI_TEST("设置WiFi测试", 3),
            SCREEN_TEST("LED显示屏测试", 4),
            LIGHT_TEST("LED指示灯测试", 5),
            BUTTON_TEST("按键测试", 6),
            GYRO_TEST("陀螺仪测试", 7),
            SPEAKER_TEST("喇叭测试", 8),
            HEADSET_TEST("耳机测试", 9),
            BLUETOOTH_TEST("蓝牙测试", 10),
            START_RECORDING_TEST("开始录音", 11),
            STOP_RECORDING_TEST("停止录音", 12),
            PLAY_RECORDING_TEST("录音播放", 13),
            STOP_PLAY_TEST("停止播放", 14),
            REMOVE_RECORDING_TEST("删除录音", 15),
            PASS("通过", 16),
            NOT_PASS("未通过", 17),
            ACTIVATION_TEST("激活", 18),
            NIB_CALIBRATION_TEST("笔尖校准", 19),
            MUSIC_CYCLE_TEST("音乐循环测试", 20),
            REMOVE_TEMP_FILE("删除临时文件", 21),
            REGISTERED("出厂注册", 22),
            CALIBRATION_CIRCLE_ONE("校准点1", 23),
            CALIBRATION_CIRCLE_TWO("校准点2", 24),
            CALIBRATION_CIRCLE_THREE("校准点3", 25),
            CALIBRATION_CIRCLE_FOUR("校准点4", 26);

            String typeMsg;
            int code;

            Type(String type, int code) {
                this.typeMsg = type;
                this.code = code;
            }

            public String getTypeInfo(Type type) {
                return type.typeMsg;
            }
        }

    /**
     * 测试内容bean
     *
     */
    public static class Test {
        public HotArea.Area area;
        public Type type;

        public Test(int page, int left, int top, int right, int bottom, Type type) {
            super();
            int[][] rects = { { left, top, right, bottom } };
            area = new HotArea.Area(page, rects);
            this.type = type;
        }
    }
    
    /**
     * 获取测试数组.
     */
    private static Test[] getTestArray() {
        Test[] testArray = new Test[26];
        testArray[0] = new Test(1, 93, 48, 369, 88, Type.IDENTIFICATION_TEST);
        testArray[1] = new Test(1, 594, 48, 870, 88, Type.GET_WIFI_TEST);
        testArray[2] = new Test(1, 594, 122, 870, 163, Type.SET_WIFI_TEST);
        testArray[3] = new Test(1, 93, 122, 369, 163, Type.SCREEN_TEST);
        testArray[4] = new Test(1, 93, 205, 369, 246, Type.LIGHT_TEST);
        testArray[5] = new Test(1, 594, 205, 870, 246, Type.BUTTON_TEST);
        testArray[6] = new Test(1, 594, 292, 870, 332, Type.GYRO_TEST);
        testArray[7] = new Test(1, 93, 292, 369, 332, Type.SPEAKER_TEST);
        testArray[8] = new Test(1, 93, 375, 369, 415, Type.HEADSET_TEST);
        testArray[9] = new Test(1, 594, 375, 870, 415, Type.BLUETOOTH_TEST);
        testArray[10] = new Test(1, 93, 459, 308, 500, Type.START_RECORDING_TEST);
        testArray[11] = new Test(1, 380, 460, 617, 501, Type.STOP_RECORDING_TEST);
        testArray[12] = new Test(1, 664, 459, 901, 500, Type.PLAY_RECORDING_TEST);
        testArray[13] = new Test(1, 664, 521, 901, 562, Type.STOP_PLAY_TEST);
        testArray[14] = new Test(1, 381, 522, 618, 563, Type.REMOVE_RECORDING_TEST);
        testArray[15] = new Test(1, 150, 623, 426, 663, Type.PASS);
        testArray[16] = new Test(1, 580, 623, 856, 663, Type.NOT_PASS);
        testArray[17] = new Test(1, 150, 696, 426, 736, Type.ACTIVATION_TEST);
        testArray[18] = new Test(1, 580, 696, 856, 736, Type.NIB_CALIBRATION_TEST);
        testArray[19] = new Test(1, 150, 771, 426, 812, Type.MUSIC_CYCLE_TEST);
        testArray[20] = new Test(1, 580, 771, 856, 812, Type.REMOVE_TEMP_FILE);
        testArray[21] = new Test(1, 150, 846, 426, 887, Type.REGISTERED);
        testArray[22] = new Test(1, 57, 621, 118, 665, Type.CALIBRATION_CIRCLE_ONE);
        testArray[23] = new Test(1, 888, 621, 949, 665, Type.CALIBRATION_CIRCLE_TWO);
        testArray[24] = new Test(1, 56, 770, 118, 813, Type.CALIBRATION_CIRCLE_THREE);
        testArray[25] = new Test(1, 888, 770, 949, 813, Type.CALIBRATION_CIRCLE_FOUR);
        return testArray;
    }

    public static Test getTestBean(QuickCodeInfo quickCodeInfo) {
        if (quickCodeInfo == null || !quickCodeInfo.isValid() || quickCodeInfo.pageInfo == null
            || !quickCodeInfo.mayComputeMp()) {
            return null;
        }
        for (Test test : testArray) {
            if (test.area.hasIn(quickCodeInfo.code.mpPoint)) {
                return test;
            }
        }
        return null;
    }
    
    public static float getXPix(int x){
        return WIDTH_PIX / 1000 * x;
    }
    
    public static float getYPix(int y){
        return HEIGHT_PIX / 1000 * y;
    }

    /**
     * regular ones
     */
    public static final float[][] circleCenters = {
            {866, 9022}, {9110, 9022}, {862, 11102}, {9108, 11102},
    };
    /**
     * the one currently on Feng's hands
     */
    public static final float[][] circleCentersForFeng = {
            {838, 8968}, {9082, 8968}, {834, 11048}, {9080, 11048},
    };
}


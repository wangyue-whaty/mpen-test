package com.mp.shared.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.mp.shared.common.Code;
import com.mp.shared.common.MpCode;
import com.mp.shared.common.PageInfo;
import com.mp.shared.common.QuickCodeInfo;


public final class QualityInspectionTest {
    QuickCodeInfo quickCodeInfo;

    @Before
    public void setQuickCodeInfo() {
        quickCodeInfo = new QuickCodeInfo();
        quickCodeInfo.pageInfo = new PageInfo();
        quickCodeInfo.pageInfo.pageNum = 1;
        quickCodeInfo.pageInfo.startCode = new MpCode(2182151525L, 0, new Byte("5"));
        quickCodeInfo.pageInfo.xCodeNum = 124;
        quickCodeInfo.pageInfo.yCodeNum = 175;
        quickCodeInfo.pageInfo.matrixGap = 0;
        quickCodeInfo.pageInfo.margin = new int[4];
        quickCodeInfo.pageInfo.margin[0] = 0;
        quickCodeInfo.pageInfo.margin[1] = 0;
        quickCodeInfo.pageInfo.margin[2] = 0;
        quickCodeInfo.pageInfo.margin[3] = 30;
        quickCodeInfo.pageInfo.matrixSize = 5;
        quickCodeInfo.pageInfo.dotSize = 3;
        quickCodeInfo.pageInfo.dotShift = 3;
        quickCodeInfo.code = new Code();
        quickCodeInfo.code.type = Code.Type.MP;
        quickCodeInfo.code.prx = 1;
        quickCodeInfo.code.pry = 1;
    }

    @Test
    public void getTestBeanTest() {
        long[] code = { 2182152655L, 2182152717L, 2182154329L, 2182154267L, 2182156003L, 2182156065L, 2182158049L,
            2182157987L, 2182159723L, 2182159785L, 2182161583L, 2182161619L, 2182161654L, 2182163018L, 2182162983L,
            2182165186L, 2182165239L, 2182166803L, 2182166856L, 2182168415L, 2182168468L, 2182170151L, 2182165051L,
            2182165154L, 2182168403L, 2182168506L };
        QualityInspection.Type[] type = { QualityInspection.Type.IDENTIFICATION_TEST,
            QualityInspection.Type.GET_WIFI_TEST, QualityInspection.Type.SET_WIFI_TEST,
            QualityInspection.Type.SCREEN_TEST, QualityInspection.Type.LIGHT_TEST, QualityInspection.Type.BUTTON_TEST,
            QualityInspection.Type.GYRO_TEST, QualityInspection.Type.SPEAKER_TEST, QualityInspection.Type.HEADSET_TEST,
            QualityInspection.Type.BLUETOOTH_TEST, QualityInspection.Type.START_RECORDING_TEST,
            QualityInspection.Type.STOP_RECORDING_TEST, QualityInspection.Type.PLAY_RECORDING_TEST,
            QualityInspection.Type.STOP_PLAY_TEST, QualityInspection.Type.REMOVE_RECORDING_TEST,
            QualityInspection.Type.PASS, QualityInspection.Type.NOT_PASS, QualityInspection.Type.ACTIVATION_TEST,
            QualityInspection.Type.NIB_CALIBRATION_TEST, QualityInspection.Type.MUSIC_CYCLE_TEST,
            QualityInspection.Type.REMOVE_TEMP_FILE, QualityInspection.Type.REGISTERED,
            QualityInspection.Type.CALIBRATION_CIRCLE_ONE, QualityInspection.Type.CALIBRATION_CIRCLE_TWO,
            QualityInspection.Type.CALIBRATION_CIRCLE_THREE, QualityInspection.Type.CALIBRATION_CIRCLE_FOUR };
        QualityInspection.Test testBean = null;
        for (int i = 0; i < code.length; i++) {
            quickCodeInfo.code.mpCode = new MpCode(code[i], 0, new Byte("5"));
            quickCodeInfo.code.mpPoint = null;
            testBean = QualityInspection.getTestBean(quickCodeInfo);
            assertEquals(testBean.type, type[i]);
        }
    }
}

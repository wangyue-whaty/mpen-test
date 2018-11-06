package com.mp.shared.record;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by ZSC on 2017/11/23.
 */

public class PenStatusRecordTest {
    @Test
    public void testTaskRecord() {
        // 主要测试顺序和null的可解析性
        final PenStatusRecord record = new PenStatusRecord();
        record.battery = 100;
        record.realtime = 1511416494L;
        record.upTime = 1511416494L;
        record.location = "北京市";
        record.bookListVersion = "1511416494";
        record.softVersion = "3.72";
        record.osVersion = "4.2";

        testOneRecord(record);
    }

    private void testOneRecord(PenStatusRecord record) {
        final ActionRecord ar = record.toActionRecord();
        final Gson gson = new Gson();
        assertTrue(ar != null);
        final String asStr = gson.toJson(ar);
        final ActionRecord ar2 = gson.fromJson(asStr, ActionRecord.class);
        assertEquals(ActionRecord.Type.PEN_STATUS_INFO, ar2.type);
        assertEquals(7, ar2.data.size());

        final PenStatusRecord r2 = PenStatusRecord.fromActionRecord(ar2);
        Assert.assertEquals(gson.toJson(record), gson.toJson(r2));
    }
}

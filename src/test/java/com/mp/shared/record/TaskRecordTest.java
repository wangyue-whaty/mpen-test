package com.mp.shared.record;

import com.google.gson.Gson;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by feng on 3/17/17.
 */

public final class TaskRecordTest {

    private Gson gson = new Gson();
    @Test
    public void testTaskRecord() {
        // 主要测试顺序和null的可解析性
        final TaskRecord record = new TaskRecord();
        record.id = 1;
        record.name = ActionRecord.Subtype.CaptureImgDecode;
        record.prevTaskId = 2;
        record.parentTaskId = 3;
        record.isGroup = false;
        record.createdRealTime = 5;
        record.duration = 6;
        record.isSuccessful = true;
        record.isCancelled = false;
        record.errorNo = 0;
        record.extra = "extra";

        testOneRecord(record);
        // change the name
        record.name = ActionRecord.Subtype.PlayAudio;
        testOneRecord(record);
        record.prevTaskId = 22;
        testOneRecord(record);
        record.parentTaskId = 33;
        testOneRecord(record);
        record.isGroup = true;
        testOneRecord(record);
        record.createdRealTime = 55;
        testOneRecord(record);
        record.duration = 66;
        testOneRecord(record);
        record.isSuccessful = false;
        testOneRecord(record);
        record.isCancelled = true;
        testOneRecord(record);
        record.errorNo = 1;
        testOneRecord(record);
        record.extra = "extra2";
        testOneRecord(record);
        record.extra = null;
        testOneRecord(record);
    }

    private void testOneRecord(TaskRecord record) {
        final ActionRecord ar = record.toActionRecord();
        assertTrue(ar != null);
        final String asStr = gson.toJson(ar);
        final ActionRecord ar2 = gson.fromJson(asStr, ActionRecord.class);
        assertEquals(record.name, ar2.subType);
        assertEquals(ActionRecord.Type.TASK, ar2.type);
        assertEquals(10, ar2.data.size());

        final TaskRecord r2 = TaskRecord.fromActionRecord(ar2);
        assertTrue(record.isEqual(r2));
        r2.extra = ".....";
        assertFalse(record.isEqual(r2));
        // test parse failure
        ar2.version = ar2.version + 1;
        assertTrue(null == TaskRecord.fromActionRecord(ar2));
        ar2.version = ar2.version - 1;
        assertTrue(null != TaskRecord.fromActionRecord(ar2));
    }
}

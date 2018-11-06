package com.mp.shared.record;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mp.shared.common.IsEqual;
import com.mp.shared.common.Utils;

/**
 * Created by feng on 2/25/17.
 *
 * 每个Task的记录
 */

public final class TaskRecord implements ActionRecord.Sub, IsEqual<TaskRecord> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRecord.class);
    
    static final int CURRENT_VER = 1;
    public int id;
    public ActionRecord.Subtype name;

    public int prevTaskId;

    public int parentTaskId;

    public boolean isGroup;

    public long createdRealTime;
    public long duration;

    public boolean isSuccessful;
    public boolean isCancelled;
    public int errorNo;

    public String extra;
    
    @Override
    public boolean isEqual(TaskRecord record) {
        if (record == null) {
            return false;
        } else if (record == this) {
            return true;
        }
        return id == record.id
                && Utils.equals(name, record.name) && prevTaskId == record.prevTaskId
                && parentTaskId == record.parentTaskId && isGroup == record.isGroup
                && createdRealTime == record.createdRealTime && duration == record.duration
                && isSuccessful == record.isSuccessful && isCancelled == record.isCancelled
                && errorNo == record.errorNo && Utils.equals(extra, record.extra);
    }

    /**
     * @return  生成统一包装
     */
    @Override
    public ActionRecord toActionRecord() {
        final ActionRecord record = new ActionRecord();
        record.type = ActionRecord.Type.TASK;
        record.subType = name;
        record.version = CURRENT_VER;
        record.data = new ArrayList(10);
        if (toCollection(record.data)) {
            return record;
        } else {
            return null;
        }
    }

    /**
     * @return 检查record.type/subType/version，返回TaskRecord or null
     */
    public static TaskRecord fromActionRecord(ActionRecord record) {
        if (record == null || ActionRecord.Type.TASK != record.type) {
            return null;
        } else if (record.version > CURRENT_VER) {
            return null;  // because version can't work
        }
        final TaskRecord taskRecord = new TaskRecord();
        try {
            taskRecord.id = record.getAsInt(0, 0);
            taskRecord.name = record.subType;
            taskRecord.prevTaskId = record.getAsInt(1, 0);
            taskRecord.parentTaskId = record.getAsInt(2, 0);
            taskRecord.isGroup = record.getAsBoolean(3, false);
            taskRecord.createdRealTime = record.getAsLong(4, 0);
            taskRecord.duration = record.getAsLong(5, 0);
            taskRecord.isSuccessful = record.getAsBoolean(6, false);
            taskRecord.isCancelled = record.getAsBoolean(7, false);
            taskRecord.errorNo = record.getAsInt(8, 0);
            taskRecord.extra = record.getAsString(9);
        } catch (Exception e) {
            LOGGER.error("record.data--"+record.data+"--解析data数组错误--"+e.getMessage());
            return null;
        }
        return taskRecord;
    }

    /**
     * TaskRecord will add its fields into collection.
     * *** Order to add is important, keep same as order of retrieving ***
     * @param collection the collection to add to
     * @return true if everything ok
     */
    private boolean toCollection(ArrayList<Object> collection) {
        // below are task record specific
        boolean result = true;
        result &= collection.add(id);
        //result &= collection.add(name); // already in subType
        result &= collection.add(prevTaskId);
        result &= collection.add(parentTaskId);
        result &= collection.add(isGroup);
        result &= collection.add(createdRealTime);
        result &= collection.add(duration);
        result &= collection.add(isSuccessful);
        result &= collection.add(isCancelled);
        result &= collection.add(errorNo);
        result &= collection.add(extra);
        return result;
    }
    
}

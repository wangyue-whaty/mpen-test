package com.mp.shared.record;

import com.mp.shared.common.IsEqual;
import com.mp.shared.common.Utils;

import java.util.ArrayList;

/**
 * Created by ZSC on 2017/11/15.
 */

public final class PenStatusRecord implements ActionRecord.Sub, IsEqual<PenStatusRecord> {

    public int battery;            // 电池电量
    public long realtime;          // 当前时间
    public long upTime;            // 系统运行时间
    public String location;        // 地理位置信息
    public String bookListVersion; // booklist版本
    public String softVersion;     // 软件版本
    public String osVersion;       // 系统版本

    @Override
    public boolean isEqual(PenStatusRecord record) {
        if (record == null) {
            return false;
        } else if (record == this) {
            return true;
        }
        return Utils.equals(softVersion, record.softVersion)
                && realtime == record.realtime && upTime == record.upTime
                && Utils.equals(location, record.location) && battery == record.battery
                && Utils.equals(osVersion, record.osVersion) && Utils.equals(bookListVersion, record.bookListVersion);
    }

    @Override
    public ActionRecord toActionRecord() {
        final ActionRecord record = new ActionRecord();
        record.type = ActionRecord.Type.PEN_STATUS_INFO;
        record.data = new ArrayList(7);
        if (addRecordData(record.data)) {
            return record;
        }
        return null;
    }

    private boolean addRecordData(ArrayList<Object> collection) {
        boolean result = true;
        result &= collection.add(battery);
        result &= collection.add(realtime);
        result &= collection.add(upTime);
        result &= collection.add(location);
        result &= collection.add(bookListVersion);
        result &= collection.add(softVersion);
        result &= collection.add(osVersion);
        return result;
    }

    /**
     * 从 collection 还原到具体数据项
     * @param record
     * @return
     */
    public static PenStatusRecord fromActionRecord(ActionRecord record) {
        final PenStatusRecord pr = new PenStatusRecord();
        try {
            pr.battery = record.getAsInt(0, 0);
            pr.realtime = record.getAsLong(1, 0);
            pr.upTime = record.getAsLong(2, 0);
            pr.location = record.getAsString(3);
            pr.bookListVersion = record.getAsString(4);
            pr.softVersion = record.getAsString(5);
            pr.osVersion = record.getAsString(6);
        } catch (Exception e) {
            System.out.println("PenStatusRecord "+e.toString());
            return null;
        }
        return pr;
    }
}

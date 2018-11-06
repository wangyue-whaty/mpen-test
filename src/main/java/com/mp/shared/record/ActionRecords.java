package com.mp.shared.record;

import java.util.List;

import com.mp.shared.common.Utils;
import com.mp.shared.record.ActionRecord;

/**
 * Created by zyt .
 * 
 * 向服务器提交记录统一参数bean.
 */
public class ActionRecords implements RecordBase {
    private String uploadUuid;
    private Integer numRecords;
    private List<ActionRecord> records;
    private String penId;

    public String getPenId() {
        return penId;
    }

    public void setPenId(String penId) {
        this.penId = penId;
    }

    public String getUploadUuid() {
        return uploadUuid;
    }

    public Integer getNumRecords() {
        return numRecords;
    }

    public List<ActionRecord> getRecords() {
        return records;
    }

    public void setUploadUuid(String uploadUuid) {
        this.uploadUuid = uploadUuid;
    }

    public void setNumRecords(Integer numRecords) {
        this.numRecords = numRecords;
    }

    public void setRecords(List<ActionRecord> records) {
        this.records = records;
    }

    /**
     * @return 是不是有效记录
     */
    @Override
    public boolean isValid() {
        return uploadUuid != null && numRecords > 0 && records != null;
    }
}

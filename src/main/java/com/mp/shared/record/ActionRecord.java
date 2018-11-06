package com.mp.shared.record;

import java.util.ArrayList;

/**
 * Created by feng on 2/26/17.
 *
 * 用户行为或者系统动作记录
 *
 * 这里统一记录格式；具体数据，需要每个具体的Sub class（比如TaskRecord）来解析
 * TODO 使用一个更正规的 factory
 */

public final class ActionRecord {
    /**
     * interface 生成统一ActionRecord
     */
    public interface Sub {
        /**
         * @return 生成统一ActionRecord，可能返回null
         */
        ActionRecord toActionRecord();
    }

    public Type type;
    public Subtype subType;

    /**
     * Actual record class (i.e TaskRecord) will read/put data here
     * *** Order to add is important, keep same as order of retrieving ***
     */
    public ArrayList<Object> data;
    public int version;  // 具体数据版本
    
    public enum Type {
        TASK, PEN_STATUS_INFO
    }
    
    /**
     *  FetchCodeInfo：码解析资源   
     *  ReadEvalGroup：口语评测   
     *  CaptureImgDecode：图像解码
     *  PlayAudio：点读
     *  GameTask：游戏
     *  ReadAfterGroup：跟读对比
     *
     */
    public enum Subtype {
        FetchCodeInfo, ReadEvalGroup, CaptureImgDecode, PlayAudio, GameTask, ReadAfterGroup
    }

    /**
     * 尝试解析成 Int
     * 可能会抛异常
     */
    public int getAsInt(int idx, int defaultValue) {
        final Object obj = data.get(idx);
        if (obj == null) {
            return defaultValue;
        } else {
            return ((Double) obj).intValue();
        }
    }
    /**
     * 尝试解析成 Long
     * 可能会抛异常
     */
    public long getAsLong(int idx, long defaultValue) {
        final Object obj = data.get(idx);
        if (obj == null) {
            return defaultValue;
        } else {
            return ((Double) obj).longValue();
        }
    }
    /**
     * 尝试解析成 Boolean
     * 可能会抛异常
     */
    public boolean getAsBoolean(int idx, boolean defaultValue) {
        final Object obj = data.get(idx);
        if (obj == null) {
            return defaultValue;
        } else {
            return (Boolean) obj;
        }
    }
    /**
     * 尝试解析成 String
     * 可能会抛异常
     */
    public String getAsString(int idx) {
        final Object obj = data.get(idx);
        if (obj == null) {
            return null;
        } else {
            return obj.toString();
        }
    }
}

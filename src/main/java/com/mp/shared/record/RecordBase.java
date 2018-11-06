package com.mp.shared.record;

/**
 * Created by feng on 5/7/17.
 *
 * 用于JsonRecordReader的record，提供一个方法来过滤无效记录
 */

public interface RecordBase {
    /**
     * @return 是不是有效记录
     */
    boolean isValid();
}

package com.mp.shared.common;

/**
 * Created by feng on 3/17/17.
 *
 * Override equals 太麻烦，就自己定义这个来比较相等
 */

public interface IsEqual<T> {
    /**
     * check to see if two are equal
     */
    boolean isEqual(T other);
}

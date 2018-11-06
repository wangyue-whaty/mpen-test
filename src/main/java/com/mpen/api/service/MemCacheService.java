/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import com.mpen.api.exception.CacheException;

/**
 * 数据缓存接口.
 *
 * @author kai
 *
 */
public interface MemCacheService {

    /**
     * 根据key获取一个缓存对象.
     *
     * @param key stored key
     * @return if success true else false
     * @throws CacheException CacheException
     */
    <T> T get(String key) throws CacheException;

    /**
     * 缓存一个对象并设置数据的失效时间.
     *
     * @param key stored key
     * @param value stored data
     * @param expiration An expiration time, in seconds. Can be up to 30 days. After 30 days, is treated as a unix
     *        timestamp of an exact date.
     * @return if success true else false
     * @throws CacheException CacheException
     */
    <T> boolean set(String key, T value, int expiration) throws CacheException;


    /**
     * 根据key获取一个缓存对象.
     *
     * @param key stored key
     * @return if success true else false
     * @throws CacheException CacheException
     */
    boolean delete(String key) throws CacheException;
}

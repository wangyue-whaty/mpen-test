/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import com.mpen.api.exception.CacheException;
import com.mpen.api.service.MemCacheService;
import com.whaty.framework.cache.core.model.Cache;
import com.whaty.framework.cache.core.service.impl.DefaultRedisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 数据缓存服务.
 *
 * @author kai
 *
 */
@Component
@PropertySource("classpath:redis.properties")
public class MemCacheServiceImpl extends DefaultRedisService implements MemCacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemCacheServiceImpl.class);

    @Override
    public <T> T get(String key) throws CacheException {
        Cache cache = this.getCache("SpringBoot");
        return (T) cache.get(key);
    }

    @Override
    public <T> boolean set(String key, T value, int expiration) throws CacheException {
        Cache cache = this.getCache("SpringBoot");
        return cache.put(key, value, expiration);
    }

    @Override
    public boolean delete(String key) throws CacheException {
        Cache cache = this.getCache("SpringBoot");
        cache.remove(key);
        return true;
    }

}

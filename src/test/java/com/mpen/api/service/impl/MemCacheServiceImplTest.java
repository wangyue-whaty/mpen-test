/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import java.util.UUID;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mpen.TestBase;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.SsoUser;
import com.mpen.api.exception.CacheException;
import com.mpen.api.service.MemCacheService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MemCacheServiceImplTest extends TestBase {

    @InjectMocks
    @Autowired
    MemCacheService memCacheService;

    @BeforeClass
    public static void setUp() throws Exception {
        MockitoAnnotations.initMocks(MemCacheServiceImplTest.class);
    }

    @Test
    public void testSetObjectToCacheSuccess() throws CacheException {

        SsoUser expectObj = getTestSsoUser();

        String key = UUID.randomUUID().toString();

        memCacheService.set(key, expectObj, Constants.DEFAULT_CACHE_EXPIRATION);

        SsoUser gotObj = memCacheService.<SsoUser>get(key);

        Assert.assertEquals(expectObj.getId(), gotObj.getId());
        Assert.assertEquals(expectObj.getLoginId(), gotObj.getLoginId());

        memCacheService.delete(key);

    }

    @Test
    public void testSetObjectToCacheUseSameKeyWithDifferentTypeSuccess() throws CacheException {

        SsoUser ssoUser = getTestSsoUser();

        DdbPeCustom ddbPeCustom = getTestDdbPeCustom(ssoUser);

        String key = UUID.randomUUID().toString();

        memCacheService.set(key, ssoUser, Constants.DEFAULT_CACHE_EXPIRATION);

        SsoUser gotObj1 = memCacheService.<SsoUser>get(key);

        Assert.assertEquals(ssoUser.getId(), gotObj1.getId());

        memCacheService.set(key, ddbPeCustom, Constants.DEFAULT_CACHE_EXPIRATION);

        DdbPeCustom gotObj2 = memCacheService.<DdbPeCustom>get(key);

        Assert.assertEquals(ddbPeCustom.getId(), gotObj2.getId());

        memCacheService.delete(key);

    }

    @Test
    public void testSetObjectToCacheWithNullValueFailed() throws CacheException {

        SsoUser expectObj = null;
        String key = UUID.randomUUID().toString();
        memCacheService.set(key, expectObj, Constants.DEFAULT_CACHE_EXPIRATION);
        Assert.assertNull(memCacheService.<SsoUser>get(key));
    }

    @Test
    public void testSetObjectToCacheWithExpirtationSuccess() throws CacheException, InterruptedException {

        SsoUser expectObj = getTestSsoUser();

        String key = UUID.randomUUID().toString();

        int expiration = 10;// seconds

        memCacheService.set(key, expectObj, expiration);

        Thread.sleep(12000);

        SsoUser gotObj = memCacheService.<SsoUser>get(key);

        Assert.assertEquals(null, gotObj);
    }

    @Test
    public void testGetObjectFromCacheWithDifferentTypeFailed() throws CacheException, InterruptedException {

        SsoUser expectObj = getTestSsoUser();
        String loginId = expectObj.getLoginId();

        String key = UUID.randomUUID().toString();

        memCacheService.set(key, expectObj, Constants.DEFAULT_CACHE_EXPIRATION);

        SsoUser ssoUser = memCacheService.<SsoUser>get(key);

        Assert.assertEquals(ssoUser.getLoginId(), loginId);
    }

}

/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;


import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mp.shared.common.Page;
import com.mpen.TestBase;
import com.mpen.api.bean.Goods;
import com.mpen.api.bean.GoodsInfo;
import com.mpen.api.service.ShopGoodsService;

/**
 * ShopGoodsServiceImplTest.
 * 
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ShopGoodsServiceImplTest extends TestBase {
    @InjectMocks
    @Autowired
    ShopGoodsService shopGoodsService;

    @BeforeClass
    public static void setUp() throws Exception {
        MockitoAnnotations.initMocks(ShopGoodsServiceImplTest.class);
    }
    
    /**
     * 获取教学资源展示列表测试
     * 展示列表包括:id,name,photo
     * @throws Exception
     */
    @Test
    public void testGetGoodsTeachLinkSuccess() throws Exception {
        Goods goods = getTestGoodsTeachLink();
        final Page<GoodsInfo> bookInfo = shopGoodsService.getGoods(goods);
        List<GoodsInfo> goodsInfos = bookInfo.getItems();
        Assert.assertEquals(bookInfo != null, true);
        for (GoodsInfo goodsInfo : goodsInfos) {
            // 校验id是否存在
            Assert.assertEquals(goodsInfo.getId() != null, true);
            // 校验name是否存在
            Assert.assertEquals(goodsInfo.getName() != null, true);
            // 校验photo是否存在
            Assert.assertEquals(goodsInfo.getPhoto() != null, true);
        }
    }

}

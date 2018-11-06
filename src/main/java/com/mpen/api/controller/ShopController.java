/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mp.shared.common.NetworkResult;
import com.mpen.api.bean.Goods;
import com.mpen.api.bean.GoodsInfo;
import com.mpen.api.common.Constants;
import com.mpen.api.common.RsHelper;
import com.mpen.api.common.Uris;
import com.mpen.api.service.ShopGoodsService;
import com.mpen.api.util.FileUtils;

/**
 * TODO 商城相关API.
 *
 * @author zyt
 *
 */
@RestController
@EnableAsync
@RequestMapping(Uris.V1_SHOPS)
public class ShopController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private ShopGoodsService shopGoodsService;

    /**
     * 获取商城书籍列表接口.
     *
     * @return Result对象
     */
    @GetMapping(Uris.GOODS)
    public @ResponseBody Callable<NetworkResult<Object>> getGoods(final Goods goods, final HttpServletRequest request,
        final HttpServletResponse response) {
        return new Callable<NetworkResult<Object>>() {
            @Override
            public NetworkResult<Object> call() throws Exception {
                switch (goods.getAction()) {
                case Constants.GET_TOP_GOODS:
                    GoodsInfo goodsInfo = new GoodsInfo();
                    final List<GoodsInfo> list = new ArrayList<>();
                    // TODO 临时数据
                    goodsInfo.setId(Constants.ZERO);
                    goodsInfo.setPoster(FileUtils.getFullRequestPath(Constants.POSTER1_PATH));
                    goodsInfo.setLink(Constants.WINDIAN_PATH);
                    list.add(goodsInfo);
                    return RsHelper.success(list);
                case Constants.GET_GOODS:
                    return RsHelper.success(shopGoodsService.getGoods(goods));
                case Constants.GET_GOODS_TEACHLINK:
                    goods.setNeedTeachLink(true);
                    return RsHelper.success(shopGoodsService.getGoods(goods));
                case Constants.GET_BOOKS_PHOTO:
                    return RsHelper.success(shopGoodsService.getBooksPhotoByIds(goods.getBookId()));
                case Constants.GET_GOODS_BY_BOOK_ID:
                    return RsHelper.success(shopGoodsService.getGoodsByBookId(goods.getBookId()));
                case Constants.GET_GOODS_BY_GOOD_ID:
                    // TODO 对老版本兼容，以后去掉
                    return RsHelper.success(shopGoodsService.getGoodsByBookId(goods.getId()));
                default:
                    return RsHelper.error(Constants.NO_MATCHING_METHOD);
                }
            }
        };

    }
}

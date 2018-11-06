package com.mpen.api.service;

import java.util.List;

import com.mp.shared.common.Page;
import com.mpen.api.bean.Goods;
import com.mpen.api.bean.GoodsInfo;
import com.mpen.api.exception.CacheException;

public interface ShopGoodsService {
    /**
     * 获取商城书籍列表.
     * @throws Exception 
     * 
     */
    Page<GoodsInfo> getGoods(Goods goods) throws Exception;

    /**
     * 获取图片列表.
     * 
     */
    List<GoodsInfo> getBooksPhotoByIds(String ids) throws CacheException;

    /**
     * 获取商品.
     * @throws Exception 
     * 
     */
    GoodsInfo getGoodsByBookId(String bookId) throws Exception;
    
}

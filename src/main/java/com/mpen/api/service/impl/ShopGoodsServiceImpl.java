package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mp.shared.common.FullBookInfo;
import com.mp.shared.common.Page;
import com.mp.shared.common.Publisher;
import com.mpen.api.bean.Goods;
import com.mpen.api.bean.GoodsInfo;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Constants.CacheType;
import com.mpen.api.domain.DdbResourceBook;
import com.mpen.api.domain.MobileApp;
import com.mpen.api.exception.CacheException;
import com.mpen.api.service.ResourceBookService;
import com.mpen.api.service.ShopGoodsService;
import com.mpen.api.util.FileUtils;

/**
 * 商品相关服务.
 *
 * @author zyt
 *
 */
@Component
public class ShopGoodsServiceImpl implements ShopGoodsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShopGoodsServiceImpl.class);

    @Autowired
    private ResourceBookService resourceBookService;

    @Override
    public Page<GoodsInfo> getGoods(Goods goods) throws Exception {
        if (goods.getPageNo() == null) {
            goods.setPageNo(Constants.DEFAULT_PAGENO);
            goods.setPageSize(Constants.MAX_PAGESIZE);
        }
        int pageIndex = (goods.getPageNo() - 1) * goods.getPageSize();
        int pageLength = pageIndex + goods.getPageSize();
        final Page<GoodsInfo> page = new Page<GoodsInfo>();
        // 获取筛选过后符合条件book列表
        final List<FullBookInfo> bookInfos = filterBookInfo(goods);
        if (bookInfos == null || bookInfos.size() <= 0) {
            return page;
        }
        final int bookInfoSize = bookInfos.size();
        if (pageIndex >= bookInfoSize) {
            return page;
        }
        if (pageLength > bookInfoSize) {
            pageLength = bookInfoSize;
        }
        // 对筛选结果进行分页处理
        List<GoodsInfo> books = new ArrayList<>();
        for (int i = pageIndex; i < pageLength; i++) {
            final GoodsInfo goodsInfo = new GoodsInfo(bookInfos.get(i));
            goodsInfo.setPhoto(FileUtils.getFullRequestPath(goodsInfo.getPhoto()));
            books.add(goodsInfo);
        }
        page.setItems(books);
        page.setTotalCount(bookInfos.size());
        return page;
    }

    private List<FullBookInfo> filterBookInfo(Goods goods) throws Exception {
        List<FullBookInfo> allBookInfo = new ArrayList<FullBookInfo>();
        final List<FullBookInfo> bookInfos = new ArrayList<>();
        if (goods.isNeedTeachLink()) {
        	// TODO 暂时直接查询数据库,以后改为从缓存中查询
            final List<DdbResourceBook> teachBookInfos = resourceBookService.getBooksTeachLink();
            if (teachBookInfos == null || teachBookInfos.size() <= 0) {
                return null;
            }
            // DdbResourceBook 转换为 FullBookInfo 类型
            for (DdbResourceBook ddbResourceBook : teachBookInfos) {
            	// TODO: 增加缓存后，生成fullBookInfo需要设置 toFullBookInfo 第二个参数为 true
                FullBookInfo fullBookInfo = resourceBookService.toFullBookInfo(
                		ddbResourceBook, false);
                allBookInfo.add(fullBookInfo);
            }
        } else {
            final Page<FullBookInfo> allBookInfoPage = resourceBookService.getCacheInfos(FullBookInfo.class, null,
                    CacheType.CACHE_FULL_BOOKS, null);
            if (allBookInfoPage == null || allBookInfoPage.getItems().size() <= 0) {
                return null;
            }
            allBookInfo = allBookInfoPage.getItems();
        }
        if (goods.getBookName() == null) {
            goods.setBookName("");
        }
        if (goods.getSuitGrade() == null) {
            goods.setSuitGrade("");
        }
        final String[] grades = goods.getSuitGrade().split(",");
        if (goods.getPublisherID() == null) {
            // 默认缺省值为外研社
            goods.setPublisherID(Publisher.PublisherID.WYS);
        }
        if (goods.getSystemType() == null) {
            goods.setSystemType(MobileApp.Type.WYT);
        }
        for (FullBookInfo fullBookInfo : allBookInfo) {
            boolean suitGrade = false;
            if (goods.getSystemType() == MobileApp.Type.GARDENER && !fullBookInfo.hasMpp()) {
            	// 园丁系统需要有 mpp 文件
                continue;
            } else if (goods.isNeedTeachLink() && !fullBookInfo.hasTeachLink()) {
            	continue;
            }
            final Publisher.PublisherID bookPublishId = Publisher.fromString(fullBookInfo.bookInfo.publisher);
            // 判断书籍类型是否符合
            if (StringUtils.isNotBlank(goods.getBookType())
                && !goods.getBookType().equals(fullBookInfo.type.getName())) {
                continue;
            }
            // 判断适应年级是否符合
            if (StringUtils.isBlank(goods.getSuitGrade())) {
                suitGrade = true;
            } else if (StringUtils.isNotBlank(fullBookInfo.grade)) {
                for (String grade : grades) {
                    if (fullBookInfo.grade.contains(grade)) {
                        suitGrade = true;
                        break;
                    }
                }
            }
            // 判断书籍名称模糊查询是否符合
            if (suitGrade && fullBookInfo.bookInfo.fullName.contains(goods.getBookName())
                && Publisher.isAllowed(bookPublishId, goods.getPublisherID())) {
                bookInfos.add(fullBookInfo);
            }
        }
        return bookInfos;
    }

    @Override
    public List<GoodsInfo> getBooksPhotoByIds(String ids) throws CacheException {
        final List<GoodsInfo> list = new ArrayList<GoodsInfo>();
        final String[] split = StringUtils.split(ids, "__");
        for (String id : split) {
            GoodsInfo goodsInfo = new GoodsInfo();
            final DdbResourceBook book = resourceBookService.getById(id);
            if (book != null) {
                goodsInfo.setId(id);
                goodsInfo.setName(book.getName());
                goodsInfo.setPhoto(FileUtils
                    .getFullRequestPath(StringUtils.isBlank(book.getPhoto()) ? book.getSuitImage() : book.getPhoto()));
                list.add(goodsInfo);
            }
        }
        return list;
    }

    @Override
    public GoodsInfo getGoodsByBookId(String bookId) throws Exception {
        final DdbResourceBook book = resourceBookService.getById(bookId);
        if (book == null) {
            return null;
        }
        final GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setId(book.getId());
        goodsInfo.setName(book.getName());
        goodsInfo.setBookId(book.getId());
        goodsInfo.setPhoto(
            FileUtils.getFullRequestPath(StringUtils.isBlank(book.getPhoto()) ? book.getSuitImage() : book.getPhoto()));
        goodsInfo.setAuthor(book.getAuthor());
        goodsInfo.setIntroduction(book.getIntroduction());
        goodsInfo.setResSize(book.getResSize());
        goodsInfo.setIsbn(book.getIsbn());
        return goodsInfo;
    }

}

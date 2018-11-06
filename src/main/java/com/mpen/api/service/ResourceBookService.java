/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import java.util.List;

import com.mp.shared.common.BookInfo;
import com.mp.shared.common.FileModule;
import com.mp.shared.common.FullBookInfo;
import com.mp.shared.common.LearnWordStructureInfo;
import com.mp.shared.common.MpCode;
import com.mp.shared.common.Page;
import com.mp.shared.common.PageInfo;
import com.mp.shared.common.ResourceVersion;
import com.mp.shared.common.SuccessResult;
import com.mpen.api.bean.Book;
import com.mpen.api.bean.ExamResult;
import com.mpen.api.bean.FileParam;
import com.mpen.api.bean.OralTestInfo;
import com.mpen.api.bean.OralTestInfo.OralTestPaper;
import com.mpen.api.bean.PreBook;
import com.mpen.api.bean.Unit;
import com.mpen.api.common.Constants.CacheType;
import com.mpen.api.common.Constants.ManualOp;
import com.mpen.api.common.Progress;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbResourceBook;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;

/**
 * ResourceBookService接口.
 *
 * @author kai
 *
 */
public interface ResourceBookService {

    /**
     * 获取书籍列表.
     * @param <T>
     * 
     * @throws Exception
     * 
     */
    <T> Page<T> getCacheInfos(Class<T> c, String bookId, CacheType type, ResourceVersion version) throws Exception;
    
    void updateLocalCache(CacheType type, String bookId);

    /**
     * 获取书籍.
     * 
     */
    DdbResourceBook getById(String id) throws CacheException;
    
    /**
     * 从数据库DdbResourceBook 转换为 FullBookInfo
     * @param book
     * @param checkFiles 是不是要检查相关 mppLink，mpvLink，teachLink等等文件在不在；以及 mp 文件可不可读
     * @return 如果有错误，返回null
     */
    FullBookInfo toFullBookInfo(DdbResourceBook book, boolean checkFiles);

    /**
     * 获取预下载书籍列表.
     * 
     * @throws Exception
     * 
     */
    List<PreBook> getPrepownloadBooks() throws Exception;

    /**
     * 获取书籍目录结构.
     * 
     */
    List<Unit> getBookContent(String bookId, String str) throws SdkException, CacheException;

    BookInfo getBookInfo(String bookId) throws Exception;

    PageInfo getPageInfoByMpCode(MpCode code) throws Exception;

    LearnWordStructureInfo getStructureInfo(String bookId) throws Exception;

    SuccessResult addBook(Book book) throws SdkException;

    SuccessResult createCode(Book book) throws SdkException, CacheException;

    Progress getProgress(Book book) throws CacheException;
    
    void updateBookDetail() throws Exception;
    
    /**
     * 更新pageInfo
     * @param bookId
     * @param isActive 是否为正式版
     * @param isSendEmail 是否发送邮件
     * @throws Exception
     */
    void updatePageInfoDetail(String bookId, boolean isActive, boolean isSendEmail)throws Exception;
    
    /**
     * 将版本设置为正式版本
     * @param version
     * @param key
     * @param bookId
     * @throws SdkException
     * @throws CacheException
     */
    void setIsActive(String version,ManualOp op, String bookId) throws SdkException, CacheException;
    
    /** 出版系统上传书资源文件后更新数据库信息
     * @param fileParam 
     * @param path 资源路径
     * @param fileMd5 文件的md5值
     * @return
     * @throws CacheException
     * @throws SdkException
     */
    SuccessResult updateBookLink(FileParam fileParam, String path, String fileMd5) throws CacheException, SdkException;

    List<FileModule> downloadBook(String id) throws Exception;
    /**
     * 书的资源下载
     * @param id
     * @param needTeachLink 下载教学资源
     * @param needMpLink 下载mp资源
     * @param needMppLink 下载mp,mpp,mpv整合资源
     * @return
     * @throws Exception
     */
    List<FileModule> downloadBookZipEx(String id, boolean needTeachLink, final boolean needMpLink, final boolean needMppLink) throws Exception;
    

    
    /**
     * 获取教学资源地址
     * @return
     */
    List<DdbResourceBook> getBooksTeachLink();



    ExamResult getOralTestInfo(String bookId, int num, DdbPeCustom peCustom, int assessmentType, List<String> loginds,
            int type) throws Exception;

    OralTestInfo getAllOralTestInfo(String bookId, String loginld, int assessmentType) throws Exception;

    List<OralTestPaper> getOralBookContent(String bookId);
}

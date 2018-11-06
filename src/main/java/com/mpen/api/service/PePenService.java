/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator;
import com.mp.shared.common.MPFileKey;
import com.mp.shared.common.Page;
import com.mpen.api.bean.PenInfo;
import com.mpen.api.bean.PenMac;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.domain.DdbPenCmd;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;

public interface PePenService {
    /**
     * 保存笔信息.
     * 
     */
    Boolean savePen(String identifiaction, PenInfo penInfo) throws SdkException;

    /**
     * 删除笔信息.
     * 
     */
    void deleteByIdentification(String identifiaction);

    /**
     * 查询笔是否允许打开adb.
     * 
     */
    Boolean adbAdmit(String identifiaction) throws SdkException;

    /**
     * 获取未绑定笔列表.
     * 
     */
    List<PenMac> getUnBindPen(String macStr) throws SdkException;

    /**
     * 获取笔完整mac地址.
     * 
     */
    PenMac getCompleteMac(String mac) throws SdkException;

    DdbPePen getPenByIdentifiaction(String identifiaction);

    /**
     * 未绑定时，默认允许云点读100次.
     * 
     */
    boolean checkReadingAllow(HttpServletRequest httpRequest);
    
    /**
     * 获取笔需要执行的命令 
     * @throws SdkException 
     */
    DdbPenCmd getPenCmd(String penId, String cmdId, String result) throws SdkException;
    
    boolean updateCmdUrl(String comdId,String url, String description);
    
    boolean checkAppVersion(HttpServletRequest request);
    
    /**
     * 根据序列号获取笔id和mac地址
     * 
     */
    DdbPePen getPenIdAndMac(String serialNum) throws CacheException;
    
    /**
     * 生成教师笔的版本信息
     * @return
     * @throws SdkException 
     */
    boolean createTeacherPenVersionInfo() throws SdkException;
    
    /**
     * 获取教师笔版本信息
     * @return
     * @throws SdkException 
     */
    Page<DdbPePen> getTeacherPenVersionInfo() throws SdkException;
    
    /**
     * 得到文件所对应的加密密钥集合
     * @param penInfo
     * @return
     * @throws Exception 
     */
    List<MPFileKey> getFilesRSAKey(String penId, PenInfo penInfo, String publishKey) throws Exception;
    
    /**
     * 根据序列号或mac获取笔信息(售后管理系统)
     * @param serialNum
     * @return
     * @throws CacheException
     */
    DdbPePen getPenInfoBySerialNumOrMac(String serialNum, String mac) throws SdkException;
    
    /**
     * 根据绑定手机号获取笔信息
     * @param bindMobile
     * @return
     * @throws SdkException
     */
    DdbPePen getPenInfoByBindMobile(String bindMobile) throws SdkException;
    
}

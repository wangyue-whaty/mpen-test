/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.service.impl;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mp.shared.common.SuccessResult;
import com.mpen.api.bean.Pen;
import com.mpen.api.bean.PenCustom;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.domain.DdbPrPenCustom;
import com.mpen.api.domain.EnumConst;
import com.mpen.api.exception.CacheException;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.EnumConstMapper;
import com.mpen.api.mapper.PePenMapper;
import com.mpen.api.mapper.PrPenCustomMapper;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.service.PePenService;
import com.mpen.api.service.PrPenCustomService;
import com.mpen.api.util.CommUtil;

/**
 * PrPenCustomService服务.
 * 
 * @author zyt
 *
 */
@Component
public class PrPenCustomServiceImpl implements PrPenCustomService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PrPenCustomServiceImpl.class);
    @Autowired
    private PePenMapper pePenMapper;
    @Autowired
    private PePenService pePenService;
    @Autowired
    private EnumConstMapper enumConstMapper;
    @Autowired
    private PrPenCustomMapper prPenCustomMapper;
    @Autowired
    private MemCacheService memCacheService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PenCustom saveBindRelationship(Pen penParam, UserSession user) throws SdkException, CacheException {
        // 验证笔是否存在
        final DdbPePen pen = pePenService.getPenByIdentifiaction(penParam.getIdentifiaction());
        if (pen == null || StringUtils.isBlank(pen.getId())) {
            throw new SdkException("没有找到指定笔的信息！");
        }
        // 验证笔是否合法
        final EnumConst isNative = enumConstMapper.getById(pen.getFlagActivie());
        if (!Constants.ONE.equals(isNative.getCode())) {
            throw new SdkException("该笔没有激活，请联系管理员！");
        }
        // 查询绑定关系
        if (Constants.ONE.equals(pen.getIsBind())) {
            throw new SdkException("该笔已经绑定过了，不能重复绑定！");
        }
        DdbPrPenCustom prPenCustom = prPenCustomMapper.getByUserId(user.getPeCustom().getId());
        if (prPenCustom != null && StringUtils.isNotBlank(prPenCustom.getId())) {
            throw new SdkException("该用户已经绑定过了，不能重复绑定！");
        }
        prPenCustom = new DdbPrPenCustom();
        prPenCustom.setId(CommUtil.genRecordKey());
        prPenCustom.setFkPenId(pen.getId());
        prPenCustom.setCreateDatetime(new Date());
        prPenCustom.setFkCustomId(user.getPeCustom().getId());
        prPenCustom.setIsValid(Constants.INT_ONE);
        // 保存绑定关系
        prPenCustomMapper.create(prPenCustom);
        // 添加绑定的设备的mac地址。
        pen.setMacAddress(penParam.getMacAddress());
        // 标记该笔为绑定状态
        pen.setIsBind(Constants.ONE);
        pePenMapper.update(pen);
        memCacheService.delete(CommUtil.getCacheKey(Constants.CACHE_PENINFO_KEY_PREFIX + pen.getIdentifiaction()));
        PenCustom penCustom = new PenCustom();
        penCustom.setId(prPenCustom.getId());
        return penCustom;
    }

    @Override
    public SuccessResult checkBindRelationship(String identifiaction, UserSession user) throws SdkException {
        // 验证笔是否存在
        final DdbPePen pen = pePenService.getPenByIdentifiaction(identifiaction);
        if (pen == null || StringUtils.isBlank(pen.getId())) {
            throw new SdkException(Constants.NO_MACHING_PEN);
        }
        final DdbPrPenCustom ddbPrPenCustom = prPenCustomMapper.getByPenIdAndUserId(pen.getId(),
            user.getPeCustom().getId());
        final SuccessResult result = new SuccessResult();
        if (ddbPrPenCustom != null && StringUtils.isNotBlank(ddbPrPenCustom.getId())) {
            result.setSuccess(true);
        } else {
            result.setSuccess(false);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean unBindRelationship(Pen penParam, UserSession user, String agentOperateKey) throws SdkException, CacheException {
        DdbPePen pen = null;
        String fkCustomId = null;
        // TODO:目前所有外研通用户都可以使用扫码解除绑定功能,之后要限制只有代理商和用户自己可以解绑
        if (Constants.AGENT_OPERATE_KEY.equals(agentOperateKey)) {
            pen = pePenMapper.getPenInfoBySerialNumOrMacAddress(penParam.getSerialNumber(),  penParam.getMacAddress());
            if (pen == null) {
                throw new SdkException(Constants.NO_MACHING_PEN);
            }
            // 根据笔信息查询绑定关系:1.存在绑定关系->解除 2.不存在绑定关系->return
            final DdbPrPenCustom ddbPrPenCustom = prPenCustomMapper.getByPenId(pen.getId());
            if (null == ddbPrPenCustom) {
                throw new SdkException("该笔没有绑定信息!"); 
            }
            fkCustomId = ddbPrPenCustom.getFkCustomId();
        } else {
            pen = pePenService.getPenByIdentifiaction(penParam.getIdentifiaction());
            if (pen == null) {
                throw new SdkException(Constants.NO_MACHING_PEN);
            }
            fkCustomId = user.getPeCustom().getId();
        }
        final DdbPrPenCustom penCustom = new DdbPrPenCustom();
        penCustom.setFkCustomId(fkCustomId);
        penCustom.setFkPenId(pen.getId());
        penCustom.setDeleteTime(new Date());
        memCacheService.delete(CommUtil.getCacheKey(Constants.CACHE_PENINFO_KEY_PREFIX + pen.getIdentifiaction()));
        final int temp = prPenCustomMapper.deleteByCustomIdAndPenId(penCustom);
        if (temp > 0) {
            pen.setIsBind(Constants.ZERO);
            pePenMapper.update(pen);
            return true;
        }
        return false;
    }
}

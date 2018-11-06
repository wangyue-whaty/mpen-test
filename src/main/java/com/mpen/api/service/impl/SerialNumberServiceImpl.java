package com.mpen.api.service.impl;

import java.time.Instant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mpen.api.bean.Pen;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.domain.DdbPenSerialNumberRelationship;
import com.mpen.api.domain.DdbSerialNumber;
import com.mpen.api.domain.EnumConst;
import com.mpen.api.exception.SdkException;
import com.mpen.api.mapper.EnumConstMapper;
import com.mpen.api.mapper.PenSerialNumberRelationshipMapper;
import com.mpen.api.mapper.SerialNumberMapper;
import com.mpen.api.service.PePenService;
import com.mpen.api.service.SerialNumberService;
import com.mpen.api.util.CommUtil;

@Component
public class SerialNumberServiceImpl implements SerialNumberService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SerialNumberServiceImpl.class);
    @Autowired
    private PePenService pePenService;
    @Autowired
    private EnumConstMapper enumConstMapper;
    @Autowired
    private SerialNumberMapper serialNumberMapper;
    @Autowired
    private PenSerialNumberRelationshipMapper penSerialNumberRelationshipMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveRelationship(Pen penParam) throws SdkException {
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
        // 笔是否已有有效序列号
        DdbPenSerialNumberRelationship penSerialNumber = penSerialNumberRelationshipMapper
            .getVolidDataByPenId(pen.getId());
        if (penSerialNumber != null) {
            throw new SdkException("该笔已绑定过序列号！");
        }
        String number = null;
        String prefix = null;
        long suffix = 0;
        try {
            number = penParam.getSerialNumber();
            if(number.length() < 13) {
                throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
            }
            // 截取字符串的后11位
            suffix = Long.parseLong(number.substring(number.length() - 11, number.length()));
            // 获得截取减去后11位的字符串
            prefix = number.substring(0, number.length() - 11);
        } catch (NumberFormatException e) {
            throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
        }
        // 此序列号是否在某一个有效序列号段内
        final DdbSerialNumber serialNumber = serialNumberMapper.get(prefix, suffix);
        if (serialNumber == null) {
            throw new SdkException("该序列号非法！");
        }
        // 此序列号是否已是某一支笔的有效序列号
        penSerialNumber = penSerialNumberRelationshipMapper.getVolidDataBySerialNumber(number);
        if (penSerialNumber != null) {
            throw new SdkException("该序列号已被占用！");
        }
        // 校验通过
        penSerialNumber = new DdbPenSerialNumberRelationship();
        penSerialNumber.setId(CommUtil.genRecordKey());
        penSerialNumber.setFkPenId(pen.getId());
        penSerialNumber.setIsValid(1);
        penSerialNumber.setSerialNumber(number);
        penSerialNumber.setCreateDate(Instant.now());
        penSerialNumberRelationshipMapper.save(penSerialNumber);
        return true;
    }

}

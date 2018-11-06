package com.mpen.api.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mpen.api.domain.PCVersionUpdate;
import com.mpen.api.mapper.PCVersionUpdateMapper;
import com.mpen.api.service.PCVersionUpdateService;
import com.mpen.api.util.FileUtils;

/*
 * PC系统ServiceImpl层
 */
@Service
public class PCVersionUpdateServiceImpl implements PCVersionUpdateService {

    @Resource
    PCVersionUpdateMapper pcVersionUpdateMapper;

    /*
     * 园丁PC系统根据类型返回更新的数据
     */
    @Override
    public PCVersionUpdate get(PCVersionUpdate.Type type) throws Exception {
        final PCVersionUpdate pc = pcVersionUpdateMapper.get(type);
        if (pc != null) {
            pc.setFileUrl(FileUtils.getFullRequestPath(pc.getFileUrl()));
        }
        return pc;
    }

}

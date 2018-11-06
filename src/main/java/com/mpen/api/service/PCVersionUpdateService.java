package com.mpen.api.service;

import com.mpen.api.domain.PCVersionUpdate;
import com.mpen.api.domain.PCVersionUpdate.Type;

/*
 * PC系统Service层
 * 
 */
public interface PCVersionUpdateService {

    PCVersionUpdate get(Type type) throws Exception;

}

package com.mpen.api.service;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mp.shared.record.ActionRecords;
import com.mpen.api.bean.UserSession;
import com.mpen.api.exception.SdkException;

public interface ActionRecordService {
    int save(ActionRecords actionRecords, UserSession userSession, HttpServletRequest request) throws SdkException, JsonProcessingException;
}

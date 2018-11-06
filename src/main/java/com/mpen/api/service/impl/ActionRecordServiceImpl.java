package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mp.shared.record.ActionRecord;
import com.mp.shared.record.ActionRecords;
import com.mp.shared.record.TaskRecord;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbActionRecord;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.domain.DdbRecordUserBook;
import com.mpen.api.exception.SdkException;
import com.mpen.api.service.ActionRecordService;
import com.mpen.api.service.PePenService;
import com.mpen.api.service.RecordUserBookService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.RawStudyLogUtil;
import com.mpen.api.util.StudyLogUtil;

/**
 * ActionRecord服务.
 *
 * @author zyt
 *
 */
@Component
public class ActionRecordServiceImpl implements ActionRecordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActionRecordServiceImpl.class);
    @Autowired
    private PePenService pePenService;
    @Autowired
    private RecordUserBookService recordUserBookService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(ActionRecords actionRecords, UserSession userSession, HttpServletRequest request)
        throws SdkException, JsonProcessingException {
        //打印笔端原始日志到log中,保存原始文件
        final Map<String,Object> rawStudyLogMap = new HashMap<>();
        final String loginId = userSession != null?userSession.getLoginId():"";
        rawStudyLogMap.put("loginId", loginId);
        rawStudyLogMap.put("actionRecords", actionRecords);
        rawStudyLogMap.put("ipAddress", CommUtil.getIpAddr(request));
        RawStudyLogUtil.printStudyLog(rawStudyLogMap);

        // 口语评测或课本点读成功数量
        int count = 0;
        final List<ActionRecord> actionRecordList = actionRecords.getRecords();
        if (actionRecordList == null || actionRecordList.size() <= 0) {
            return count;
        }
        final String uploadUuid = actionRecords.getUploadUuid();
        final String penId = actionRecords.getPenId();
        final DdbPePen pen = pePenService.getPenByIdentifiaction(penId);
        if (pen == null) {
            throw new SdkException(Constants.INVALID_PARAMRTER_MESSAGE);
        }
        DdbActionRecord record = null;
        ActionRecord actionRecord = null;
        final List<DdbRecordUserBook> recordUserBooks = new ArrayList<>();
        for (int i = 0; i < actionRecordList.size(); i++) {
            actionRecord = actionRecordList.get(i);
            record = new DdbActionRecord();
            record.setUploadUuid(uploadUuid);
            record.setSequceNumInBatch(i);
            record.setFkPenId(pen.getId());
            record.setUploadTime(new Date());
            record.setType(actionRecord.type);
            record.setSubType(actionRecord.subType);
            if (ActionRecord.Type.TASK == actionRecord.type
                && (ActionRecord.Subtype.FetchCodeInfo == actionRecord.subType
                    || ActionRecord.Subtype.ReadEvalGroup == actionRecord.subType)
                && userSession != null) {
                if (recordUserBookService.save(TaskRecord.fromActionRecord(actionRecord), userSession, pen.getId(), recordUserBooks)) {
                    count++;
                }
            }
            record.setData(Constants.GSON.toJson(actionRecord.data));
            record.setVersion(actionRecord.version);
            LOGGER.info(Constants.GSON.toJson(record));
        }
        
        //打印处理后的日志集合
        //TODO 删除studylog;pipeline直接使用rawStudyLog
        if (recordUserBooks != null && recordUserBooks.size() >0) {
            StudyLogUtil.printStudyLog(recordUserBooks);
        }
        
        return count;
    }
}

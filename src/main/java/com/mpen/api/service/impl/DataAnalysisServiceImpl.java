package com.mpen.api.service.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mpen.api.bean.DataAnalysisResult;
import com.mpen.api.bean.DataAnalysisResult.BookRanking;
import com.mpen.api.bean.DataAnalysisResult.TimeStudying;
import com.mpen.api.bean.DataAnalysisResult.Version;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbRecordUserBook;
import com.mpen.api.mapper.PeCustomMapper;
import com.mpen.api.mapper.PePenMapper;
import com.mpen.api.mapper.RecordUserBookMapper;
import com.mpen.api.service.DataAnalysisService;

@Component
public class DataAnalysisServiceImpl implements DataAnalysisService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataAnalysisServiceImpl.class);
    @Autowired
    private RecordUserBookMapper recordUserBookMapper;
    @Autowired
    private PePenMapper pePenMapper;
    @Autowired
    private PeCustomMapper peCustomMapper;
    @Override
    public DataAnalysisResult getDataAnalysisResult() {
        final LocalDateTime time = LocalDateTime.now();
        final DataAnalysisResult dataResult = new DataAnalysisResult();
        final LocalDate endDate = time.toLocalDate();
        final LocalDate startDate = endDate.minusDays(1);
        final List<DdbPeCustom> users = peCustomMapper.get();
        dataResult.userCount = users.size();
        dataResult.date = startDate.toString();
        final List<BookRanking> bookRanding = recordUserBookMapper.getBookRanding(startDate, endDate);
        dataResult.bookList = bookRanding;
        final List<Version> appVersion = pePenMapper.getAppVersion();
        dataResult.appVersion = appVersion;
        final List<Version> romVersion = pePenMapper.getRomVersion();
        dataResult.romVersion = romVersion;
        final TimeStudying[] timeStudying = new TimeStudying[7];
        final List<DdbRecordUserBook> dailyRecord = recordUserBookMapper.getDailyRecord(startDate, endDate);
        dataResult.clickTimes = dailyRecord.size();
        for (DdbRecordUserBook record : dailyRecord) {
            final Instant instant = record.getClickTime().toInstant();
            final LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            final int index = (localDateTime.getHour() - 8) / 2;
            if (index > 6 || index < 0) {
                continue;
            }
            if (timeStudying[index] == null) {
                timeStudying[index] = new TimeStudying();
            }
            timeStudying[index].clickNum += 1;
            timeStudying[index].user.add(record.getLoginId());
        }
        dataResult.timeStudying = timeStudying;
        return dataResult;
    }

}

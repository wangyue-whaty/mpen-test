package com.mpen.api.util;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import com.mpen.api.bean.WeeklyParam;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.mapper.PeCustomMapper;

/**
 * TODO 任务定时器
 *
 */
public class TaskTimers {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskTimers.class);
    private TaskExecutor taskExecutor;
    @Autowired
    private PeCustomMapper peCustomMapper;

    public TaskTimers(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    /**
     * TODO 周报定时推送任务(每周日上午十点)
     */
    @Async
    @Scheduled(cron = "0 0 10 * * SUN")
    public void weeklyTask() {
        final List<DdbPeCustom> customs = peCustomMapper.get();
        final WeeklyParam param = new WeeklyParam();
        final LocalDate date = LocalDate.now();
        param.setEndDate(date.toString());
        param.setStartDate(date.minusWeeks(1).toString());
        final Map<String, String> msg = new HashMap<>();
        msg.put(Constants.WEEKLY, Constants.GSON.toJson(param));
        customs.forEach((custom) -> {
            try {
                // TODO 暂时只对ios平台，viaton进行推送
                final JPushUtil.JpushParam jpushParam = new JPushUtil.JpushParam(custom.getLoginId(),
                    Constants.WEEKLY_MSG, msg, JPushUtil.AppType.VIATON, JPushUtil.PlatformType.IOS);
                JPushUtil.sendPushToUser(jpushParam);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}

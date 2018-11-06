package com.mpen.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class RawStudyLogUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(RawStudyLogUtil.class);

    /**
     * 笔端原始学情日志打印
     * 
     * @param t
     */
    public static <T> void printStudyLog(T t) {
        LOGGER.info(JSON.toJSONString(t));
    }
}

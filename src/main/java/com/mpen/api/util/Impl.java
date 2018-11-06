package com.mpen.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mp.shared.CompatUtils;

/**
 * Created by feng on 3/15/17.
 *
 * 笔端 服务器、笔app共享 实现
 */

public final class Impl extends CompatUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(Impl.class);

    /**
     * log interface
     *
     * @param tag
     * @param msg
     */
    @Override
    public void info(String tag, String msg) {
        LOGGER.info(tag + "-" + msg);
    }

    /**
     * log interface
     *
     * @param tag
     * @param msg
     */
    @Override
    public void warning(String tag, String msg) {
        LOGGER.warn(tag + "-" + msg);
    }

    /**
     * log interface
     *
     * @param tag
     * @param msg
     */
    @Override
    public void error(String tag, String msg) {
        LOGGER.error(tag + "-" + msg);
    }

    /**
     * log interface
     *
     * @param tag
     * @param msg
     * @param exception
     */
    @Override
    public void error(String tag, String msg, Exception exception) {
        LOGGER.error(tag + "-" + msg, exception);
    }
}

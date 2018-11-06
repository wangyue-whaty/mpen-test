package com.mp.shared;

/**
 * Created by feng on 3/15/17.
 *
 * 这里建立一个通用的常用的工具类的interface，这样在服务器和笔之间共享的代码可以使用
 *
 * 然后在服务器，和笔端都需要有对应的实现，会来设置 impl
 *
 *
 */

public abstract class CompatUtils {
    /**
     * 服务器，笔端，要在这个CompatUtils被用之前，初始化 impl
     */
    public static CompatUtils impl;
    /**
     * log interface
     */
    public abstract void info(String tag, String msg);

    /**
     * log interface
     */
    public abstract void warning(String tag, String msg);

    /**
     * log interface
     */
    public abstract void error(String tag, String msg);

    /**
     * log interface
     */
    public abstract void error(String tag, String msg, Exception exception);

}

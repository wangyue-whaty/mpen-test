package com.mpen.api.util;

import java.time.LocalDateTime;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mp.shared.common.NetworkResult;
import com.mpen.api.bean.UserSession;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Uris;

import nl.bitwalker.useragentutils.UserAgent;

/**
 * TODO 使用whaty云眼分析用户行为，需要使用自定义格式，通过此工具类生成特定格式日志
 */
public final class LogUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogUtil.class);
    private static final String SITE_CODE = "ddb";

    /**
     * TODO 生成日志方法，通过不同type区分用户行为，增加统计用户行为时，需要扩展枚举类型Type
     */
    public static void printLog(final HttpServletRequest request, String action, UserSession userSession,
        NetworkResult<Object> result) {
        final String uri = request.getRequestURI();
        Type type = Type.OTHER;
        if (uri.contains(Uris.V1_AUDIOS)) {
            type = Type.CLOUD_READING;
        } else if (uri.contains(Uris.V1_USER + Uris.LOGIN)) {
            type = Type.REGISTRATION;
        } else {
            switch (action) {
            case Constants.SAVE_BINDRELATIONSHIP:
                type = Type.BIND_RELATIONSHIP;
                break;
            case Constants.GET_VALID_BOOKS:
                type = Type.GET_BOOKPAGE;
                break;
            default:
                return;
            }
        }
        final LogInfo logInfo = new LogInfo();
        //原uid 由session.getId()更改为ucenterkey,确定用户一次登录操作的唯一标识
        Object ucenterKeyObj = null; 
        //获取认证请求头,判断是否为空,如果为空则为V1否则为V2
        String authorization = request.getHeader("Authorization");
        if(CommUtil.checkV2SSO(authorization)) {
            ucenterKeyObj = authorization;
        }else {
            final Cookie[] cookies = request.getCookies();
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(Constants.UCENTERKEY)){
                    ucenterKeyObj = cookie.getValue();
                    break;
                }
            }
        }
        final User user = new User(ucenterKeyObj == null ? Constants.UNKNOW : ucenterKeyObj.toString(),
            userSession == null ? Constants.UNKNOW : userSession.getLoginId(), CommUtil.getIpAddr(request), SITE_CODE);
        logInfo.user = user;
        final LocalDateTime time = LocalDateTime.now();
        final Event event = new Event(uri, CommUtil.genRecordKey(), type.name, type.code, type.desc,
            Constants.DATA_FORMART.format(time), result.getErrorCode(), result.getErrorMsg());
        logInfo.event = event;
        final Detail detail = new Detail(request);
        logInfo.detail = detail;
        LOGGER.info(Constants.GSON.toJson(logInfo));
    }

    public static final class LogInfo {
        public User user;
        public Event event;
        public Detail detail;
    }

    public static final class User {
        public final String uid;
        public final String loginId;
        public final String ip;
        public final String siteCode;

        public User(String uid, String loginId, String ip, String siteCode) {
            this.uid = uid;
            this.loginId = loginId;
            this.ip = ip;
            this.siteCode = siteCode;
        }
    }

    public static final class Event {
        // 页面标题
        public String pageTitle;
        // 访问页面地址
        public String url;
        // 事件标识
        public String eventid;
        // 事件名称
        public String eventName;
        // 事件编号
        public String eventCode;
        // 事件描述
        public String eventDesc;
        // 从哪来跳转
        public String referrer;
        // 前一步时间的编号
        public String previousCode;
        // 离开时间(yyyy-MM-dd hh:mm:ss)
        public String endTime;
        // 发生时间(yyyy-MM-dd hh:mm:ss)
        public String beginTime;
        // 持续时间单位秒
        public int duration;
        // 执行码
        public String errorCode;
        // 执行信息
        public String errorMsg;

        public Event() {

        }

        public Event(String url, String eventid, String eventName, String eventCode, String eventDesc, String beginTime,
            String errorCode, String errorMsg) {
            this.url = url;
            this.eventid = eventid;
            this.eventName = eventName;
            this.eventCode = eventCode;
            this.eventDesc = eventDesc;
            this.beginTime = beginTime;
            this.errorCode = errorCode;
            this.errorMsg = errorMsg;
        }

    }

    public enum Type {
        BIND_RELATIONSHIP("绑定", "0", "笔与手机app保存绑定关系"), CLOUD_READING("云点读", "1", "云点读"), GET_BOOKPAGE("书籍列表", "2",
            "获取可点读书单"), OTHER("其它", "3", "其它"), REGISTRATION("注册", "4", "用户注册后首次登陆");

        String name;
        String code;
        String desc;

        Type(String name, String code, String desc) {
            this.name = name;
            this.code = code;
            this.desc = desc;
        }

    }

    public static final class Detail {
        // 浏览器信息
        public String browser;
        public Termial termial;
        //网梯定义：应用配置，用于云眼统计与页面展示
        public App app;

        public Detail() {

        }

        public Detail(HttpServletRequest request) {
            this.termial = new Termial();
            this.app = new App();
            // user-version:笔端定义请求头属性，用于接受笔端的系统信息
            if (StringUtils.isNotBlank(request.getHeader("user-version"))) {
                final UserAgent agent = UserAgent.parseUserAgentString(request.getHeader("user-version"));
                this.browser = agent.getBrowser().getName();
                termial.os = agent.getOperatingSystem().getName();
                app.code = agent.getOperatingSystem().getName();
            } else {
                this.browser = Constants.UNKNOW;
                termial.os = Constants.UNKNOW;
                app.code = Constants.UNKNOW;
            }
        }
    }
    public static final class Termial {
        // 操作系统
        public String os;
        // 设备id
        public String deviceID;
        // 设备名称
        public String deviceName;
        // 设备mac地址
        public String deviceMac;
    }
    /**
     * 网梯开发中定义：
     *     接收产品的应用信息，用于云眼项目进行数据统计于展示
     *     目前点读笔只需code值(用于区分Linux和Android笔)
     */
    public static final class App {
        //应用编号
        public String code;
        //应用名称
        public String name;
        //版本
        public String version;
        //描述
        public String description;
    }

}

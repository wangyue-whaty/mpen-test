/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public final class JPushUtil {
    protected static final Logger LOG = LoggerFactory.getLogger(JPushUtil.class);

    /**
     * 推送服务对外方法.
     * 
     * @throws APIRequestException
     * @throws APIConnectionException
     * 
     */
    public static void sendPushToUser(JpushParam param) throws APIConnectionException, APIRequestException {
        if (param.appType == null) {
            final AppType[] values = AppType.values();
            for (AppType appType : values) {
                param.appType = appType;
                send(param);
            }
        } else {
            send(param);
        }
    }

    /**
     * 对指定平台推送自定义消息.
     * 
     * @throws APIRequestException
     * @throws APIConnectionException
     * 
     */
    private static PushResult send(JpushParam param) {
        JPushClient jpushClient = null;
        try {
            Notification build = null;
            switch (param.platformType) {
            case IOS:
                build = Notification.newBuilder().addPlatformNotification(
                    IosNotification.newBuilder().setAlert(param.alert).addExtras(param.msg).build()).build();
                break;
            case ANDROID:
                build = Notification.newBuilder().addPlatformNotification(AndroidNotification.newBuilder()
                    .setTitle(param.appType.getTitle()).setAlert(param.alert).addExtras(param.msg).build()).build();
                break;
            default:
                build = Notification.newBuilder()
                    .addPlatformNotification(
                        IosNotification.newBuilder().setAlert(param.alert).addExtras(param.msg).build())
                    .addPlatformNotification(AndroidNotification.newBuilder().setTitle(param.appType.getTitle())
                        .setAlert(param.alert).addExtras(param.msg).build())
                    .build();
                break;
            }
            final PushPayload payload = PushPayload.newBuilder().setPlatform(Platform.all())
                // 设置接受的平台
                .setAudience(Audience.alias(param.alias))
                // Audience设置为all，说明采用广播方式推送，所有用户都可以接收到
                .setNotification(build).setOptions(Options.newBuilder().setApnsProduction(true).build()).build();
            jpushClient = new JPushClient(param.appType.getSecret(), param.appType.getKey());
            return jpushClient.sendPush(payload);
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        } finally {
            if (jpushClient != null) {
                jpushClient.close();
            }
        }
        return null;
    }

    public static final class JpushParam {
        // 推送别称（loginId）
        public String alias;
        // 推送消息
        public String alert;
        // 自定义消息
        public Map<String, String> msg;
        // 推送app类型
        public AppType appType;
        // 推送指定平台
        public PlatformType platformType;

        public JpushParam(String alias, String alert, Map<String, String> msg, AppType appType,
            PlatformType platformType) {
            this.alias = alias;
            this.alert = alert;
            this.msg = msg;
            this.appType = appType;
            this.platformType = platformType;
        }

    }

    public enum AppType {
        HAOQI("好奇智能点读笔", "ef1183764eac5f222f47b465", "d7750aab49abd1f3adcc0814"), VIATON("云智点读笔",
            "ce48944ef483a5b87cfda41d", "0cb1e2bac525ce282779cbd5");
        String title;
        String key;
        String secret;

        AppType(String title, String key, String secret) {
            this.title = title;
            this.key = key;
            this.secret = secret;
        }

        public final String getTitle() {
            return title;
        }

        public final String getKey() {
            return key;
        }

        public final String getSecret() {
            return secret;
        }

    }

    public enum PlatformType {
        IOS, ANDROID, ALL
    }
}

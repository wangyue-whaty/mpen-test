package com.mpen.api.util.qcloud;

import java.util.ArrayList;
import java.util.List;

import com.mpen.api.common.Constants;
import com.mpen.api.exception.CacheException;
import com.mpen.api.service.MemCacheService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.qcloud.sms.SmsSingleSender;
import com.mpen.api.util.qcloud.sms.SmsSingleSenderResult;

public class SmsUtil {
    public static final int APP_ID = 1400040435;
    public static final String APP_KEY = "28d148c4aa846dee85bf7d7c509da1ba";
    public static final String ZYT_PHONE = "13661309890";
    public static final int TMPL_ID = 42713;

    public static SmsSingleSenderResult sendSmsError(ArrayList<String> params) {
        try {
            // 初始化单发
            SmsSingleSender singleSender = new SmsSingleSender(APP_ID, APP_KEY);
            return singleSender.sendWithParam("86", ZYT_PHONE, TMPL_ID, params, "", "", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addToSmsQueue(ArrayList<String> params, MemCacheService memCacheService) {
        try {
            final String key = CommUtil.getCacheKey(Constants.CACHE_SEND_SMS_KEY);
            List<ArrayList<String>> smsList = memCacheService.get(key);
            if (smsList == null) {
                smsList = new ArrayList<>();
            }
            smsList.add(params);
            memCacheService.set(key, smsList, Constants.DEFAULT_CACHE_EXPIRATION);
        } catch (CacheException e) {
            e.printStackTrace();
        }
    }

}

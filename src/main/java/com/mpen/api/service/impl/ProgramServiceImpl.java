package com.mpen.api.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.reflect.TypeToken;
import com.mpen.api.bean.FileParam;
import com.mpen.api.common.Constants;
import com.mpen.api.exception.SdkException;
import com.mpen.api.service.ProgramService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.JsSignUtil;

// TODO: 之后对微信API的访问单独提取出来做成一个文件WechatUtil,包括url,CommUtil.get,constant等
@Component
public class ProgramServiceImpl implements ProgramService{
    private static final Logger LOGGER = LoggerFactory.getLogger(ProgramServiceImpl.class);
    
    // TODO：单元测试
    @Override
    public String getWeChatQRcode(FileParam fileParam) throws SdkException {
        final long time = System.currentTimeMillis();
        if (time > Constants.API_TOKEN_OVERTIME) {
            // 得到access_token 和 expires_in
            final String string = CommUtil.get(
                "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxa7a006c8ea49e5f8&secret=908203838a61bb7c57e1a0cb8632e32c");
            final Map<String, String> map = Constants.GSON.fromJson(string,
                new TypeToken<Map<String, String>>() {
                }.getType());
            Constants.API_TOKEN = map.get("access_token");
            Constants.API_TOKEN_OVERTIME = time + 7200000;
        }
        final Map<String, Object> param = new HashMap<>();
        param.put("page", "pages/play/play");
        param.put("scene", fileParam.getId());
        final String path = "/root/johny/github/mpen-manager/incoming/complete/" + CommUtil.genRecordKey()
            + ".jpg";
        // 根据token 和param参数请求weixin.qq.com得到二维码
        CommUtil.post("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + Constants.API_TOKEN,
            Constants.GSON.toJson(param), path);
        return path;
    }

    
    @Override
    public Map<String, String> getWechatQRcodeEx(HttpServletRequest request, String requestUrl)
            throws SdkException, IOException {
        final long time = System.currentTimeMillis();
        if (time > Constants.WECHAT_TOKEN_OVERTIME) {
            // 得到access_token 和 expires_in
            final String string = CommUtil.get(Constants.GET_WECHAT_ACCESSTOKEN_URL.replace("APPID", Constants.WECHAT_PUB_ID)
                    .replace("SECRET", Constants.WECHAT_PUB_SECRET));
            final Map<String, String> map = Constants.GSON.fromJson(string, new TypeToken<Map<String, String>>() {
            }.getType());
            Constants.WECHAT_TOKEN = map.get("access_token");
            // 获取微信JSSDK的ticket
            final String ticketString = CommUtil.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="
                    + Constants.WECHAT_TOKEN + "&type=jsapi");
            final Map<String, String> ticketMap = Constants.GSON.fromJson(ticketString,
                    new TypeToken<Map<String, String>>() {
                    }.getType());
            Constants.WECHAT_TICKET = ticketMap.get("ticket");
            Constants.WECHAT_TOKEN_OVERTIME = time + 7200000;
        }
        return JsSignUtil.sign(requestUrl, Constants.WECHAT_TOKEN, Constants.WECHAT_TICKET);
    }
    
}

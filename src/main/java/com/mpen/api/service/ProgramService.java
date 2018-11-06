package com.mpen.api.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.mpen.api.bean.FileParam;
import com.mpen.api.exception.SdkException;

/**
 * 小程序接口
 * @author wangyue
 *
 */
public interface ProgramService {

    /**
     * 获取小程序二维码接口
     * @return
     * @throws SdkException 
     */
    String getWeChatQRcode(FileParam fileParam) throws SdkException;
 
    /**
     * 微信公众号获取扫码所需签名接口
     * @param request
     * @return
     * @throws SdkException
     * @throws IOException
     */
    Map<String, String> getWechatQRcodeEx(HttpServletRequest request, String requestUrl) throws SdkException, IOException;
}

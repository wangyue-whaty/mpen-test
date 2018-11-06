/*
 * 文件名：MPenSessionHttpServletRequest.java 
 * 描述：〈描述〉
 * 创建人：huze
 * 创建时间：2017年12月16日下午4:27:34
 */
package com.mpen.api.filter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import com.mpen.api.common.Constants;

/**
 * COOKIE规范中要求在cookie的名或值中不能使用一下特殊字符。
 * "(" | ")" | "<" | ">" | "@"
 * | "," | ";" | ":" | "\" | <">
 * | "/" | "[" | "]" | "?" | "="
 * | "{" | "}" 
 * 
 * TODO 要把cookie都规范化（手机端，笔端，和服务器端）
 * 
 * @author huze
 */
public class MPenSessionHttpServletRequest extends HttpServletRequestWrapper {

    public final static String COOKIES = "Cookie";

    /**
     * @param request
     */
    public MPenSessionHttpServletRequest(HttpServletRequest request) {
        super(request);
    }

    @Override
    public Cookie[] getCookies() {
        Cookie[] cookies = null;
        final String strCookie = super.getHeader(COOKIES);
        if (null != strCookie && strCookie.indexOf(Constants.SESSIONKEY) >= 0) {
            final String[] strCookies = strCookie.split(";");
            final int length = strCookies.length;
            cookies = new Cookie[length];
            for (int i = 0; i < length; i++) {
                final String[] entry = strCookies[i].trim().split("=");
                if (entry.length == 2) {
                    cookies[i] = new Cookie(entry[0], entry[1]);
                }
            }
        } else {
            cookies = super.getCookies();
        }
        return cookies;
    }

}

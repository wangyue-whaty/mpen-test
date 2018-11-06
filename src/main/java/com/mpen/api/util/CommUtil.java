/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.util;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.WebUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.mp.shared.utils.FUtils;
import com.mpen.api.bean.UserPhoto;
import com.mpen.api.common.Constants;
import com.mpen.api.common.SSOKey;
import com.mpen.api.domain.SsoUser;
import com.mpen.api.exception.SdkException;

/**
 * 通用工具类.
 *
 * @author kai
 *
 */
public final class CommUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommUtil.class);
    // 汉字从一到十对应数字map
    private static final Map<String, Integer> CNNumerMap = new HashMap<String, Integer>();

    static {
        CNNumerMap.put("一", 1);
        CNNumerMap.put("二", 2);
        CNNumerMap.put("三", 3);
        CNNumerMap.put("四", 4);
        CNNumerMap.put("五", 5);
        CNNumerMap.put("六", 6);
        CNNumerMap.put("七", 7);
        CNNumerMap.put("八", 8);
        CNNumerMap.put("九", 9);
        CNNumerMap.put("十", 10);
    }

    /**
     * 汉字一,二...排序比较
     * @param o1
     * @param o2
     * @return
     */
    public static int CNNumerCompare(String o1, String o2) {
        final int len1 = o1.length();
        final int len2 = o2.length();
        final int lim = Math.min(len1, len2);
        final char v1[] = o1.toCharArray();
        final char v2[] = o2.toCharArray();
        int k = 0;
        while (k < lim) {
            final char c1 = v1[k];
            final char c2 = v2[k];
            if (c1 != c2) {
                if (CNNumerMap.containsKey(String.valueOf(c1)) && CNNumerMap.containsKey(String.valueOf(c2))) {
                    return CNNumerMap.get(String.valueOf(c1)) - CNNumerMap.get(String.valueOf(c2));
                }
                return c1 - c2;
            }
            k++;
        }
        return len1 - len2;
    }
    

    public static String genRecordKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 测试时通过identifiaction获取code方法.
     * 
     */
    public static String getPenCode(String identifiaction) throws SdkException {
        final String first = identifiaction.substring(0, 3);
        final String second = identifiaction.substring(3);
        return DigestUtils.md5Hex(DigestUtils.md5Hex(second + first + second));
    }
    /**
     * 获取学情日志表名字
     * @param loginId
     * @return recordTableName
     */
    public static String getRecordTableName(String loginId){
        int value=(int) (Long.parseLong( loginId )%4);
        String recordTableName="ddb_new_record_user_book_shard_"+value;
        return recordTableName;
    }
    
    /**
     * 获取UTC时间.
     * 
     */
    public static String getUTCTime(Date date) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        final String time = sdf.format(date);
        return time;
    }

    /**
     * 获取缓存Key.
     * 
     */
    public static String getCacheKey(String str) {
        str = Constants.SCHOOL_NO + "_SpringBoot_" + str;
        return str;
    }
    /**
     *  时间(yyyy-MM-dd HH:mm:ss):String格式转Date格式
     */
    public static  Date parseTimeFormattoDate(String date){
        Date time = null;
        try {
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }
    /**
     * 时间(yyyy-MM-dd HH:mm:ss):Date格式转String格式
     */
    public static String parseTimeToString(Date date){
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(date);
        return time;
    }
    /**
     *  时间(yyyy-MM-dd):String格式转Date格式
     */
    public static  Date parseTimeFormattoDayDate(String date){
        Date time = null;
        try {
            time = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }
    /**
     *  时间(yyyy-MM-dd):Date格式转String格式
     */
    public static  String parseTimeFormattoDayDate(Date date){
        String time = null;
        try {
            time = new SimpleDateFormat("yyyy-MM-dd").format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }
    /**
     *  时间(yyyy-MM):Date格式转String格式
     */
    public static  String parseTimeFormattoMonthDate(Date date){
        String time = null;
        try {
            time = new SimpleDateFormat("yyyy-MM").format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }
    /**
     * 获取：昨天22:00 今天22:00
     * @return
     */
    public static Map<String,String> getDay(){
        final Map<String, String> dayMap=new HashMap<>();
        final Date date = new Date();
        final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        final String format = df.format(date);
        final String todayDate = format + " 22:00:00";
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        final Date time = calendar.getTime();
        final String format2 = df.format(time);
        final String beforeDate = format2 + " 22:00:00";
        dayMap.put("todayDate",todayDate);
        dayMap.put("beforeDate", beforeDate);
        return dayMap;
    }

    /**
     * 时间(yyyy-MM):String格式转Date格式
     */
    public static Date parseTimeFormattoMonthDate(String date) {
        Date time = null;
        try {
            time = new SimpleDateFormat("yyyy-MM").parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }
    
    /**
     * 传入具体一天，返回具体日期减少一天
     * 
     * @throws ParseException
     */
    public static String subDay(String date) {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = null;
        try {
            dt = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dt);
        rightNow.add(Calendar.DATE, -1);
        final Date dt1 = rightNow.getTime();
        final String reStr = sdf.format(dt1);
        return reStr;

    }
    
    /**
     * 获取指定日期与当前日期相差的天数
     * @param date
     * @return
     */
    public static long diffInSeconds(Date date) {
        Instant instant = date.toInstant();
        return Duration.between(instant, Instant.now()).getSeconds();
    }

    /**
     * HttpClient post方法.
     * 
     */
    public static String post(String url, Map<String, String> map) throws SdkException {
        CloseableHttpClient closeableHttpClient = null;
        try {
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            closeableHttpClient = httpClientBuilder.build();
            final HttpPost httppost = new HttpPost(url);
            final List<NameValuePair> params = new ArrayList<NameValuePair>();
            map.forEach((key, value) -> {
                params.add(new BasicNameValuePair(key, value));
            });
            httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            final HttpResponse response = closeableHttpClient.execute(httppost);
            // 如果状态码为200,就是正常返回
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (Exception exception) {
            throw new SdkException(Constants.UCENTER_ERROR);
        } finally {
            try {
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * HttpClient post方法.目前仅小程序使用(根据token 和param参数请求weixin.qq.com得到二维码)
     * TODO：单元测试
     */
    public static void post(String url, String param,String targetFile) throws SdkException {
        CloseableHttpClient closeableHttpClient = null;
        try {
            closeableHttpClient = HttpClientBuilder.create().build();
            final HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(param, "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpResponse resp = closeableHttpClient.execute(httpPost);
            if (resp.getStatusLine().getStatusCode() == 200) {
                System.out.println(resp.toString());
                FUtils.inputToFile(resp.getEntity().getContent(), targetFile);
            }
        } catch (Exception exception) {
            throw new SdkException(Constants.UCENTER_ERROR);
        } finally {
            try {
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * HttpClient post上传文件.
     * 
     */
    public static String post(String url, Map<String, String> headerMap, Map<String, String> bodyMap, String fileName,
        String filePath) throws SdkException {
        CloseableHttpClient closeableHttpClient = null;
        CloseableHttpResponse response = null;
        try {
            closeableHttpClient = HttpClientBuilder.create().build();
            final HttpPost httpPost = new HttpPost(url);
            final MultipartEntity customMultiPartEntity = new MultipartEntity();
            if (bodyMap != null && bodyMap.size() > 0) {
                for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
                    customMultiPartEntity.addPart(entry.getKey(),
                        new StringBody(entry.getValue(), ContentType.create("text/plain", Consts.UTF_8)));
                }
            }
            final ContentBody fileBody = new FileBody(new File(filePath));
            customMultiPartEntity.addPart(fileName, fileBody);
            httpPost.setEntity(customMultiPartEntity);
            if (headerMap != null && headerMap.size() > 0) {
                for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            response = closeableHttpClient.execute(httpPost);
            if (response != null && response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * HttpClient get方法.目前仅小程序使用得到access_token
     * 
     */
    public static String get(String url) throws SdkException {
        CloseableHttpClient closeableHttpClient = null;
        try {
            final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            closeableHttpClient = httpClientBuilder.build();
            final HttpGet httpGet = new HttpGet(url);
            final HttpResponse response = closeableHttpClient.execute(httpGet);
            // 如果状态码为200,就是正常返回
            if (response.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(response.getEntity());
                return result;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new SdkException(Constants.UCENTER_ERROR);
        } finally {
            try {
                if (closeableHttpClient != null) {
                    closeableHttpClient.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
    
    /**
     * https post
     */
    public static String postHttps(String url, Map<String, String> map) throws SdkException {
        RestTemplate rs = new RestTemplate(new HttpsClientRequestFactory());
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        map.forEach((key, value) -> {
            postParameters.add(key, value);
        });
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(postParameters);
        String result = rs.postForObject(url, entity, String.class);
        return result;
    }
    
   /**
    * V1获取用户信息
    * TODO：以后使用长链接，提高内部效率。
    * @param request
    * @return
    * @throws SdkException
    */
    public static SsoUser getUserInfo(HttpServletRequest request) throws SdkException {
    	RestTemplate rs = new RestTemplate(new HttpsClientRequestFactory());
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
        //放入标识(用户中心拦截器识别)
        postParameters.add("MTerminal", "mobile");
        //放入cookie
        HttpHeaders headers = new HttpHeaders();
        //默认请求地址
        String url = Constants.USERCENTER_GET_USER;
        //TODO sessionId方式改为通过笔的public key/private key来验证笔的身份。
        //判断是否是sessionId方式请求(目前只有笔端需要验证的接口使用sessionId方式获取用户信息)
        final String sessionId = getCookieValue(request, Constants.SESSIONKEY);
        if (StringUtils.isNotBlank(sessionId)) {
            //是则修改url
            url = Constants.USERCENTER_GET_USER_WITH_SESSIONID;
            Map<String, String> map = Constants.GSON.fromJson(sessionId, Map.class);
            String loginId = map.get(Constants.LOGINIDKEY);
            //TODO 兼容未加密的sessionid,后续删除该兼容代码
            String session = "sessionId";
            String loginIdDecrypt = AESUtils.decrypt(SSOKey.SESSIONID_KEY, loginId.replaceAll("-", "="));
            if(StringUtils.isNotBlank(loginIdDecrypt)) {
                String value = AESUtils.getValue(loginIdDecrypt);
                loginId = AESUtils.encrypt(SSOKey.SESSIONID_KEY,value).replace("=", "-");
                session = AESUtils.encrypt(SSOKey.SESSIONID_KEY, AESUtils.getSaltData("sessionId")).replace("=", "-");
            }
            headers.add("Cookie", "loginId="+loginId);
            headers.add("Cookie", "sessionId="+session);
        }else {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                headers.add("Cookie", cookie.getName()+"="+cookie.getValue());
            }
        }
        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
        
        //请求用户中心
        try {
            String string = rs.postForObject(url, r, String.class);
            //处理返回结果
            JSONObject jsonObject = JSONObject.parseObject(string);
            if (jsonObject.containsKey(Constants.RESULT)) {
                if (Constants.SUCCESS.equals(jsonObject.get(Constants.RESULT))) {
                    String tip = JSON.toJSONString(jsonObject.get(Constants.TIP));
                    SsoUser ssoUser = JSONObject.parseObject(tip, SsoUser.class);
                    return ssoUser;
                }
            }
        } catch (RestClientException e) {
            throw new SdkException(Constants.UCENTER_ERROR);
        }
        
        
        return null;
    }
    
    /**
     * V2获取用户信息
     * TODO：以后使用长链接，提高内部效率。
     * @param request
     * @return
     * @throws SdkException
     */
     public static SsoUser getUserInfoV2(String url,String authorization) throws SdkException {
         RestTemplate rs = new RestTemplate(new HttpsClientRequestFactory());
         HttpHeaders headers = new HttpHeaders();
         headers.add("Authorization", authorization);
         HttpEntity<Object> entity = new HttpEntity<>(headers);
         ResponseEntity<String> exchange = rs.exchange(url, HttpMethod.GET, entity, String.class);
         try {
             String result = exchange.getBody();
             //处理返回结果
             if (StringUtils.isNotBlank(result)) {
                 JSONObject jsonObject = JSONObject.parseObject(result);
                 String jsonString = JSON.toJSONString(jsonObject.get("data"));
                 SsoUser ssoUser = JSONObject.parseObject(jsonString, SsoUser.class);
                 if(ssoUser == null) {
                     throw new SdkException(jsonObject.getString("errorMsg"),jsonObject.getString("errorCode"),null,null);
                 }
                 return ssoUser;
             }
         } catch (RestClientException e) {
             throw new SdkException(Constants.UCENTER_ERROR);
         }
         return null;
     }
    
    /**
     * 更新用户信息
     * 判断ticket是否包含" ",包含则为V2,不包含 则为V1
     * TODO：以后使用长链接，提高内部效率。
     * @param ticket
     * @param user
     * @throws SdkException
     */
    public static Boolean updateUserInfo(String ticket,SsoUser user) throws SdkException {
    	RestTemplate rs = new RestTemplate(new HttpsClientRequestFactory());
        HttpHeaders headers = new HttpHeaders();
        String string = null;
        //判断ticket是否包含" ",包含则为Authorization,是V2版,否则V1版
        if(ticket.contains(" ")) {
            final String url = Constants.NEW_USERCENTER_USER+user.getId();
            headers.add("content-type", "application/json");
            headers.add("Authorization", ticket);
            HttpEntity<SsoUser> r = new HttpEntity<>(user, headers);
            //请求用户中心
            string = rs.postForObject(url, r, String.class);
            //处理返回结果
            if (StringUtils.isNotBlank(string)) {
                JSONObject jsonObject = JSONObject.parseObject(string);
                if (jsonObject.containsKey("errorMsg")) {
                    if ("Success".equals(jsonObject.get("errorMsg"))) {
                        return true;
                    }else {
                        throw new SdkException(jsonObject.getString("errorMsg"),jsonObject.getString("errorCode"),null,null);
                    }
                }
            }
        }else {
            //放入参数
            MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
            postParameters.add("MTerminal", "mobile");
            String userStr = JSON.toJSONString(user, SerializerFeature.WriteNullStringAsEmpty);
            postParameters.add("user", userStr);
            //放入cookie
            headers.add("Cookie", Constants.LOGINIDKEY+"="+user.getBindmobile());
            headers.add("Cookie", Constants.UCENTERKEY+"="+ticket);
            HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(postParameters, headers);
            //请求用户中心
            string = rs.postForObject(Constants.USERCENTER_UPDATE_USER, r, String.class);
            //处理返回结果
            if (StringUtils.isNotBlank(string)) {
                JSONObject jsonObject = JSONObject.parseObject(string);
                if (jsonObject.containsKey(Constants.RESULT)) {
                    if (Constants.SUCCESS.equals(jsonObject.get(Constants.RESULT))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    

    /**
     * 获取请求Ip地址, 来自diandubi项目的com.ucenter.util.CommonUtil.
     * 
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (!ip.isEmpty() && ip.indexOf(",") > 0) {
            ip = StringUtils.split(ip, ",")[0];
        }
        return ip;
    }

    public static String getScheme(HttpServletRequest request) {
        String scheme = request.getHeader("x-forwarded-proto");
        if (StringUtils.isBlank(scheme)) {
            scheme = request.getScheme();
        }
        return scheme;
    }

    public static String checkUserAgent(String userAgent) {
        if (StringUtils.isBlank(userAgent)) {
            return Constants.WRONG_USERAGENT;
        }
        try {
            final long time = Math
                .abs(System.currentTimeMillis() / 1000 - Long.valueOf(userAgent.substring(4, userAgent.length() - 4)));
            // 请求允许时间误差为1h
            if (time > 3600) {
                return Constants.TIME_ERROR;
            }
            // 验证user-agent,生成规则:四位随机数+时间戳（s）+md5(机数+时间戳)
            final String md5Hex = DigestUtils.md5Hex(userAgent.substring(0, userAgent.length() - 4));
            final int md5HexLen = md5Hex.length();
            final String md52IntResult = String.valueOf(Integer.valueOf(md5Hex.substring(md5HexLen - 4), 36));
            final String prefix = md52IntResult.length() > 4 ? md52IntResult.substring(0, 4)
                : String.format("%04d", Integer.valueOf(md52IntResult));
            final String suffix = userAgent.substring(userAgent.length() - 4);
            if (!prefix.substring(prefix.length() - 4).equals(suffix)) {
                return Constants.WRONG_USERAGENT;
            }
        } catch (Exception e) {
            return Constants.WRONG_USERAGENT;
        }
        return Constants.SUCCESS;
    }

    public static String getLoginId(HttpServletRequest httpRequest) {
        final String sessionId = getCookieValue(httpRequest, Constants.SESSIONKEY);
        String loginId = "";
        if (StringUtils.isNotBlank(sessionId)) {
            //TODO 用不同penId（超过1000次，还不超过1000次）分别做postman测试
            Map<String, String> map = Constants.GSON.fromJson(sessionId, Map.class);
            loginId = map.get(Constants.LOGINIDKEY);
            String penId = map.get(Constants.PENKEY);
            //获取解密后的信息
            final String loginIdDecrypt = AESUtils.decrypt(SSOKey.SESSIONID_KEY, loginId.replaceAll("-", "="));
            //TODO 兼容未加密的sessionid,后续删除该兼容代码
            if (StringUtils.isNotBlank(loginIdDecrypt)) {
                final String penIdDecrypt = AESUtils.decrypt(SSOKey.SESSIONID_KEY, penId.replaceAll("-", "="));
                if (AESUtils.validateSaltData(loginIdDecrypt) && AESUtils.validateSaltData(penIdDecrypt)) {
                    loginId = AESUtils.getValue(loginIdDecrypt);
                    penId = AESUtils.getValue(penIdDecrypt);
                } else {
                    return null;
                }
            }
            
            // TODO 暂时保存penId信息，以后需要对笔信息进行相关验证
            httpRequest.setAttribute(Constants.LOGINIDKEY, loginId);
            httpRequest.setAttribute(Constants.PENKEY, penId);
        } else {
            loginId = getCookieValue(httpRequest, Constants.LOGINIDKEY);
        }
        return loginId;
    }

    public static String getCookieValue(HttpServletRequest request, String key) {
        final Cookie cookie = WebUtils.getCookie(request, key);
        if (cookie != null) {
            return cookie.getValue();
        }
        return StringUtils.EMPTY;
    }
    
    /**
     * linux获取本地多张网卡ip
     *   引自：http://blog.csdn.net/luckly_p/article/details/47274531
     * 
     */
    public static boolean checkLocalIp(String temp) throws SocketException {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements()) {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                ip = (InetAddress) addresses.nextElement();
                if (ip != null && ip instanceof Inet4Address && StringUtils.isNotBlank(ip.getHostAddress())
                    && ip.getHostAddress().equals(temp)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean checkV2SSO(String authorization) {
        return StringUtils.isNotBlank(authorization);
    }
    
    /**
     * HttpClient 获取头像方法.
     * 
     */
    public static List<UserPhoto> listPhotos(List<String> loginIds) throws SdkException {
        //获取loginId
        JSONArray jsonObjects = new JSONArray();
        for (String loginId : loginIds) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("loginId", loginId);
            jsonObjects.add(jsonObject);
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("userPhotos", jsonObjects);
        RestTemplate rs = new RestTemplate();
        String string = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("content-type", "application/json");
            HttpEntity<JSONObject> r = new HttpEntity<>(jsonObject, headers);
            // 请求用户中心
            string = rs.postForObject(Constants.NEW_USERCENTER_LIST_PHOTO, r, String.class);
            // 处理返回结果
            if (StringUtils.isNotBlank(string)) {
                JSONObject object = JSONObject.parseObject(string);
                if (object.containsKey("errorMsg")) {
                    if ("Success".equals(object.get("errorMsg"))) {
                        String data = object.getString("data");
                        if (StringUtils.isNotBlank(data)) {
                            return JSONArray.parseArray(
                                    JSON.toJSONString(JSONObject.parseObject(data).get("userPhotos")), UserPhoto.class);
                        }
                    } else {
                        throw new SdkException(object.getString("errorMsg"), object.getString("errorCode"), null, null);
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new SdkException(Constants.UCENTER_ERROR);
        }
        return null;
    }
}
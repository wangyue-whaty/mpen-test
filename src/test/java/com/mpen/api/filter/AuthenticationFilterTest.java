package com.mpen.api.filter;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.hateoas.hal.Jackson2HalModule.TrueOnlyBooleanSerializer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.bean.Book;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.domain.SsoUser;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthenticationFilterTest extends TestBase{

    @Autowired
    private TestRestTemplate restTemplate;
    private static final String BASE_URI = "/v1/publishing/book?key=";
    private static final String KEY = Constants.PUBLISH_KEY;
    private static final String PHOTO ="/v1/user/photo";
    private static final String PEN_SERIALNUM ="/v1/pens/serialNum?action=getPenInfoBySerialNumOrMac&bindMobile=13001538488";
    // 出版系统可能访问的ip
    private static final String[] ALLOW_IPS = {"139.210.167.214","114.247.222.226", "47.92.157.127", "47.92.159.20","222.168.57.*",
            "221.122.122.0","221.122.122.1","221.122.122.2","221.122.122.3","221.122.122.4","221.122.122.5","221.122.122.6",
            "221.122.122.7","221.122.122.8","221.122.122.9","221.122.122.10","221.122.122.11","221.122.122.12","221.122.122.13",
            "221.122.122.14","221.122.122.15","221.122.122.16","221.122.122.17","221.122.122.18","221.122.122.19","221.122.122.20",
            "221.122.122.21","221.122.122.22","221.122.122.23","221.122.122.24","221.122.122.25","221.122.122.26","221.122.122.27",
            "221.122.122.28","221.122.122.29","221.122.122.30","221.122.122.31"};
    // 伪造不在允许访问列表的ip
    private static final String[] DISALLOW_IPS = {"192.168.1.1","114.247.222.227","47.92.159.22"};
    private static final String userName = "15963179705";
    private static final String password = "3986290380150-1529655095608-0d38af59630098b33fdb536e82ea032f";
    
    /**
     * 测试出版系统ip访问拦截,当返回状态码不为403时,代表校验通过
     */
    @Test
    public void testAllowPublish() {
        // 测试成功访问的情况
        testAllowPublish(ALLOW_IPS, true);
        // 测试访问失败的情况
        testAllowPublish(DISALLOW_IPS, false);
    }
    
    public void testAllowPublish(String[] ips, boolean expectedPass) {
        for (final String publishIp : ips) {
            // 设置认证信息
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            // 模拟请求ip
            headers.add("X-Forwarded-For", publishIp);
            final Book book = new Book();
            book.setAction("getProgress");
            book.setBookId("ad7767577dc34ec3abc9c50c7596305a");
            // 通过REST URL 发送请求
            HttpEntity<Book> request = new HttpEntity<Book>(book,headers);
            if (expectedPass) {
                ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI + KEY, HttpMethod.POST, request, Object.class);
                // 校验返回状态码是否为200
                Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
            } else {
                ResponseEntity<String> entity = restTemplate.exchange(BASE_URI + KEY, HttpMethod.POST, request, String.class);
                // 校验返回状态码是否为403
                Assert.assertEquals(entity.getStatusCode(), HttpStatus.FORBIDDEN);
            }
           
        }
    }
    

    
    /**
     * 测试没有cookie的访问是否被禁止
     * 
     */
    @Test
    public void testCheckLoginAccessFailed() {
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        // 屏蔽cookie中的loginId
//        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        HttpEntity<SsoUser> request = new HttpEntity<SsoUser>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(PHOTO + "?action=getUserPhoto", HttpMethod.GET, request,String.class);
        // 校验返回状态码是否为403
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.FORBIDDEN);
    }
    
    
    /**
     * 测试携带cookie的访问状态是否成功
     */
    @Test
    public void testCheckLoginAccessSuccess() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        HttpEntity<SsoUser> request = new HttpEntity<SsoUser>(headers);
        ResponseEntity<Object> entity = restTemplate.exchange(PHOTO + "?action=getUserPhoto", HttpMethod.GET, request,Object.class);
        // 校验返回状态码和结果
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }
    
    /**
     * 测试售后管理系统访问认证
     */
    @Test
    public void testAllowManager() {
        String loginToken = getLoginToken(userName,password);
        // params[0] 正确的Authorization与userId;params[1] 错误的Authorization与userId
        final String[][] params = { { loginToken.split(",")[0], loginToken.split(",")[1], "success" },
                { loginToken.split(",")[1], loginToken.split(",")[0],"fail" } };
        for (final String[] param : params) {
            // 设置认证信息
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.add("Authorization", param[0]);
            headers.add("userId", param[1]);
            // 通过REST URL 发送请求
            HttpEntity<DdbPePen> request = new HttpEntity<DdbPePen>(headers);
            if (param[2].equals("success")) {
                ResponseEntity<Object> entity = restTemplate.exchange(PEN_SERIALNUM + "&key=" + Constants.KEY_FOR_WYS_INVENTORY_MANAGER, HttpMethod.GET, request, Object.class);
                // 校验返回状态码是否为200
                Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
            } else if(param[2].equals("fail")){
                ResponseEntity<String> entity = restTemplate.exchange(PEN_SERIALNUM + "&key=" + Constants.KEY_FOR_WYS_INVENTORY_MANAGER, HttpMethod.GET, request, String.class);
                // 校验返回状态码是否为403
                Assert.assertEquals(entity.getStatusCode(), HttpStatus.FORBIDDEN);
            }
           
        }
    }
}

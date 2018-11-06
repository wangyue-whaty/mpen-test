/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.domain.DdbPeCustom;

/**
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ResourceBookControllerTest extends TestBase {
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URI = "/v1/books/";

    @Test
    public void testGetBookListSuccess() throws Exception { // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom(); // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId()); // 通过REST URL
                                                                   // 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI + "?action=getVolidBooks", HttpMethod.GET,
                request, Object.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetBookPageListSuccess() throws Exception {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI
            + "?action=getBookPages&bookId=4028ac305804c097015804c143730001&version=1489071876918", HttpMethod.GET,
            request, Object.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }
    
    @Test
    public void testGetBookPageListForPublishSuccess() throws Exception {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI
            + "?action=getBookPages&bookId=4028ac305804c097015804c143730001&key=publish", HttpMethod.GET,
            request, Object.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }
    
    @Test
    public void testGetBookPageListForPublishFail() throws Exception {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        // 请求路径中填写错误的key
        ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI
            + "?action=getBookPages&bookId=4028ac305804c097015804c143730001&key=failPublish", HttpMethod.GET,
            request, Object.class);
        // 校验返回状态吗和结果
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
        Map<String, Object> body = (Map<String, Object>) entity.getBody();
        Object resdata = body.get("data");
        // 检验返回参数是否为null
        Assert.assertEquals(resdata, null);
    }
}

/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import java.util.HashMap;

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

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpen.TestBase;
import com.mpen.api.bean.Message;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbPeCustom;

/**
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MessageControllerTest extends TestBase {
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URI = "/v1/messages/";

    @Test
    public void testPushAppSuccess() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        Message message = new Message();
        message.setAction("pushApp");
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<Message> request = new HttpEntity<Message>(message, headers);
        ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI, HttpMethod.POST, request, Object.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testPushAppVideoWithoutPathError() throws Exception {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        Message message = new Message();
        message.setAction("pushApp");
        message.setType(Constants.ONE);
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(" ", headers);
        ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI, HttpMethod.POST, request, Object.class);
        // 校验返回状态吗和结果
        ObjectMapper mapper = new ObjectMapper();
        String value = (String) mapper.readValue(JSON.toJSONString(entity.getBody()), HashMap.class).get("errorCode");
        Assert.assertEquals(value, "400");
    }

}

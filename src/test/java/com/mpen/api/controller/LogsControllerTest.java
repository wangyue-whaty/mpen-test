/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

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

import com.mp.shared.record.ActionRecords;
import com.mpen.TestBase;
import com.mpen.api.domain.DdbPeCustom;

/**
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class LogsControllerTest extends TestBase {
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URI = "/v1/logs/";

    @Test
    public void testSaveLogsSuccess() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        ActionRecords actionRecords = getTestActionRecords();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<ActionRecords> request = new HttpEntity<ActionRecords>(actionRecords, headers);
        ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI, HttpMethod.POST, request, Object.class);
        // 校验返回状态吗和结果
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

}

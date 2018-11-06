/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import java.util.List;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mpen.TestBase;
import com.mpen.api.bean.PenInfo;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbPePen;

/**
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PePenControllerTest extends TestBase {
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URI = "/v1/pens";
    private static final String SERIALNUM = BASE_URI + "/serialNum";

    @Test
    public void testCheckBindSuccess() throws Exception {
        DdbPePen pen = getTestDdbPePen();
        DdbPeCustom peCustom = getTestDdbPeCustom();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        String identifiaction = pen.getIdentifiaction();
        HttpEntity<PenInfo> request = new HttpEntity<PenInfo>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(BASE_URI + identifiaction + "?action=checkBind",
            HttpMethod.GET, request, String.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testUpgradeAppSuccess() throws Exception {
        DdbPePen pen = getTestDdbPePen();
        DdbPeCustom peCustom = getTestDdbPeCustom();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL发送请求
        String identifiaction = pen.getIdentifiaction();
        HttpEntity<String> request = new HttpEntity<String>(headers);
        // TODO 修改泛型类型，修改数据库参数，添加回滚注解@Transactional
        ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI + identifiaction + "?action=upgradeApp",
                HttpMethod.GET, request, Object.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testUpgradeRomSuccess() throws Exception {
        DdbPePen pen = getTestDdbPePen();
        DdbPeCustom peCustom = getTestDdbPeCustom();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL发送请求
        String identifiaction = pen.getIdentifiaction();
        HttpEntity<String> request = new HttpEntity<String>(headers);
        // TODO 修改泛型类型，修改数据库参数，添加回滚注解@Transactional
        ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI + identifiaction + "?action=upgradeRom",
                HttpMethod.GET, request, Object.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testAdbAdmitSuccess() throws Exception {
        DdbPePen pen = getTestDdbPePen();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        // 通过REST URL发送请求
        String identifiaction = pen.getIdentifiaction();
        HttpEntity<String> request = new HttpEntity<String>(headers);
        // TODO 修改泛型类型，修改数据库参数，添加回滚注解@Transactional
        ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI + identifiaction + "?action=adbAdmit",
                HttpMethod.GET, request, Object.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testUnBindPenSuccess() throws Exception {
        DdbPeCustom peCustom = getTestDdbPeCustom();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI
            + "?action=unBindPen&macAddress=MPEN56:CF:B4:9B:C3:8A__MPEN7B:24:B7:38:C3:8A", HttpMethod.GET, request,
                Object.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testCompleteMacSuccess() throws Exception {
        DdbPeCustom peCustom = getTestDdbPeCustom();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(BASE_URI + "?action=completeMac&macAddress=56CFB4",
                HttpMethod.GET, request, String.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }
    @Test
    public void testGetPenIdAndMac() throws Exception {
        // 测试一支教师笔,一支非教师笔
        final String[][] params = {{"V917060006644","isNotTeacher"},{"V917060006641","isTeacher"}};
        for (String[] serialNumber : params) {
            DdbPeCustom peCustom = getTestDdbPeCustom();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.add("Cookie", "loginId=" + peCustom.getLoginId());
            // 通过REST URL发送请求
            HttpEntity<String> request = new HttpEntity<String>(headers);
            ResponseEntity<Object> entity = restTemplate.exchange(SERIALNUM + "?action=PenIdAndMac&serialNumber=" + serialNumber[0],
                HttpMethod.GET, request, Object.class); // 校验返回状态码和结果
            Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
            Map<String, Object> jsonObject = (Map<String, Object>) entity.getBody();
            final String jsonString = JSON.toJSONString(jsonObject.get("data"));
            final DdbPePen ddbPePen = JSONObject.parseObject(jsonString,DdbPePen.class);
            if (serialNumber[1].equals("isNotTeacher")) {
                Assert.assertEquals(0 , ddbPePen.getIsTeacher().intValue());
            } else if (serialNumber[1].equals("isTeacher")) {
                Assert.assertEquals(1 , ddbPePen.getIsTeacher().intValue());
            }
        }
    }
    
    @Test
    public void testGetPenIdAndMscAndIsBand(){
        //用户登录
        getLoginToken(getTestCustom().getUserName(), getTestCustom().getPassword());
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + getTestCustom().getUserName());
        // 通过REST URL发送请求
        final HttpEntity<Object> request = new HttpEntity<Object>(headers);
        final ResponseEntity<Object> entity = restTemplate.exchange(SERIALNUM + "?action=PenIdAndMac&serialNumber=V8C18060030454",
            HttpMethod.GET, request, Object.class); // 校验返回状态码和结果
        final Map<String, String> body = (Map<String, String>)entity.getBody();
        final Object resdata = body.get("data");
        final String data = "{id=0004b649c7fb4212a9bebda5452b6f1f, macAddress=9e:07:54:4f:c0:f2, isBind=1, isTeacher=0.0, storageCapacity=0.0}";
        Assert.assertEquals(data, resdata.toString());    
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);  
    }
    
    @Test
    public void testunBindPenSuccess() throws Exception {
        // 测试一支教师笔,一支非教师笔
        final String[][] params = {{"98:5F:B3:0B:C3:8A","isTeacher"},{"BA:52:0A:0F:C3:8A","isNotTeacher"}};
        for (String[] macAddress : params) {
            DdbPeCustom peCustom = getTestDdbPeCustom();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.add("Cookie", "loginId=" + peCustom.getLoginId());
            // 通过REST URL发送请求
            HttpEntity<String> request = new HttpEntity<String>(headers);
            ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI + "?action=unBindPen&macAddress="+ macAddress[0],
                HttpMethod.GET, request, Object.class); // 校验返回状态吗和结果
            Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
            Map<String, Object> jsonObject = (Map<String, Object>) entity.getBody();
            List<Map<String, Object>> penMacList = (List<Map<String, Object>>) jsonObject.get("data");
            for (int i=0;i<penMacList.size();i++) {
                // Gson将字符串转map时,int、long默认为double类型
                if (macAddress[1].equals("isNotTeacher")) {
                    Assert.assertEquals(0.0, penMacList.get(i).get("isTeacher"));
                } else if (macAddress[1].equals("isTeacher")) {
                    Assert.assertEquals(1.0, penMacList.get(i).get("isTeacher"));
                }
            }
            
        }
    }
}

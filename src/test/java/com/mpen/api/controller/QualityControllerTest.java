/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import java.io.File;
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

import com.mpen.TestBase;
import com.mpen.api.bean.Pen;
import com.mpen.api.bean.PenInfo;
import com.mpen.api.common.Constants.PenType;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.mapper.PePenMapper;
import com.mpen.api.mapper.PenSerialNumberRelationshipMapper;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.RSAUtils;

/**
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class QualityControllerTest extends TestBase {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    PePenMapper pePenMapper;
    @Autowired
    PenSerialNumberRelationshipMapper penSerialNumberRelationshipMapper;

    private static final String BASE_URI = "/v1/qa";

    private static final String PENS_URI = BASE_URI + "/pens";

    private static final String BOOKS_URI = BASE_URI + "/books";

    private static final String AUDIOS_URI = BASE_URI + "/audios";

    private static final String PEN_URI = BASE_URI + "/pens/serialNum";

    /**
     * 激活笔测试
     * 新增笔类型判断
     * @throws Exception
     */
    @Test
    public void testPassQaSuccess() throws Exception {
        String filepath = "D:\\temp";
        File file = new File(filepath);
        if (!file.exists()) {
            file.mkdir();
        }
        String[] strArr = RSAUtils.getKeyPairStringArray(filepath);
        DdbPePen pen = getTestDdbPePen();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        // 通过REST URL 发送请求
        String identifiaction = pen.getIdentifiaction();
        String macAddress = pen.getMacAddress();
        String code = CommUtil.getPenCode(identifiaction);
        PenInfo penInfo = new PenInfo();
        penInfo.setAction("passQa");
        penInfo.setCode(code);
        penInfo.setMacAddress(macAddress);
        penInfo.setPublicKey(strArr[0]);
        penInfo.setActiveAdd("深圳");
        penInfo.setStorageCapacity(9999);
        final PenType[] testTypes = { PenType.ANDROID, PenType.LINUX, null };
        for (final PenType testType: testTypes) {
            penInfo.setType(testType);
            HttpEntity<PenInfo> request = new HttpEntity<PenInfo>(penInfo, headers);
            ResponseEntity<Object> entity = restTemplate.exchange(PENS_URI + identifiaction, HttpMethod.POST, request,
                Object.class);
            // 校验返回状态吗和结果
            Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK); 
            DdbPePen ddbPePen = pePenMapper.getByIdentifiaction(identifiaction);
            // 校验插入与查询的StorageCapacity字段内容是否一致
            Assert.assertEquals(penInfo.getStorageCapacity(), ddbPePen.getStorageCapacity()); 
        }
    }
    
    /**
     * 测试获取文件密钥
     * @throws Exception
     */
    @Test
    public void testGetFileRSAKeySuccess() throws Exception {
        String filepath = "D:\\temp";
        File file = new File(filepath);
        if (!file.exists()) {
            file.mkdir();
        }
        String[] strArr = RSAUtils.getKeyPairStringArray(filepath);
        DdbPePen pen = getTestDdbPePen();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        // 通过REST URL 发送请求
        String identifiaction = pen.getIdentifiaction();
        String macAddress = pen.getMacAddress();
        String code = CommUtil.getPenCode(identifiaction);
        PenInfo penInfo = new PenInfo();
        penInfo.setAction("fileRSAkey");
        penInfo.setCode(code);
        penInfo.setMacAddress(macAddress);
        penInfo.setPublicKey(strArr[0]);
        penInfo.setActiveAdd("深圳");
        List<String> fileNameList = getFileNameList();
        penInfo.setFileNameList(fileNameList );
        final PenType[] testTypes = { PenType.ANDROID, PenType.LINUX, null };
        for (final PenType testType: testTypes) {
            penInfo.setType(testType);
            HttpEntity<PenInfo> request = new HttpEntity<PenInfo>(penInfo, headers);
            ResponseEntity<Object> entity = restTemplate.exchange(PENS_URI + identifiaction, HttpMethod.POST, request,
                Object.class);
            // 校验返回状态吗和结果
            Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK); 
            Map<String, Object> body = (Map<String, Object>) entity.getBody();
            List<Map<String, Object>> resdata = (List<Map<String, Object>>)body.get("data");
            // 遍历fileNameList 得到fileName,secretData 判断是否为null
            for (Map map : resdata) {
                Assert.assertEquals(true, map.get("fileName") != null);
                Assert.assertEquals(true, map.get("secretData") != null);
            }
        }
    }
    
    @Test
    public void testPassQaWithoutCodeOrMacError() throws Exception {
        DdbPePen pen = getTestDdbPePen();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        // 通过REST URL 发送请求
        PenInfo penInfo = new PenInfo();
        penInfo.setAction("passQa");
        HttpEntity<PenInfo> request = new HttpEntity<PenInfo>(penInfo, headers);
        ResponseEntity<Object> entity = restTemplate.exchange(PENS_URI + pen.getIdentifiaction() + "?action=passQa",
            HttpMethod.POST, request, Object.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testPredownloadSuccess() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<Object> entity = restTemplate.exchange(BOOKS_URI + "?action=preDownload", HttpMethod.GET,
            request, Object.class);
        // 校验返回状态码
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testAudiosTestSuccess() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<Object> entity = restTemplate.exchange(AUDIOS_URI + "?action=audiosTest", HttpMethod.GET,
            request, Object.class);
        // 校验返回状态码
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testUploadSerialNumberSuccess() throws Exception {
        // 两种类型的笔测试:V8C,V9
        final String[] serialNum = { "V917060000078", "V8C18070000001" };
        for(String num : serialNum) {
            Pen testPen = getTestPen(num);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            // 通过REST URL 发送请求
            testPen.setAction("uploadSerialNumber");
            HttpEntity<Pen> request = new HttpEntity<Pen>(testPen, headers);
            ResponseEntity<Map> entity = restTemplate.exchange(PEN_URI, HttpMethod.POST, request, Map.class);
            // 校验返回状态吗和结果
            Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
            // Assert.assertEquals(entity.getBody().get("data"), true);
            penSerialNumberRelationshipMapper.deleteBySerialNumber(testPen.getSerialNumber());
        }
        
    }
    
}

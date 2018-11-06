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

import com.mpen.TestBase;
import com.mpen.api.domain.DdbPeCustom;

/**
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ShopControllerTest extends TestBase {
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URI = "/v1/shops";
    private static final String GOODS_URI = BASE_URI + "/goods";

    @Test
    public void testGetTopGoodsSuccess() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(GOODS_URI + "?action=getTopGoods", HttpMethod.GET,
            request, String.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetGoodsByBookGradeAndTypeSuccess() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(GOODS_URI
            + "?action=getGoods&suitGrade=小一,初一&bookType=课外阅读", HttpMethod.GET, request, String.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetGoodsByBookTypeSuccess() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(GOODS_URI + "?action=getGoods&bookType=课外阅读",
            HttpMethod.GET, request, String.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetGoodsByBookGradeSuccess() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(GOODS_URI + "?action=getGoods&suitGrade=小一,初一",
            HttpMethod.GET, request, String.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetGoodsByBookNameSuccess() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        // TODO 修改泛型类型，修改数据库参数，添加回滚注解@Transactional
        ResponseEntity<Object> entity = restTemplate.exchange(GOODS_URI + "?action=getGoods&bookName=小粉猪",
                HttpMethod.GET, request, Object.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetGoodsPhotoByIdsSuccess() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(GOODS_URI
            + "?action=getBooksPhoto&bookId=2212c08952604e94be2e4b85cfadacd2__222febb5b7144d158864dcadb15caf29",
            HttpMethod.GET, request, String.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetGoodsByBookIdsSuccess() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        // TODO 修改泛型类型，修改数据库参数，添加回滚注解@Transactional
        ResponseEntity<Object> entity = restTemplate
            .exchange(GOODS_URI + "?action=getGoodsByBookId&bookId=2c9450815114a265015114a2bf3b0007", HttpMethod.GET,
                        request, Object.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    /**
     * 获取教学资源展示列表测试
     */
    @Test
    public void testGetGoodsTeachLinkSuccess() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<Object> request = new HttpEntity<Object>(headers);
        ResponseEntity<Object> entity = restTemplate.exchange(GOODS_URI
            + "?action=getGoodsTeachLink&systemType=GARDENER", HttpMethod.GET, request, Object.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }
}

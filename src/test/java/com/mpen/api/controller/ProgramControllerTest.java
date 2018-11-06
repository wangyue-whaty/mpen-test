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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mpen.TestBase;
import com.mpen.api.bean.PenInfo;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Uris;
import com.mpen.api.domain.DdbPePen;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ProgramControllerTest extends TestBase{

    @Autowired
    private TestRestTemplate restTemplate;
    
    private static final String BASE_URI = "/v1/program";
    
    /**
     * 测试获取微信扫一扫需要的signature签名
     * 只能校验是否从微信服务端获取到签名信息,无法做校验是否正确,校验是由微信端来做的.
     * @throws Exception
     */
    @Test
    public void testGetWechatQrCodeSuccess() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Authorization", getLoginToken("15963179705", "3986290380150-1529655095608-0d38af59630098b33fdb536e82ea032f"));
        // 通过REST URL 发送请求
        HttpEntity<PenInfo> request = new HttpEntity<PenInfo>(headers);
        ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI + Uris.CODE + "?action="+ Constants.GET_QR_CODE +"&reqUrl=http://test.com",
            HttpMethod.GET, request, Object.class);
        Map<String,Object> map = (Map<String,Object>)entity.getBody();
        // 校验返回状态吗和结果
        Assert.assertEquals(entity.getStatusCode() != null, true);
        Assert.assertEquals(map.get("errorCode"), "200");
        Assert.assertEquals(map.get("errorMsg"), "Success");
        // 校验所有的返回值不为null
        Map<String,Object> resultMap = (Map<String,Object>)map.get("data");
        Assert.assertEquals(resultMap.get("jsapi_ticket")!= null, true);
        Assert.assertEquals(resultMap.get("url")!= null, true);
        Assert.assertEquals(resultMap.get("nonceStr")!= null, true);
        Assert.assertEquals(resultMap.get("timestamp")!= null, true);
        Assert.assertEquals(resultMap.get("signature")!= null, true);
    }
}

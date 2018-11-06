package com.mpen.api.controller;

import org.apache.commons.codec.binary.Base64;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import com.mpen.TestBase;
import com.mpen.api.bean.Dynamic;
import com.mpen.api.common.Constants;

/**
 * 动态controller测试类
 * 
 * @author sxg
 * @date 2018年8月17日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DynamicControllerTest extends TestBase {
    private static final String userName = "13717521920";
    private static final String password = "3986290380150-1529655095608-e4c779a27cd390537a936a82a81fe0e3";
    private static final String DYNAMICLIST_URL = "/v1/dynamic/dynamicList/";
    private static final String EDIT_COVER_URL = "/v1/dynamic/editCover/";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testDynamicController() {
        // 初始化数据
        final Dynamic dynamic = new Dynamic();
        dynamic.setAction(Constants.DYNAMIC);
        dynamic.setPageNo(1);
        dynamic.setPageSize(10);
        // 测试动态列表
        this.postControllerTest(userName, password, dynamic, DYNAMICLIST_URL);
        dynamic.setAction(Constants.MYDYNAMIC);
        // 测试我的动态
        this.postControllerTest(userName, password, dynamic, DYNAMICLIST_URL);
        dynamic.setAction(Constants.MYDYNAMIC);
        dynamic.setLoginId("18931334240");
        // 测试好友的动态
        this.postControllerTest(userName, password, dynamic, DYNAMICLIST_URL);
        dynamic.setAction(Constants.COVER);
        // 测试封面
        this.postControllerTest(userName, password, dynamic, DYNAMICLIST_URL);
        dynamic.setAction(Constants.PRAISE);
        dynamic.setId("4");
        // 测试点赞
        this.postControllerTest(userName, password, dynamic, DYNAMICLIST_URL);
    }

    /**
     * 测试编辑封面
     */
    @Test
    public void testEditCover() {
        final String base64String = "iVBORw0KGgoAAAANSUhEUgAAACcAAAAbCAYAAAD/G5bjAAABJElEQVRYhe2W3"
                + "Q2DIBCAD9Mh7GOTDtGuYaMPHYe6QkcQkw4i3cMHu4XFX8SAIGrUxC+5hzOHfHD2UpQzY"
                + "KM4awsMccjZcsjZsmO5+AGAUBXxT/+2pv5bJKw+QJ3cglwFDYsByMOPlKUVSV3r5XnK0"
                + "jSq81CzTo1critGWY4NBJs1TQ3xxMPJgo6VU4m0wvXNCCR8Q5LxHCf8feXzcXC5tg2GU"
                + "Wzc0L2lQgJ32jlZrt8SLWwjvxboH4pEYsuw4nAGsuofhDFJ1WrsyTedcHPiKHkhPjp00Y"
                + "6HCwB9A1wtx8UA8jlHB7423C92AW7u/GZKuSV4nsWbD4h2iWNSNAskEzvweWqXnKRP72"
                + "huNSsc6QlGfXPLgcp5slF2/JdpZQ45Ww45W/7U3lPDydKfswAAAABJRU5ErkJggg==";

        final byte[] data = Base64.decodeBase64(base64String);
        final MultipartFile file = new MockMultipartFile("photo", "hello.jpg", "image/jpeg", data);
        final Dynamic dynamic = new Dynamic();
        dynamic.setFile(file);
        final MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
        param.add("dynamic", dynamic);
        // 获取认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.MULTIPART_FORM_DATA);
        headers.add("Authorization", getLoginToken(userName, password));

        // 通过REST URL 发送请求
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<MultiValueMap<String, Object>>(param,
                headers);
        ResponseEntity<Object> entity = restTemplate.exchange(EDIT_COVER_URL, HttpMethod.POST, request, Object.class);
        // 校验返回状态吗和结果
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }
}

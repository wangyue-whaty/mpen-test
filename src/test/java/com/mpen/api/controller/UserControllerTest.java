/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.controller;

import java.util.HashMap;
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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpen.TestBase;
import com.mpen.api.bean.Pen;
import com.mpen.api.bean.User;
import com.mpen.api.bean.UserInfo;
import com.mpen.api.bean.UserSession;
import com.mpen.api.bean.WeeklyParam;
import com.mpen.api.common.Constants;
import com.mpen.api.common.Constants.PenType;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbPePen;
import com.mpen.api.domain.SsoUser;
import com.mpen.api.mapper.PePenMapper;
import com.mpen.api.mapper.RecordExamDetailMapper;
import com.mpen.api.service.PeCustomService;
import com.mpen.api.service.PrPenCustomService;
import com.mpen.api.service.SsoUserService;

/**
 * @author zyt
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserControllerTest extends TestBase {
    @Autowired
    private TestRestTemplate restTemplate;

    private static final String BASE_URI = "/v1/user";
    private static final String SAVE_SAVEBINDRELATIONSHIP_URI = BASE_URI + "/pen";
    private static final String GET_USER_INFO = BASE_URI + "/book";
    private static final String LOGIN = BASE_URI + "/login";
    private static final String CHANGE_USER_INFO = BASE_URI + "/";
    private static final String WEEKLY = BASE_URI + "/weekly";
    private static final String PHOTO = BASE_URI + "/photo";

    @Autowired
    SsoUserService ssoUserService;

    @Autowired
    PeCustomService peCustomService;

    @Autowired
    PrPenCustomService prPenCustomService;

    @Autowired
    PePenMapper pePenMapper;

    @Autowired
    RecordExamDetailMapper recordExamDetailMapper;

    @Test
    public void testSaveBindRelationshipSuccess() throws Exception {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        DdbPePen pen = getTestDdbPePen();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        Pen penParam = new Pen();
        penParam.setAction(Constants.SAVE_BINDRELATIONSHIP);
        penParam.setIdentifiaction(pen.getIdentifiaction());
        penParam.setMacAddress(pen.getMacAddress());
        // 通过REST URL 发送请求
        HttpEntity<Pen> request = new HttpEntity<Pen>(penParam, headers);
        // TODO 修改泛型类型，修改数据库参数，添加回滚注解@Transactional
        ResponseEntity<Object> entity = restTemplate.exchange(SAVE_SAVEBINDRELATIONSHIP_URI, HttpMethod.POST, request,
                Object.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testSaveBindRelationshipWithoutIdOrMacError() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        Pen penParam = new Pen();
        penParam.setAction(Constants.SAVE_BINDRELATIONSHIP);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<Pen> request = new HttpEntity<Pen>(penParam, headers);
        restTemplate.exchange(SAVE_SAVEBINDRELATIONSHIP_URI, HttpMethod.POST, request, String.class);
        // TODO 进行结果校验
    }

    @Test
    public void testLoginSuccess() throws Exception {
        User user = new User();
        // 登陆校验返回是否绑定过android笔信息.
        final String userPasses[][] = { { "123123213", "b59c67bf196a4758191e42f76670ceba", null },
                { "15963179705", "e10adc3949ba59abbe56e057f20f883e", PenType.ANDROID.toString() } };
        for (String[] userPass : userPasses) {
            user.setUserName(userPass[0]);
            user.setPassword(userPass[1]);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            // 通过REST URL 发送请求
            HttpEntity<User> request = new HttpEntity<User>(user, headers);
            ResponseEntity<String> entity = restTemplate.exchange(LOGIN, HttpMethod.POST, request, String.class);
            // 校验返回状态吗和结果
            Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
            final ObjectMapper mapper = new ObjectMapper();
            final HashMap<String, Object> jsonObject = mapper.readValue(entity.getBody(), HashMap.class);
            final String jsonString = JSON.toJSONString(jsonObject.get("data"));
            final UserInfo userInfo = JSONObject.parseObject(jsonString, UserInfo.class);
            if (userPass[2] == PenType.ANDROID.toString()) {
                Assert.assertEquals(userInfo.isHasBoundAndroidPen(), true);
            } else {
                Assert.assertEquals(userInfo.isHasBoundAndroidPen(), false);
            }

        }
    }

    @Test
    public void testLoginWithWrongLoginIdError() throws Exception {
        User user = new User();
        user.setUserName("1");
        user.setPassword("b59c67bf196a4758191e42f76670ceba");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        // 通过REST URL 发送请求
        HttpEntity<User> request = new HttpEntity<User>(user, headers);
        ResponseEntity<HashMap> entity = restTemplate.exchange(LOGIN, HttpMethod.POST, request, HashMap.class);
        // 校验返回状态吗和结果
        Assert.assertEquals(entity.getBody().get("errorCode"), "400");
        Assert.assertEquals(entity.getBody().get("errorMsg"), "账号不存在");
    }

    @Test
    public void testLoginWithWrongPasswordError() throws Exception {
        User user = new User();
        user.setUserName("123123213");
        user.setPassword("a");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        // 通过REST URL 发送请求
        HttpEntity<User> request = new HttpEntity<User>(user, headers);
        ResponseEntity<HashMap> entity = restTemplate.exchange(LOGIN, HttpMethod.POST, request,
                HashMap.class);
        // 校验返回状态吗和结果
        Assert.assertEquals(entity.getBody().get("errorCode"), "400");
        Assert.assertEquals(entity.getBody().get("errorMsg"), "密码错误");
    }

    @Test
    public void testUnBindRelationshipSuccess() throws Exception {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        DdbPePen pen = getTestDdbPePen();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        Pen penParam = new Pen();
        penParam.setAction(Constants.UN_BINDRELATIONSHIP);
        penParam.setIdentifiaction(pen.getIdentifiaction());
        // 通过REST URL 发送请求
        HttpEntity<Pen> request = new HttpEntity<Pen>(penParam, headers);
        ResponseEntity<String> entity = restTemplate.exchange(SAVE_SAVEBINDRELATIONSHIP_URI, HttpMethod.POST, request,
                String.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetUserStudyInfoSuccess() throws Exception {
        // 创建一个测试用户
        getLoginToken(getTestUser().getUserName(), getTestUser().getPassword());
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + getTestUser().getUserName());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<Object> entity = restTemplate.exchange(
                GET_USER_INFO + "?action=" + Constants.COMPLETE_USER_STUDY_INFO, HttpMethod.GET, request, Object.class);
        // 校验返回状态吗和结果
        Assert.assertEquals(HttpStatus.OK, entity.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) entity.getBody();
        Object resdata = body.get("data");
        JSONObject json = (JSONObject) JSONObject.toJSON(resdata);
        JSONArray jArray = (JSONArray) json.get("books");
        // 遍历books 得到type 判断type是否为null
        for (int i = 0; i < jArray.size(); i++) {
            String type = (String) jArray.getJSONObject(i).get("type");
            Assert.assertEquals(true, type != null);
        }
    }

    @Test
    public void testGetUserStudyInfoReadLevelSuccess() throws Exception {
        // 创建一个readLevel为4的测试用户
        UserSession userSession1 = getTestUserSessionReadLevelFour();
        // 创建一个readLevel为null的测试用户
        UserSession userSession2 = getTestUserSessionReadLevelNull();
        // 获取阅读分级4
        final Integer readLevel1 = recordExamDetailMapper.getReadLevel(userSession1.getLoginId());
        // 检验返回阅读分级4
        Assert.assertEquals(readLevel1.intValue(), 4);

        // 获取阅读分级null
        final Integer readLevel2 = recordExamDetailMapper.getReadLevel(userSession2.getLoginId());
        // 检验返回阅读分级null
        Assert.assertNull(readLevel2);

        // 创建一个readLevel为11的测试用户
        UserSession userSession3 = getTestUserSessionReadLevelEleven();
        // 获取阅读分级4
        final Integer readLevel3 = recordExamDetailMapper.getReadLevel(userSession3.getLoginId());
        // 检验返回阅读分级4
        Assert.assertEquals(readLevel3.intValue(), 11);
    }

    @Test
    public void testGetBooktudyInfoSuccess() throws Exception {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(
                GET_USER_INFO + "?action=" + Constants.BOOK_STUDY_INFO + "&bookId=4028ac305804c097015804c143730001",
                HttpMethod.GET, request, String.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetBooktudyInfoWithWrongBookIdError() throws Exception {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(
                GET_USER_INFO + "?action=" + Constants.BOOK_STUDY_INFO + "&bookId=123", HttpMethod.GET, request,
                String.class);
        // 校验返回状态吗和结果
        ObjectMapper mapper = new ObjectMapper();
        String value = (String) mapper.readValue(entity.getBody(), HashMap.class).get("errorCode");
        // TODO 进行结果校验
        Assert.assertEquals(value, "400");
    }

    @Test
    public void testBookContentStudyDetailSuccess() throws Exception {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(GET_USER_INFO + "?action="
                + Constants.BOOK_CONTENT_STUDY_DETAIL + "&bookId=4028ac305804c097015804c143730001", HttpMethod.GET,
                request, String.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testBookContentSpokenDetailSuccess() throws Exception {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<String> request = new HttpEntity<String>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(GET_USER_INFO + "?action="
                + Constants.BOOK_CONTENT_SPOKEN_DETAIL + "&bookId=4028ac305804c097015804c143730001", HttpMethod.GET,
                request, String.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testChangeUserInfoSuccess() throws Exception {
        User user = new User();
        user.setNickName("zyt");
        user.setGrade("624c1a98-fd8c-11e6-9f75-c81f66dbee68");
        user.setAction(Constants.CHANGE_USER_INFO);
        user.setAge(12);
        user.setSchool("北大");
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<User> request = new HttpEntity<User>(user, headers);
        ResponseEntity<String> entity = restTemplate.exchange(CHANGE_USER_INFO, HttpMethod.POST, request, String.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetUserLabelsSuccess() throws Exception {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<User> request = new HttpEntity<User>(headers);
        ResponseEntity<String> entity = restTemplate.exchange(CHANGE_USER_INFO + "?action=getUserLabels",
                HttpMethod.GET, request, String.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetAllWeeklys() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        // 通过REST URL 发送请求
        HttpEntity<WeeklyParam> request = new HttpEntity<WeeklyParam>(headers);
        ResponseEntity<Map> entity = restTemplate.exchange(WEEKLY + "?action=getAllWeeklys", HttpMethod.GET, request,
                Map.class);
        // 校验返回状态吗和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testGetUserPhoto() {
        // 创建一个测试用户
        DdbPeCustom peCustom = getTestDdbPeCustom();
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        headers.add("Cookie", "loginId=" + peCustom.getLoginId());
        HttpEntity<SsoUser> request = new HttpEntity<SsoUser>(headers);
        ResponseEntity<Map> entity = restTemplate.exchange(PHOTO + "?action=getUserPhoto", HttpMethod.GET, request,
                Map.class);
        // 校验返回状态码和结果
        // TODO 进行结果校验
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
    }

    @Test
    public void testUnBindRelationshipAgentSuccess() throws Exception {
        // 同一支笔发两次请求,第一次成功解绑,第二次解绑失败
        final String[][] params = { { "V917060006573", null, "success", "doSaveBind" },
                { "V917060006573", null, "fail", "" }, { null, "54:60:BC:0D:C3:8A", "success", "doSaveBind" },
                { null, "54:60:BC:0D:C3:8A", "fail", "" } };
        for (final String[] param : params) {
            // 第一次和第二次绑定笔
            if ("doSaveBind".equals(param[3])) {
                testSaveBindRelationshipSuccess();
            }
            // 创建一个测试用户
            DdbPeCustom peCustom = getTestDdbPeCustom();
            // 设置认证信息
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.add("Cookie", "loginId=" + peCustom.getLoginId());
            Pen penParam = new Pen();
            penParam.setAction(Constants.UN_BINDRELATIONSHIP);
            penParam.setSerialNumber(param[0]);
            penParam.setMacAddress(param[1]);
            // 第一，二次通过序列号解绑，第三，四次通过Mac地址解绑
            HttpEntity<Pen> request = new HttpEntity<Pen>(penParam, headers);
            ResponseEntity<Object> entity = restTemplate.exchange(
                    SAVE_SAVEBINDRELATIONSHIP_URI + "?agentOperateKey=" + Constants.AGENT_OPERATE_KEY, HttpMethod.POST,
                    request, Object.class);
            Map<String, Object> jsonObject = (Map<String, Object>) entity.getBody();
            // 校验返回状态吗和结果
            Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
            if (param[2].equals("success")) {
                Assert.assertEquals(jsonObject.get("errorCode"), "200");
            } else {
                Assert.assertEquals(jsonObject.get("errorCode"), "400");
                Assert.assertEquals(jsonObject.get("errorMsg"), "该笔没有绑定信息!");
            }
        }
    }
}

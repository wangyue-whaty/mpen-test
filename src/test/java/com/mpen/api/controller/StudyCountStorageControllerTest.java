package com.mpen.api.controller;

import java.util.List;

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
import com.mp.shared.common.NetworkResult;
import com.mpen.TestBase;
import com.mpen.api.bean.StudyCountAssist;
import com.mpen.api.common.Uris;
import com.mpen.api.domain.DdbLearnLogBookDetailTrace;
import com.mpen.api.domain.DdbLearnLogBookSumTrace;
import com.mpen.api.domain.DdbLearnLogBookTrace;
import com.mpen.api.domain.DdbLearnLogDayTrace;

/**
 * 学情日志统计存储接口测试类
 * @author hzy
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class StudyCountStorageControllerTest extends TestBase {
    @Autowired
    private TestRestTemplate restTemplate;
    
    /**
     * 保存用户每日学习详情统计测试
     */
    @Test
    public void learnLogDayTraceTest() {
        final List<DdbLearnLogDayTrace> logDayTraces = listLogDayTraces();
        final String uri = Uris.V1_STUDY_COUNT + Uris.LEARNLOG_DAYTRACE;
        baseTraceTest(logDayTraces, uri);
    }
    
    /**
     * 保存用户学习书籍内容详情统计测试
     */
    @Test
    public void learnLogBookDetailTraceTest() {
        final List<DdbLearnLogBookDetailTrace> logDayTraces = listLogBookDetailTraces();
        final String uri = Uris.V1_STUDY_COUNT + Uris.LEARNLOG_BOOKDETAILTRACE;
        baseTraceTest(logDayTraces, uri);
    }
    
    /**
     * 保存用户学习书籍列表统计测试
     */
    @Test
    public void learnLogBookSumTraceTraceTest() {
        final List<DdbLearnLogBookSumTrace> logBookSumTraces = listLogBookSumTraces();
        final String uri = Uris.V1_STUDY_COUNT+Uris.LEARNLOG_BOOKSUMTRACE;
        baseTraceTest(logBookSumTraces, uri);
    }
    
    /**
     * 保存用户书籍学习轨迹统计测试
     */
    @Test
    public void learnLogBookTraceTraceTest() {
        final List<DdbLearnLogBookTrace> logBookTraces = listLogBookTraces();
        final String uri = Uris.V1_STUDY_COUNT+Uris.LEARNLOG_BOOKTRACE;
        baseTraceTest(logBookTraces, uri);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private <T> void baseTraceTest(List<T> traces,String uri) {
        // 设置认证信息
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        // 通过REST URL 发送请求
        final HttpEntity<List<T>> request = new HttpEntity<List<T>>(traces, headers);
        final ResponseEntity<NetworkResult> entity = restTemplate.exchange(uri, HttpMethod.POST, request,
                NetworkResult.class);
        // 校验返回状态码
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
        // 校验返回结果
        final NetworkResult<Boolean> result = entity.getBody();
        Assert.assertEquals(result.getErrorCode(), "200");
        Assert.assertTrue(result.getData());
    }
    
    /**
     * 获取书籍类型测试
     */
    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void bookTypeTest() {
        // 设置认证信息
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        final String fkBookId = "ff808081533ba5a801533bbd358f0003";
        final StudyCountAssist countAssist = new StudyCountAssist(null, fkBookId, null);
        // 通过REST URL 发送请求
        final HttpEntity<StudyCountAssist> request = new HttpEntity<StudyCountAssist>(countAssist, headers);
        final ResponseEntity<NetworkResult> entity = restTemplate.exchange(Uris.V1_STUDY_COUNT+Uris.BOOK_TYPE, HttpMethod.POST, request,
                NetworkResult.class);
        // 校验返回状态码
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
        // 校验返回结果
        final NetworkResult<String> result = entity.getBody();
        Assert.assertEquals(result.getErrorCode(), "200");
        Assert.assertEquals(result.getData(), "STUDY");
    }
    
    /**
     * 获取用户学习书籍轨迹测试
     */
    @Test
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void studyPageTest() {
        // 设置认证信息
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        final String fkBookId = "ff808081581deb4101581e74ac7d0088";
        final String fkloginId = "13906030919";
        final String studyDate = "2018-03-19";
        final StudyCountAssist countAssist = new StudyCountAssist(fkloginId, fkBookId, studyDate);
        // 通过REST URL 发送请求
        final HttpEntity<StudyCountAssist> request = new HttpEntity<StudyCountAssist>(countAssist, headers);
        final ResponseEntity<NetworkResult> entity = restTemplate.exchange(Uris.V1_STUDY_COUNT+Uris.STUDY_PAGE, HttpMethod.POST, request,
                NetworkResult.class);
        // 校验返回状态码
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
        // 校验返回结果
        final NetworkResult<DdbLearnLogBookTrace> result = entity.getBody();
        Assert.assertEquals(result.getErrorCode(), "200");
        final DdbLearnLogBookTrace bookTrace = JSON.parseObject(JSON.toJSONString(result.getData()), DdbLearnLogBookTrace.class);
        Assert.assertEquals(bookTrace.getLearnPage(), "P14, P15, P16, P17, P18, P19");
    }
}

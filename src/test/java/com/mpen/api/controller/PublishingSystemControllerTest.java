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

import com.mp.shared.common.FullBookInfo;
import com.mpen.TestBase;
import com.mpen.api.bean.Book;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbResourceBook;
import com.mpen.api.mapper.ResourceBookMapper;
import com.mpen.api.util.CommUtil;

/**
 * 
 * @author wangyue
 *  出版系统相关测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PublishingSystemControllerTest extends TestBase{

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ResourceBookMapper resourceBookMapper;
    private static final String BASE_URI = "/v1/publishing/book?key=";
    private static final String KEY = Constants.PUBLISH_KEY;
    
    /**
     * 创建图书测试
     */
    @Test
    public void testCreateBook() {
        // 设置认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        // 模拟请求ip
        headers.add("X-Forwarded-For", "114.247.222.226");
        final Book book = new Book();
        book.setAction("createBook");
        book.setBookId("ad7767577dc34ec3abc9c50c75ppqqg9");//最后根据ID删除
        book.setName("单元测试书籍");
        book.setIsbn("12345");
        book.setType(FullBookInfo.Type.STUDY);
        // 通过REST URL 发送请求
        HttpEntity<Book> request = new HttpEntity<Book>(book,headers);
        ResponseEntity<Object> entity = restTemplate.exchange(BASE_URI + KEY, HttpMethod.POST, request, Object.class);
        // 校验返回状态码是否为200
        Assert.assertEquals(entity.getStatusCode(), HttpStatus.OK);
        // 查询是否创建成功
        DdbResourceBook ddbResourceBook = resourceBookMapper.getId(book.getBookId());
        Assert.assertEquals(true, ddbResourceBook != null);
        // 删除
        resourceBookMapper.delete(book.getBookId());
        // 再次查询,检验是否删除成功
        ddbResourceBook = resourceBookMapper.getId(book.getBookId());
        Assert.assertEquals(true, ddbResourceBook == null);
        int count = resourceBookMapper.countByName(book.getName());
        // 校验相同名字是否创建成功
        Assert.assertEquals(true, count == 0);
        // 再次创建(成功)
        HttpEntity<Book> request1 = new HttpEntity<Book>(book,headers);
        ResponseEntity<Object> entity1 = restTemplate.exchange(BASE_URI + KEY, HttpMethod.POST, request1, Object.class);
        // 校验返回状态码是否为200
        Assert.assertEquals(entity1.getStatusCode(), HttpStatus.OK);
        // 再次查询,检验是否创建成功
        ddbResourceBook = resourceBookMapper.getId(book.getBookId());
        count = resourceBookMapper.countByName(book.getName());
        Assert.assertEquals(true, ddbResourceBook != null);
        // 校验创建的本数是否为1
        Assert.assertEquals(true, count == 1);
        // 再次创建相同名字(失败)
        HttpEntity<Book> request2 = new HttpEntity<Book>(book,headers);
        ResponseEntity<Object> entity2 = restTemplate.exchange(BASE_URI + KEY, HttpMethod.POST, request2, Object.class);
        // 校验返回状态码是否为200
        Assert.assertEquals(entity2.getStatusCode(), HttpStatus.OK);
        count = resourceBookMapper.countByName(book.getName());
        // 校验是否创建失败(本书名只有一条记录为创建失败)
        Assert.assertEquals(true, count == 1);
        // 再次创建不同名字(成功)
        book.setBookId(CommUtil.genRecordKey());
        book.setName("单元测试书籍1");
        HttpEntity<Book> request3 = new HttpEntity<Book>(book,headers);
        ResponseEntity<Object> entity3 = restTemplate.exchange(BASE_URI + KEY, HttpMethod.POST, request3, Object.class);
        // 校验返回状态码是否为200
        Assert.assertEquals(entity3.getStatusCode(), HttpStatus.OK);
        ddbResourceBook = resourceBookMapper.getId(book.getBookId());
        Assert.assertEquals(true, ddbResourceBook != null);
        count = resourceBookMapper.countByName(book.getName());
        // 校验创建的本数是否为1
        Assert.assertEquals(true, count == 1);
        // 删除测试的书籍
        resourceBookMapper.delete("ad7767577dc34ec3abc9c50c75ppqqg9");
        resourceBookMapper.delete(book.getBookId());
    }
    
}

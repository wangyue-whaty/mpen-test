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

import com.mpen.TestBase;
import com.mpen.api.bean.UserMessage;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbUserMessage;
import com.mpen.api.mapper.DdbUserMessageMapper;

/**
 * 
 * @ClassName DdbUserMessageControllerTest
 * @Description 消息推送测试
 * @author wangyue
 * @date 2018年8月21日 下午6:42:26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserMessageControllerTest extends TestBase {

    private static final String LIST_MESSAGE_URL = "/v1/userMessage/listUserMessage";
    private static final String userName = "13717521920";
    private static final String password = "3986290380150-1529655095608-0d38af59630098b33fdb536e82ea032f";

    @Autowired
    private DdbUserMessageMapper ddbUserMessageMapper;

    @Test
    public void testDdbUserMessageController() {
        // 存入10条消息
        final List<DdbUserMessage> save = getPageDdbUserMessage();
        for (DdbUserMessage ddbUserMessage : save) {
            this.ddbUserMessageMapper.saveDdbUserMessage(ddbUserMessage);
        }
        // 查询第二页数据，每页五个
        final UserMessage userMessage = new UserMessage();
        userMessage.setAction(Constants.LIST_USER_MESSAGE);
        userMessage.setPageNo(2);
        userMessage.setPageSize(5);
        this.postControllerTest(userName, password, userMessage, LIST_MESSAGE_URL);
        // 存入一条消息
        final DdbUserMessage ddbUserMessage = getDdbUserMessage().get(0);
        this.ddbUserMessageMapper.saveDdbUserMessage(ddbUserMessage);
        // 阅读一条消息
        userMessage.setAction(Constants.ONE_USER_MESSAGE);
        userMessage.setId(ddbUserMessage.getId());
        this.postControllerTest(userName, password, userMessage, LIST_MESSAGE_URL);
        // 删除一条消息
        userMessage.setAction(Constants.DELETE_USER_MESSAGE);
        userMessage.setId(ddbUserMessage.getId());
        this.postControllerTest(userName, password, userMessage, LIST_MESSAGE_URL);
    }
}

/**  
 * @author sxg  
 * @date 2018年8月22日
 */
package com.mpen.api.mapper;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.mpen.TestBase;
import com.mpen.api.domain.DdbUserMessage;

/**
 * @author sxg
 * @date 2018年8月22日
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DdbUserMessageMapperTest extends TestBase {

    @Autowired
    private DdbUserMessageMapper ddbUserMessageMapper;

    /**
     * 用户消息SQL测试
     */
    @Test
    public void testDdbUserMessage() {

        // 针对分页操作
        for (DdbUserMessage ddbUserMessage : getPageDdbUserMessage()) {
            // 存储用户消息
            this.ddbUserMessageMapper.saveDdbUserMessage(ddbUserMessage);
        }
        final String loginId = getPageDdbUserMessage().get(0).getLoginId();
        // 第二页数据
        final List<DdbUserMessage> pageMessageInfo = this.ddbUserMessageMapper.findMessageListByLoginId(loginId, 5, 5);

        Assert.assertEquals(pageMessageInfo.size(), 5);

        // 针对每个消息进行的操作
        for (DdbUserMessage ddbUserMessage : getDdbUserMessage()) {
            // 存储用户消息
            this.ddbUserMessageMapper.saveDdbUserMessage(ddbUserMessage);
            // 根据消息ID进行查询
            final DdbUserMessage byMessageId = this.ddbUserMessageMapper.findMessageByMessageId(ddbUserMessage.getId());
            Assert.assertEquals(byMessageId != null, true);
            // 修改消息的读取状态
            final int isRead = this.ddbUserMessageMapper.updateUserMessageIsRead(ddbUserMessage.getId(), 1);
            Assert.assertEquals(isRead, 1);
            // 修改消息的删除状态
            final int isDel = this.ddbUserMessageMapper.updateUserMessageIsDel(ddbUserMessage.getId(), 1);
            Assert.assertEquals(isDel, 1);
        }
    }
}

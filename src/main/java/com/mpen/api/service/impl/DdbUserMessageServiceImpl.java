package com.mpen.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mp.shared.common.Page;
import com.mpen.api.bean.UserMessage;
import com.mpen.api.bean.UserMessageInfo;
import com.mpen.api.common.Constants;
import com.mpen.api.domain.DdbPeCustom;
import com.mpen.api.domain.DdbUserMessage;
import com.mpen.api.mapper.DdbUserMessageMapper;
import com.mpen.api.mapper.PeCustomMapper;
import com.mpen.api.service.DdbUserMessageService;
import com.mpen.api.util.CommUtil;
import com.mpen.api.util.JPushUtil;

/**
 * 推送消息服务 涉及：App2.0 消息推送相关接口
 */
@Service
public class DdbUserMessageServiceImpl implements DdbUserMessageService {
    @Autowired
    private PeCustomMapper peCustomMapper;
    @Autowired
    private DdbUserMessageMapper ddbUserMessageMapper;

    @Override
    public boolean pushMessage(String loginId, String content, Map<String, String> msg) {
        try {
            // 全部平台推送
            final JPushUtil.JpushParam jpushParam = new JPushUtil.JpushParam(loginId, content, msg,
                    JPushUtil.AppType.VIATON, JPushUtil.PlatformType.ALL);
            JPushUtil.sendPushToUser(jpushParam);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 保存消息
     */
    @Override
    public boolean saveMessage(String loginId, String type, String content, String typeDetail) {
        final DdbUserMessage ddbUserMessage = new DdbUserMessage();
        ddbUserMessage.setId(CommUtil.genRecordKey());
        ddbUserMessage.setLoginId(loginId);
        ddbUserMessage.setType(type);
        ddbUserMessage.setContent(content);
        ddbUserMessage.setIsRead(0);
        ddbUserMessage.setIsDel(0);
        final Date curDate = new Date();
        ddbUserMessage.setCreateTime(curDate);
        ddbUserMessage.setUpdateTime(curDate);
        ddbUserMessage.setTypeDetail(typeDetail);
        ddbUserMessageMapper.saveDdbUserMessage(ddbUserMessage);
        return true;
    }

    /*
     * 将按分页信息，用户ID查询出的消息信息进行封装，消息列表没有经过创建时间排序
     */
    @Override
    public Page<UserMessageInfo> UserMessagesOfPage(UserMessage message) {
        // TODO 初始化分页数据
        // 初始化页面
        final Page<UserMessageInfo> page = new Page<UserMessageInfo>();
        // 判断loginID是否为空
        final String loginId = message.getLoginId();
        // 获取用户消息的总数量
        final int totalCount = ddbUserMessageMapper.getTotalCount(loginId);
        if (totalCount == 0) {
            return page;
        }

        // 查询分页后的用户信息列表
        final List<DdbUserMessage> listDbdUserMessages = ddbUserMessageMapper.findMessageListByLoginId(loginId,
                message.getPageIndex(), message.getPageSize());

        final List<UserMessageInfo> listUserMessages = new ArrayList<UserMessageInfo>();
        // 格式化返回消息
        for (DdbUserMessage ddbUserMessage : listDbdUserMessages) {
            final UserMessageInfo userMessageInfo = new UserMessageInfo();
            userMessageInfo.setId(ddbUserMessage.getId());
            userMessageInfo.setloginId(ddbUserMessage.getLoginId());
            userMessageInfo.setType(ddbUserMessage.getType());
            userMessageInfo.setContent(ddbUserMessage.getContent());
            userMessageInfo.setIsRead(ddbUserMessage.getIsRead());
            userMessageInfo.setIsDel(ddbUserMessage.getIsDel());
            // 时间戳转变
            userMessageInfo.setCreateTimeInMs(ddbUserMessage.getCreateTime().getTime());
            userMessageInfo.setTypeDetail(ddbUserMessage.getTypeDetail());
            listUserMessages.add(userMessageInfo);
        }
        // 向page中封装数据
        page.setItems(listUserMessages);
        page.setTotalCount(totalCount);
        page.setPageNo(message.getPageNo());
        return page;
    }

    /*
     * 根据消息ID查询消息,修改消息状态（0：未读，1：已读）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean OneOfUserMessages(UserMessage userMessage) {
        // 获取用户某个消息详情
        final String messageId = userMessage.getId();
        // 前台传入数据永不会为空，所以没有进行空值判断
        final DdbUserMessage DdbUserMessage = this.ddbUserMessageMapper.findMessageByMessageId(messageId);
        // 判断消息是否已读
        if (DdbUserMessage.getIsRead() == 0) {// 消息未读
            // 修改消息为已读状态
            final int line = this.ddbUserMessageMapper.updateUserMessageIsRead(messageId, Constants.MESSAGE_IS_READ);
            return line == 1 ? true : false;
        }
        return true;

    }

    /*
     * 根据消息ID修改消息为删除状态
     * 
     * @param userMessage
     * 
     * @return true:删除成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserMessage(UserMessage userMessage) {
        final String messageId = userMessage.getId();
        int line = 0;
        if (StringUtils.isNotBlank(messageId)) {
            line = this.ddbUserMessageMapper.updateUserMessageIsDel(messageId, Constants.MESSAGE_IS_DEL);
        }
        return line == 1 ? true : false;
    }

    @Override
    public boolean pushSystemMessage(String type, String content, Map<String, String> msg) {
        final List<DdbPeCustom> customs = peCustomMapper.get();
        customs.forEach((custom) -> {
            try {
                pushMessage(custom.getLoginId(), content, msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return true;
    }

}

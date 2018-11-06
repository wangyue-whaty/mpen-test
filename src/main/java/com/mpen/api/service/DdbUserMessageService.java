package com.mpen.api.service;

import java.util.Map;

import com.mp.shared.common.Page;
import com.mpen.api.bean.UserMessage;
import com.mpen.api.bean.UserMessageInfo;

public interface DdbUserMessageService {

    /**
     * @Title userMessagePush 
     * @Description 消息推送服务
     * @param loginId 用户id
     * @param content 推送内容 
     * @param msg 额外推送内容 
     */
    boolean pushMessage(String loginId,String content, Map<String, String> msg);
    
    /**
     * @Title userMessageSave 
     * @Description 消息保存
     * @param loginId 用户id
     * @param integral 推送标题
     * @param content 推送内容 
     * @param typeDetail 
     */
    boolean saveMessage(String loginId, String integral, String content, String typeDetail);
    
    /**
     * 
     * @Title pushSystemMessage 
     * @Description 推送系统通知(主动)
     * @param type 推送类型
     * @param content 推送内容
     * @param msg 额外推送内容
     */
    boolean pushSystemMessage(String type, String content, Map<String, String> msg);
    
    
    /**
     * sxg start
     */
    
    /**
     * 
     * <p>Title: UserMessagesOfPage</p>  
     * <p>Description: 查询用户每页的消息列表，没有根据消息创建时间进行排序</p>  
     * @param message
     * @param loginId
     * @return 
     */
    Page<UserMessageInfo> UserMessagesOfPage(UserMessage message);
    
    /**
     * 
     * <p>Title: OneOfUserMessages</p>  
     * <p>Description: 根据用户ID和消息ID获取用户点击的具体的消息</p>  
     * @param MessageId
     * @return 
     */
    public boolean OneOfUserMessages(UserMessage userMessage);
    
    /**
     * 
     * <p>Title: deleteUserMessage</p>  
     * <p>Description: 根据用户ID，消息ID删除用户的消息(逻辑删除，只修改了消息的状态为1)</p>  
     * @param userMessage
     * @return 数据库中受影响的行数
     */
    public boolean deleteUserMessage(UserMessage userMessage);
    // sxg end
    
}

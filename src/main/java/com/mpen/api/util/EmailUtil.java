package com.mpen.api.util;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

/**
 * 邮件工具类
 * 
 * @author wangyue
 *
 */
@Component
public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    @Autowired
    private JavaMailSender mailSender;
    private static String deliver = "crash-alerts@mpen.com.cn"; // 发件人
    private static String[] receiver = {"backendops@mpen.com.cn"};  // 收件人
    private boolean isHtml = true;
    
    /**
     * 
     * @param deliver 发送者
     * @param receiver 接收者
     * @param carbonCopy 抄送人
     * @param subject 主题
     * @param content 内容
     * @param isHtml 是否html格式
     */
    public void sendHtmlEmail(String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message);
            messageHelper.setFrom(deliver);
            messageHelper.setTo(receiver);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, isHtml);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

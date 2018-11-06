package com.mpen.api.util;

import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailUtilTest {
    
    @Autowired
    private EmailUtil emailUtil;
    @Test
    public void sendHtmlEmail() throws MessagingException {
        String subject = "This is a HTML content email";
        String content = "<h1>This is HTML content email </h1>";
        emailUtil.sendHtmlEmail(subject, content);
    }
}

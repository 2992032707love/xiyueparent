package com.rts.test;

import com.rts.AdminApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdminApp.class)
public class EmailTest {
    @Resource
    JavaMailSender javaMailSender;
    @Test
    public void handler() throws MessagingException {
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        // 谁发的
//        mailMessage.setFrom("2992032707@qq.com");
//        // 发给谁
//        mailMessage.setTo("2992032707@qq.com");
//        // 邮件的标题
//        mailMessage.setSubject("你好");
//        // 邮件的内容
//        mailMessage.setText("邮件的内容： 测试行不行2");
//        javaMailSender.send(mailMessage);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.addAttachment("test.txt", new File("d:/test.txt"));
        helper.setFrom("2992032707@qq.com");
        helper.setTo("2992032707@qq.com");
        helper.setSubject("你好");
        helper.setText("邮件的内容： 测试行不行3");
        javaMailSender.send(mimeMessage);
    }
}

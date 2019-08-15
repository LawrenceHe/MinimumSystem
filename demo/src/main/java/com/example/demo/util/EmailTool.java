package com.example.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class EmailTool {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendSimpleMail(String subject, String content){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("helun@zhaodongdb.com");
            helper.setTo(new String[]{"helun@zhaodongdb.com", "chenjianxuan@zhaodongdb.com"});
            helper.setSubject(subject);
            helper.setText(content);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}

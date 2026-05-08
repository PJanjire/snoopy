package com.snoopy.service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import freemarker.template.Configuration;
import freemarker.template.Template;

import jakarta.mail.internet.MimeMessage;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.snoopy.dto.MailResponse;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private Configuration config;

  
    public MailResponse sendTestMailWithTemplate(String email, Map<String, Object> model) {

        MailResponse response = new MailResponse();

        try {

            MimeMessage message = sender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            Template template = config.getTemplate("testMail.ftl");

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            ClassPathResource logo = new ClassPathResource("static/img/logo.png");

            helper.setTo(email);
            helper.setSubject("Snoopy - Test Email");
            helper.setText(html, true); 
            helper.setFrom("prashantjanjire78@gmail.com");

            helper.addInline("Logo", logo);

            sender.send(message);

            response.setStatus(true);
            response.setMessage("Mail sent successfully to: " + email);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(false);
            response.setMessage("Mail sending failed: " + e.getMessage());
        }

        return response;
    }
    

    public MailResponse sendForgotPasswordMail(String email, Map<String, Object> model) {

        MailResponse response = new MailResponse();

        try {

            MimeMessage message = sender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            Template template = config.getTemplate("forgetpassword.ftl");

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            ClassPathResource logo = new ClassPathResource("static/img/logo.png");

            helper.setTo(email);
            helper.setSubject("Snoopy - Test Email");
            helper.setText(html, true);
            helper.setFrom("prashantjanjire78@gmail.com");

            helper.addInline("Logo", logo);

            sender.send(message);

            response.setStatus(true);
            response.setMessage("Mail sent successfully to: " + email);

        } catch (Exception e) {
            e.printStackTrace();

            response.setStatus(false);
            response.setMessage("Mail sending failed: " + e.getMessage());
        }

        return response;
    }
}
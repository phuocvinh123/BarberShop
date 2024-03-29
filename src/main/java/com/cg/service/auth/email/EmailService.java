package com.cg.service.auth.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class EmailService {

    private final Logger log = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private JavaMailSender javaMailSender;

    //    public void sendSimpleMessage(
//            String to, String subject, String text) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("Demo");
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(text);
//        emailSender.send(message);
//    }
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug(
                "Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
                isMultipart,
                isHtml,
                to,
                subject,
                content
        );
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom("project@localhost");
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (MailException | MessagingException e) {
            log.debug("Email could not be sent to user '{}'", to, e);
        }
        // Prepare message using a Spring helper

    }
//    public void sendPasswordResetMail(User user) {
//        log.debug("Sending password reset email to '{}'", user.getEmail());
//        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
//    }
//    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
//        if (user.getEmail() == null) {
//            log.debug("Email doesn't exist for user '{}'", user.getLogin());
//            return;
//        }
//        Locale locale = Locale.forLanguageTag(user.getLangKey());
//        Context context = new Context(locale);
//        context.setVariable(USER, user);
//        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
//        String content = templateEngine.process(templateName, context);
//        String subject = messageSource.getMessage(titleKey, null, locale);
//        sendEmail(user.getEmail(), subject, content, false, true);
//    }
}


package fr.fadigoStore.service;

import fr.fadigoStore.entities.EmailTemplateName;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendMail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("contact@fadigostore.fr");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

//    @Async
//    public void sendMail(
//            String to,
//            String username,
//            EmailTemplateName emailTemplate,
//            String confirmationUrl,
//            String activationCode,
//            String subject
//    ) throws MessagingException {
//
//        String templateName;
//
//        if(emailTemplate == null) {
//            templateName = "Confirm-email";
//        } else {
//            templateName = emailTemplate.name();
//        }
//
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(
//                mimeMessage,
//                MimeMessageHelper.MULTIPART_MODE_MIXED,
//                StandardCharsets.UTF_8.name()
//        );
//
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("username", username);
//        properties.put("confirmation_url", confirmationUrl);
//        properties.put("activation_code", activationCode);
//
//        Context context = new Context();
//        context.setVariables(properties);
//
//        helper.setFrom("contact@fadigo.fr");
//        helper.setTo(to);
//        helper.setSubject(subject);
//        String message = templateEngine.process(templateName, context);
//        helper.setText(message, true);
//
//        mailSender.send(mimeMessage);
//
//    }

}

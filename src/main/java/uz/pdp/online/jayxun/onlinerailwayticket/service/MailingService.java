package uz.pdp.online.jayxun.onlinerailwayticket.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.eclipse.angus.mail.util.MailConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.custom.SendMailDto;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class MailingService {

    private final JavaMailSender mailSender;
    private final Logger logger;

    @Async
    public void sendMail(SendMailDto sendMailDto) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(sendMailDto.getTo());
//        message.setSubject(sendMailDto.getSubject());
//        message.setText(sendMailDto.getContent());

        try {
            mimeMessage.setSubject(sendMailDto.getSubject());
            mimeMessage.setContent(sendMailDto.getContent(), "text/html;charset=utf-8");
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(sendMailDto.getTo()));
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }



    }
}
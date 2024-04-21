package uz.pdp.online.jayxun.onlinerailwayticket.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import uz.pdp.online.jayxun.onlinerailwayticket.dto.custom.SendMailDto;

@Component
@RequiredArgsConstructor
public class MailingService {

    private final JavaMailSender mailSender;

    @Async
    public void sendMail(SendMailDto sendMailDto) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sendMailDto.getTo());
        message.setSubject(sendMailDto.getSubject());
        message.setText(sendMailDto.getContent());

        mailSender.send(message);


    }

}

package uz.pdp.online.jayxun.onlinerailwayticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableScheduling
public class OnlineRailwayTicketApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineRailwayTicketApplication.class, args);

    }



}

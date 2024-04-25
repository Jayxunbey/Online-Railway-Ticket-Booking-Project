package uz.pdp.online.jayxun.onlinerailwayticket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.logging.Logger;

@Configuration
@RequiredArgsConstructor
public class CustomConfig {


    private final ApplicationContext applicationContext;

    @Bean
    public Random getRandom() {
        return new Random();
    }

    @Bean
    public Logger getLogger() {
        return Logger.getAnonymousLogger();
    }


}


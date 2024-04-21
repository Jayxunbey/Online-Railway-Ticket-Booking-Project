package uz.pdp.online.jayxun.onlinerailwayticket.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

import java.util.Random;

@Configuration
@RequiredArgsConstructor
public class CustomConfig {

    @Qualifier("applicationTaskExecutor")
    private final TaskExecutor taskExecutor;

    private final ApplicationContext applicationContext;

    @Bean
    public Random getRandom() {
        return new Random();
    }

}


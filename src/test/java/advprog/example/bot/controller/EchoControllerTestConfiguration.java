package advprog.example.bot.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EchoControllerTestConfiguration {
    @Bean
    EchoController echoController() {
        return new EchoController();
    }
}

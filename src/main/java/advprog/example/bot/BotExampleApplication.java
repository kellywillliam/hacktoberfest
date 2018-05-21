package advprog.example.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import advprog.example.bot.controller.HangoutController;

@SpringBootApplication
public class BotExampleApplication {

    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());
    public static void main(String[] args) {
        System.setProperty("line.bot.channelSecret", System.getenv("LINE_BOT_CHANNELSECRET"));
        System.setProperty("line.bot.channelToken", System.getenv("LINE_BOT_CHANNELTOKEN"));
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotExampleApplication.class, args);
    }
}

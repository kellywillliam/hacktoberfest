package advprog.example.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotExampleApplication {

    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());

    public static void main(String[] args) {
        System.setProperty("line.bot.channelSecret", System.getenv("LINE-CHANNEL-SECRET"));
        System.setProperty("line.bot.channelToken", System.getenv("LINE-CHANNEL-TOKEN"));
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotExampleApplication.class, args);
    }
}

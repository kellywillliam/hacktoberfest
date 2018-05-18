package advprog.quran.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotQuranApplication {

    private static final Logger LOGGER = Logger.getLogger(BotQuranApplication.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotQuranApplication.class, args);
    }
}

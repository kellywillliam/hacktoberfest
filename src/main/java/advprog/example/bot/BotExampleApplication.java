package advprog.example.bot;

import java.nio.file.Path;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotExampleApplication {

    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());
    public static Path downloadedContentDir;

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotExampleApplication.class, args);
    }
}

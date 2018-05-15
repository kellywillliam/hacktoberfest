package advprog.example.bot;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotExampleApplication {

    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());

    public static void main(String[] args) throws IOException {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotExampleApplication.class, args);

    }
}

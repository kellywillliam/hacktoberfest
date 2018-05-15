package advprog.example.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootApplication
public class BotExampleApplication {
	public static Path downloadedContentDir;
    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());

    public static void main(String[] args) throws IOException {
        LOGGER.info("Application starting ...");
        downloadedContentDir = Files.createTempDirectory("line-bot");
        SpringApplication.run(BotExampleApplication.class, args);
    }
}

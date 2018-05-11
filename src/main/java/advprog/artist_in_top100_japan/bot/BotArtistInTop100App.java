package advprog.artist_in_top100_japan.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotArtistInTop100App {

    private static final Logger LOGGER = Logger.getLogger(BotArtistInTop100App.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotArtistInTop100App.class, args);
    }
}

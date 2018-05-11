package advprog.artist.in.weekly.tropical.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArtistInTop100App {

    private static final Logger LOGGER = Logger.getLogger(advprog.artist_in_top100_japan.bot.ArtistInTop100App.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(advprog.artist_in_top100_japan.bot.ArtistInTop100App.class, args);
    }
}

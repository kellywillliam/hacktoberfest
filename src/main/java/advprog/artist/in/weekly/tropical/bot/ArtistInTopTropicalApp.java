package advprog.artist.in.weekly.tropical.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArtistInTopTropicalApp {

    private static final Logger LOGGER = Logger.getLogger(advprog.artist.in.weekly.tropical.bot.ArtistInTopTropicalApp.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(advprog.artist.in.weekly.tropical.bot.ArtistInTopTropicalApp.class, args);
    }
}

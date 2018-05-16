package advprog.artist.in.weekly.tropical.bot;

import advprog.artist.in.weekly.tropical.bot.ArtistInTopTropicalApp;
import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArtistInTopTropicalApp {

    private static final Logger LOGGER =
            Logger.getLogger(ArtistInTopTropicalApp.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(ArtistInTopTropicalApp.class, args);
    }
}

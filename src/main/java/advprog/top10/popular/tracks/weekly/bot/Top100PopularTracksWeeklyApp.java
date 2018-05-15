package advprog.top10.popular.tracks.weekly.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Top100PopularTracksWeeklyApp {
    private static final Logger LOGGER
            = Logger.getLogger(Top100PopularTracksWeeklyApp.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Application starting ...");
        SpringApplication.run(Top100PopularTracksWeeklyApp.class, args);
    }


}

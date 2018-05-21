package advprog.example.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import advprog.example.bot.controller.HangoutController;

@SpringBootApplication
public class BotExampleApplication {

    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());
    public static void main(String[] args) {
        HangoutController h = new HangoutController();
        System.out.println( h.getListCarousel()[0] ) ;
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotExampleApplication.class, args);
    }
}

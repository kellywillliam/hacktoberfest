package advprog.example.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotExampleApplication {

    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());

    public static void main(String[] args) {
//        System.setProperty("line.bot.channelSecret", System.getenv("f3fe99681eb03bb34f8eb97047f9417b"));
//        System.setProperty("line.bot.channelToken", System.getenv("g+DEjOWWSYqfh2dKoMEbqpw+iwDp4Gf0LVwHAcvU/szUWEphCAcj/BFssroQMuhPBE4asYUDdXeL4OduECEpbktvjz05PwG93TeFyJXO5gYHDKfxN438+7HtJlC8qch/uwEjrO+q5TP/xkBHkwi+qwdB04t89/1O/w1cDnyilFU="));

        LOGGER.info("Application starting ...");
        SpringApplication.run(BotExampleApplication.class, args);
    }
}

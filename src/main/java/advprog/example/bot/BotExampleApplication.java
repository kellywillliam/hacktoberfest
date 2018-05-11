package advprog.example.bot;

import java.util.logging.Logger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotExampleApplication {

    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());

    public static void main(String[] args) {
        System.setProperty("line.bot.channelSecret", "e536048652b5bf93d0b66f1823707152");
        System.setProperty("line.bot.channelToken", "sM5HIm/Pn5FS1br38o32QGaFOF14NNdJXQIW"
                + "EuvHLzGTIGc7FP7AJ3SNUXZ8xXQ9jj5Zv/VOwoztvblYJuv31iA5b61"
                + "V2R0wpn7/w3SndoOj04h/2APkIlt5YBLbRF6fKMgetQPHA8t"
                + "sWAu96b0tMAdB04t89/1O/w1cDnyilFU=");
        LOGGER.info("Application starting ...");
        SpringApplication.run(BotExampleApplication.class, args);
    }
}

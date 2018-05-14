package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.logging.Logger;

@LineMessageHandler
@SpringBootApplication
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());
    OriconRank eventHandler = new OriconRank();

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText().toLowerCase();
        String[] command = contentText.split(" ");
        String replyText;

        if (command[0].equals("/oricon") && command[1].equals("bluray")) {
            try {
                String date = command[3];

                if (command[2].equals("weekly")) {
                    replyText = eventHandler.weekly(date);
                }

                else if (command[2].equals("daily")) {
                    replyText = eventHandler.daily(date);
                }

                else {
                    replyText = "Pesan yang kamu kirimkan belum sesuai format." +
                            "Pastikan kamu menuliskan 'weekly' atau 'daily' dengan benar.";
                }

            }

            catch (ArrayIndexOutOfBoundsException e) {
                replyText = "Pesan yang kamu kirimkan belum sesuai format." +
                        "Pastikan format yang kamu kirimkan sudah lengkap.";
            }
        }

        else {
            replyText = "Halo, terima kasih atas pesan yang dikirimkan. \n" +
                    "Untuk menggunakan bot ini, silakkan kirimkan pesan dengan format" +
                    "'/oricon bluray [weekly/daily] [YYYY-MM-DD]' \n" +
                    "Contoh: /oricon bluray weekly 2018-05-14";
        }

        return new TextMessage(replyText);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import java.util.logging.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@LineMessageHandler
@SpringBootApplication
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());
    OriconRank eventHandler = new OriconRank();

    @EventMapping
    public TextMessage handleTextMessageEventNew(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String replyText;

        String[] tmp = contentText.split(" ");

        if (tmp[0].equalsIgnoreCase("/echo")) {
            replyText = contentText.replace("/echo ", "");
        } else {
            replyText = eventHandler.execute(contentText);
        }

        return new TextMessage(replyText);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

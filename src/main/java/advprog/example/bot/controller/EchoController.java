package advprog.example.bot.controller;

import static org.apache.logging.log4j.message.MapMessage.MapFormat.JSON;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    MediaWikiController eventHandler = new MediaWikiController();

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
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

/*    @EventMapping
    public ImageMessage handleImageMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("ImageMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        ImageMessage replyImage;

        String[] tmp = contentText.split(" ");

        if (tmp[0].equalsIgnoreCase("/echo")) {
        } else {
        }

        return replyImage;
    }*/

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

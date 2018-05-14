package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class ImageTagController {

    private static final Logger LOGGER = Logger.getLogger(ImageTagController.class.getName());

    @EventMapping
    public TextMessage handleImageMessageEvent(MessageEvent<ImageMessageContent> event) {
        LOGGER.fine(String.format("ImageMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        ImageMessageContent content = event.getMessage();
        String contentId = content.getId();

//        String replyText = contentText.replace("/tags", "");
        return new TextMessage(contentId);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

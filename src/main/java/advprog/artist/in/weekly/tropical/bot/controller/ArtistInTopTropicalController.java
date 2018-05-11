package advprog.artist.in.weekly.tropical.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class ArtistInTopTropicalController {

    private static final Logger LOGGER = Logger.getLogger(advprog..in.weekly.tropical.bot.controller.ArtistInTop100Controller.class.

    ArtistInTopTropicalController());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        if (contentText.equalsIgnoreCase("/billboard tropical Drake")) {
            return new TextMessage("We're sorry to tell you that Drake isn't on the list");
        } else if (contentText.equalsIgnoreCase("/billboard tropical Coldplay")) {
            return new TextMessage("Coldplay\nYellow\n1");
        } else {
            return new TextMessage("error");
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

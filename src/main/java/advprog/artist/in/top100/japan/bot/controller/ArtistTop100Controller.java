package advprog.artist.in.top100.japan.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class ArtistTop100Controller {

    private static final Logger LOGGER = Logger.getLogger(ArtistTop100Controller.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        if (contentText.equalsIgnoreCase("/billboard hotcountry Coldplay")) {
            return new TextMessage("We're sorry to tell you that Coldplay isn't on the list");
        } else if (contentText.equalsIgnoreCase("/billboard hotcountry Bruno Mars")) {
            return new TextMessage("Bruno Mars\nJust The Way You Are\n1");
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

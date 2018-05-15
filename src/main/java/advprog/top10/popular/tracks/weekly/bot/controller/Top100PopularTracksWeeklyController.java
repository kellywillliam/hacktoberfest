package advprog.top10.popular.tracks.weekly.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class Top100PopularTracksWeeklyController {
    private static final Logger LOGGER
            = Logger.getLogger(Top100PopularTracksWeeklyController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        if (contentText.equalsIgnoreCase("/billboard tropical")) {
            return new TextMessage("(1) Darude - Sandstorm\r\n"
                    + "(2) Simon & Garfunkel - Scarborough Fair\r\n"
                    + "(3) Lazy Town - We Are Number One\r\n" + "...\r\n"
                    + "(10) Christopher Tin - Sogno di Volare\r\n"
                    + "");
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

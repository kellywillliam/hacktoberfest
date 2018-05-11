package azure.sentiment.analysis.controller;

import advprog.example.bot.controller.EchoController;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class SentimentAnalysisController {
    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        return null;
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

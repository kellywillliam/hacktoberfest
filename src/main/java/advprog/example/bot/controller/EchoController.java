package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String[] parsedContentText = contentText.split(" ");
        String replyText = "An error has occured";
        switch (parsedContentText[0]) {
            case "/echo":
                replyText = contentText.replace("/echo", "")
                        .substring(1);
                break;
            case "/enterkomputer":
                ApiEnterKomputer apiEnterKomputer = new ApiEnterKomputer();
                String name = contentText.substring(contentText
                        .lastIndexOf(parsedContentText[2]));
                replyText = apiEnterKomputer.getDetailItemByCategoryAndName(parsedContentText[1],
                        name);
                break;
            default:
                break;
        }
        return new TextMessage(replyText);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class AnimeInfoController {

    private static final Logger LOGGER = Logger.getLogger(AnimeInfoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        boolean group = event.getSource().toString().contains("group");
        String contentText = content.getText();
        String replyText = "";
        String[] chatText = contentText.split(" ");
        try {
            switch (chatText[0].toLowerCase()) {
                case "/is_airing":
                    replyText = chatText[1] + " is airing";
                    break;
                case "nonton":
                    replyText = "Steins Gate 25";
                    break;
                case "/echo":
                    replyText = contentText.replace("/echo", "")
                            .substring(1);
                    break;
                default:
                    break;
            }
            return new TextMessage(replyText);
        } catch (Exception ex) {
            return new TextMessage("api mati");
        }



    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }


}

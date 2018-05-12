package advprog.example.bot.controller;

//import org.json.*;
import advprog.example.bot.confidence.percentage.ConfidencePercentage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class SfwCheckerController {

    private static final Logger LOGGER = Logger.getLogger(SfwCheckerController.class.getName());
    public static void main(String[] args) throws java.io.IOException{
        String test = ConfidencePercentage.getConfidencePercentage("http://www3.canisius.edu/~grandem/butterflylifecycle/Puzzle%20Picture.jpg");
        System.out.println(test);
    }
    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws java.io.IOException{
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String replyText = "";
        String[] chatText = contentText.split(" ");
        switch (chatText[0].toLowerCase()){
            case "/echo":
                replyText = contentText.replace("/echo", "").substring(1);
                break;
            case "/is_sfw":
                replyText = ConfidencePercentage.getConfidencePercentage(chatText[1]);
                break;
            default:
                replyText = "salah command! command yang benar : /is_sfw [link]";
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

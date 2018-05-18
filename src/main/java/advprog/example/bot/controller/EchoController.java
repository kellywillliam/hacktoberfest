package advprog.example.bot.controller;

import advprog.example.bot.composer.RandomTriviaComposer;
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

        RandomTriviaComposer triviaComposer = new RandomTriviaComposer();

        if ((contentText.substring(0, 5)).equals("/echo")) {
            String replyText = contentText.replace("/echo", "");
            return new TextMessage(replyText.substring(1));
        }

//        else if (contentText.equals("/add_question")) {
//
//            return triviaComposer.addQuestion("hjgj", "gjhghj");
//            ;
//        }
//
//        else if (contentText.equals("/change_answer")) {
//            String replyText = "";
//            return new TextMessage(replyText);
//        }

        else {
            String replyText = "";
            return new TextMessage(replyText);
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

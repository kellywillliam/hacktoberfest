package advprog.example.bot.controller;

import advprog.example.bot.composer.RandomTriviaComposer;
import advprog.example.bot.handler.QuestionHandler;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    @EventMapping
    public Message handleMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        Source source = event.getSource();
        String senderId = source.getSenderId();

        RandomTriviaComposer triviaComposer = new RandomTriviaComposer();
        QuestionHandler questionHandler = new QuestionHandler();
        Message reply = null;

        if ((contentText.substring(0, 5)).equals("/echo")) {
            String replyText = contentText.replace("/echo", "");
            return new TextMessage(replyText);
        }

        else if (contentText.equals("/add_question")) {

            if (source instanceof UserSource) {
                questionHandler.handleCommandAddQuestion(event);
                questionHandler.handleAddQuestion(event, senderId);
                for (int i = 0; i < 4; i++) {
                    questionHandler.handleAddAnswers(event);
                }
                // Add question to DB
            }
            else {
                reply = new TextMessage("Private chat only");
            }
            return reply;
        }

//        else if (contentText.equals("/change_answer")) {
//            if (source instanceof UserSource) {
//                // proses
//            }
//            else {
//                reply = new TextMessage("Private chat only");
//            }
//            return reply;
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

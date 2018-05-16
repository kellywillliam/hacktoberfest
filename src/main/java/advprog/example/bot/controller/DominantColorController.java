//package advprog.example.bot.controller;
//
//import com.linecorp.bot.model.event.Event;
//import com.linecorp.bot.model.event.MessageEvent;
//import com.linecorp.bot.model.event.message.TextMessageContent;
//import com.linecorp.bot.model.message.TextMessage;
//import com.linecorp.bot.spring.boot.annotation.EventMapping;
//import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
//
//import java.util.logging.Logger;
//
//@LineMessageHandler
//public class DominantColorController {
//
//    private static final Logger LOGGER = Logger.getLogger(DominantColorController.class.getName());
//
//    @EventMapping
//    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
//        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
//                event.getTimestamp(), event.getMessage()));
//        TextMessageContent content = event.getMessage();
//        String contentText = content.getText();
//
//        String replyText = contentText.replace("/echo", "");
//        return new TextMessage(replyText.substring(2));
//    }
//
//    @EventMapping
//    public void handleDefaultMessage(Event event) {
//        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
//                event.getTimestamp(), event.getSource()));
//    }
//}
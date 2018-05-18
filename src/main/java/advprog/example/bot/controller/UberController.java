package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class UberController {

    private static final Logger LOGGER = Logger.getLogger(UberController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        String replyText = contentText.replace("/echo", "");
        return new TextMessage(replyText.substring(1));
    }

    private void handleTextContent() {
    	//TODO implement text content handler
    }
    
    private void createMessage() {
    	//TODO implement message constructor
    }
    
    private void estimatRide() {
    	//TODO implement function when user asks for estimation from /uber
    }
    
    private void addDestination() {
    	//TODO implement function when user inputs /add_destination
    }
    
    private void removeDestination() {
    	//TODO implement function when user inputs /delete_destination
    }
    
    private void reply() {
    	//TODO implement function to reply to user
    }
    
    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

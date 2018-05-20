package advprog.example.bot.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@LineMessageHandler
public class UberController {
	@Autowired
    private LineMessagingClient lineMessagingClient;

    private static final Logger LOGGER = Logger.getLogger(UberController.class.getName());
    private static double start_latitude;
    private static double start_longitude;
    private static String end_latitude;
    private static String end_longitude;
    
    
    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
       
        handleTextContent(content, event.getReplyToken());
    }
    
    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LocationMessageContent locationMessage = event.getMessage();
        
    }
    
    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    private void handleTextContent(TextMessageContent content, String replyToken) {
    	//TODO implement text content handler
    	String cmd = content.getText();
    	if (cmd.equalsIgnoreCase("/uber")) {
    		
    	} else if (cmd.equalsIgnoreCase("/add_destination")) {
    		String imageUrl = createUri("static/buttons/1040.jpg");
    		ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
    				imageUrl,
    				"test",
    				"test",
    				Arrays.asList(
    						new URIAction("Share Location",
    								"line://nv/location")
    						)
    				);
    		TemplateMessage templateMessage = new TemplateMessage(
    				"Share Location", buttonsTemplate
    				);
    		reply(replyToken, templateMessage);
    	} else if (cmd.equalsIgnoreCase("/remove_destination")) {
    		
    	} else {
    		
    	}
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
    
    private void chooseDestination(String replyToken) {
    	String imageUrl = createUri("/static/buttons/1040.jpg");
    	
    	CarouselTemplate carouselTemplate = new CarouselTemplate(
    			Arrays.asList(
    					new CarouselColumn(imageUrl, "test", "test",
    							Arrays.asList(
    									)
    							)
    					)
    			);
    	TemplateMessage templateMessage = new TemplateMessage(
    			"Choose Destination", carouselTemplate
    			);
    	this.reply(replyToken, templateMessage);
    }
    
    
    
    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .path(path).build()
                                          .toUriString();
    }
    
    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .get();
            System.out.println("Sent messages: " + apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(@NonNull String replyToken, @NonNull String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "……";
        }
        this.reply(replyToken, new TextMessage(message));
    }
}

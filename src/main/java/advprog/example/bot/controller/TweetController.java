package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class TweetController {

    private static final Logger LOGGER = Logger.getLogger(TweetController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {

        TwitterAPI api = new TwitterAPI();
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String[] contentList = contentText.split(" ");
        String reply;

        if(contentList.length == 3 && contentList[0].equalsIgnoreCase("/tweet") &&
                contentList[1].equalsIgnoreCase("recent")){
            String user = contentList[2];
            reply = api.getUserTimeLine(user);
        } else {
            reply = "Oops, Sorry. Please use this command format: /n /recent tweet [username]";
        }

        return new TextMessage(reply);

    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

}

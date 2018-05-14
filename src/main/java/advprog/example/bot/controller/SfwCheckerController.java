package advprog.example.bot.controller;


import advprog.example.bot.confidence.percentage.ConfidencePercentage;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@LineMessageHandler
public class SfwCheckerController {

    private static final Logger LOGGER = Logger.getLogger(SfwCheckerController.class.getName());
    private static String img = "";


    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String replyText = "";
        String[] chatText = contentText.split(" ");
        try{
            switch (chatText[0].toLowerCase()) {
                case "/is_sfw":
                    replyText = ConfidencePercentage.getConfidencePercentage(chatText[1]);
                    break;
                case "/echo":
                    replyText = contentText.replace("/echo", "")
                            .substring(1);
                    break;
                default:
                    break;
            }
            return new TextMessage(replyText);
        }catch(IOException ex){
            return new TextMessage("api mati");
        }
    }

    @EventMapping
    public TextMessage handleImageMessage (MessageEvent <ImageMessageContent> event) throws IOException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        String id = event.getMessage().getId();

        final LineMessagingClient client = LineMessagingClient
                .builder("+uFmWifpVZJBF1ZuxCaIeiFA7v4FF6D4djy+NitngehBdGNjpKc7ICYgF" +
                        "ZHLFP7L/yuaH+YAIxi22WOgCGGVkwHhjWuJyE+l38fBNOhb+A2G6gNJgwFBHQ2f+B5ud6ofr7" +
                        "V7oH3ZNKD9scEl+FMTkwdB04t89/1O/w1cDnyilFU=")
                .build();

        final MessageContentResponse messageContentResponse;
        try {
            messageContentResponse = client.getMessageContent(id).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }



        Path filePath = Files.createTempFile("foo", "bar");

        Files.copy(messageContentResponse.getStream(),
                filePath, StandardCopyOption.REPLACE_EXISTING);

        return new TextMessage(filePath.toUri().toURL().toString());


    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }



}

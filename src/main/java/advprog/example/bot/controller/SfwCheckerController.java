package advprog.example.bot.controller;


import advprog.example.bot.BotExampleApplication;
import advprog.example.bot.confidence.percentage.ConfidencePercentage;
import advprog.example.bot.confidence.percentage.LineImage;
//import advprog.example.bot.line.ImgurApi;

import com.google.common.io.ByteStreams;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;




@LineMessageHandler
public class SfwCheckerController {

    private static final Logger LOGGER = Logger.getLogger(SfwCheckerController.class.getName());
    public static String idUpload = "";
    public static void main(String[] args) throws Exception{

        String url = "https://orig00.deviantart.net/e134/f/2012/259/2/5/one_piece_nami_smile____by_hiyori456-d5ew8m1.jpg";
        RestTemplate restTemplate = new RestTemplate();
        byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
        Files.write(Paths.get("src/main/resources/image.jpg"), imageBytes);
        idUpload = LineImage.uploadImage("src/main/resources/image.jpg");
        System.out.println(idUpload);
    }
    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String replyText = "";
        String[] chatText = contentText.split(" ");
        //try {
            switch (chatText[0].toLowerCase()) {
                case "/is_sfw":
                    replyText = ConfidencePercentage.getConfidencePercentage(chatText[1]);
                    //replyText = ConfidencePercentage.getFromUserImage(idUpload);
                    break;
                case "/echo":
                    replyText = contentText.replace("/echo", "")
                            .substring(1);
                    break;
                default:
                    break;
            }
            return new TextMessage(replyText);
        //} catch (IOException ex) {
         //   return new TextMessage("api mati");
        //}
    }

    @EventMapping
    public TextMessage handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws Exception {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        ImageMessageContent content = event.getMessage();
        String id = content.getId();
        String idUpload = LineImage.getImage(id);
        String theStr = ConfidencePercentage.getFromUserImage(idUpload);
        return new TextMessage(id);

    }


    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }



}

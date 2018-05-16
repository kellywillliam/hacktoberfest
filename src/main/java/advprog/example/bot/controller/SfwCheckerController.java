package advprog.example.bot.controller;


import advprog.example.bot.confidence.percentage.ConfidencePercentage;
//import advprog.example.bot.line.ImgurApi;

import advprog.example.bot.confidence.percentage.ImgurSrc;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import java.nio.file.Paths;
import java.util.Base64;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;



@LineMessageHandler
public class SfwCheckerController {
    private static final Logger LOGGER = Logger.getLogger(SfwCheckerController.class.getName());
    private static String imageUrl = "";

    public static void main(String[] args) throws Exception{
        final String channelToken = "+uFmWifpVZJBF1ZuxCaIeiFA7v4FF6D4djy+NitngehBdGNjpK"
                + "c7ICYgFZHLFP7L/yuaH+YAIxi22WOgCGGVkwHhjWuJyE+l38fBNOhb+A2G6gNJgwFBHQ2f+B5ud6ofr7V" +
                "7oH3ZNKD9scEl+FMTkwdB04t89/1O/w1cDnyilFU=";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer " + channelToken);
        String url = "https://api.line.me/v2/bot/message/7965831312251/content";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        byte[] imageBytes = restTemplate.exchange(url, HttpMethod.GET,entity,byte[].class).getBody();
        Files.write(Paths.get("src/main/resources/image.jpg"), imageBytes);
        File image = new File("src/main/resources/image.jpg");
        JSONObject temp = null;
        try {
            temp = new JSONObject(ImgurSrc.upload(image));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject dataExtract = (JSONObject) temp.get("data");
        String theurl = (String) dataExtract.get("link");
        //String idUpload = uploadImage("src/main/resources/image.jpg");;
        //String theStr = ConfidencePercentage.getFromUserImage(idUpload);
        System.out.println(theurl);


    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event){
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String replyText = "";
        String[] chatText = contentText.split(" ");
        try {
            switch (chatText[0].toLowerCase()) {
                case "/is_sfw":
                    if (chatText.length > 1 && chatText.length < 3) {
                        replyText = ConfidencePercentage.getConfidencePercentage(chatText[1]);
                    } else {
                        replyText = ConfidencePercentage.getConfidencePercentage(imageUrl);
                        imageUrl = "";
                    }

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
    public TextMessage handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws Exception {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        ImageMessageContent content = event.getMessage();
        String id = content.getId();
        imageUrl = getImage(id);


        return new TextMessage(imageUrl);

    }


    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public static String getImage(String id) throws Exception {
        final String channelToken = "+uFmWifpVZJBF1ZuxCaIeiFA7v4FF6D4djy+NitngehBdGNjpK"
                + "c7ICYgFZHLFP7L/yuaH+YAIxi22WOgCGGVkwHhjWuJyE+l38fBNOhb+A2G6gNJgwFBHQ2f+B5ud6ofr7V" +
                "7oH3ZNKD9scEl+FMTkwdB04t89/1O/w1cDnyilFU=";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer " + channelToken);
        String url = "https://api.line.me/v2/bot/message/7965121869188/content";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        byte[] imageBytes = restTemplate.exchange(url, HttpMethod.GET,entity,byte[].class).getBody();
        Files.write(Paths.get("src/main/resources/image.jpg"), imageBytes);
        File image = new File("src/main/resources/image.jpg");
        JSONObject temp = null;
        try {
            temp = new JSONObject(ImgurSrc.upload(image));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject dataExtract = (JSONObject) temp.get("data");
        String theurl = (String) dataExtract.get("link");

        return theurl;
    }





}

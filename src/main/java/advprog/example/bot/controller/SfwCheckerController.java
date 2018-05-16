package advprog.example.bot.controller;


import advprog.example.bot.confidence.percentage.ConfidencePercentage;
//import advprog.example.bot.line.ImgurApi;

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

import org.springframework.web.client.RestTemplate;


@LineMessageHandler
public class SfwCheckerController {

    private static final Logger LOGGER = Logger.getLogger(SfwCheckerController.class.getName());

    public static void main(String[] args) throws Exception{
        String url = "https://api.line.me/v2/bot/message/7963713943014/content";
        RestTemplate restTemplate = new RestTemplate();
        byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
        Files.write(Paths.get("src/main/resources/image.jpg"), imageBytes);

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
        //String idUpload = LineImage.getImage(id);
        //String theStr = ConfidencePercentage.getFromUserImage(idUpload);

        return new TextMessage(id);

    }


    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public static String getImage(String id) throws Exception {
        String url = "https://api.line.me/v2/bot/message/"+ id + "/content";
        RestTemplate restTemplate = new RestTemplate();
        byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
        Files.write(Paths.get("src/main/resources/image.jpg"), imageBytes);
        String idImage = uploadImage("src/main/resources/image.jpg");
        return idImage;
    }

    public static String uploadImage(String path) throws Exception{
        /* Api calling */

        String credentialsToEncode = "acc_7131bd91f718dd6" + ":" + "0438f48b7ba34d253d4df8f7e52485af";
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        URL urlObject = new URL("https://api.imagga.com/v1/content");
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        String boundaryString = "-Image Upload-";
        String filepath = path;
        File fileToUpload = new File(filepath);

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);

        OutputStream outputStreamToRequestBody = connection.getOutputStream();
        BufferedWriter httpRequestBodyWriter =
                new BufferedWriter(new OutputStreamWriter(outputStreamToRequestBody));

        httpRequestBodyWriter.write("\n--" + boundaryString + "\n");
        httpRequestBodyWriter.write("Content-Disposition: form-data;"
                + "name=\"myFile\";"
                + "filename=\""+ fileToUpload.getName() +"\""
                + "\nContent-Type: text/plain\n\n");
        httpRequestBodyWriter.flush();

        FileInputStream inputStreamToLogFile = new FileInputStream(fileToUpload);

        int bytesRead;
        byte[] dataBuffer = new byte[1024];
        while((bytesRead = inputStreamToLogFile.read(dataBuffer)) != -1) {
            outputStreamToRequestBody.write(dataBuffer, 0, bytesRead);
        }

        outputStreamToRequestBody.flush();

        httpRequestBodyWriter.write("\n--" + boundaryString + "--\n");
        httpRequestBodyWriter.flush();

        outputStreamToRequestBody.close();
        httpRequestBodyWriter.close();

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

        connectionInput.close();

        System.out.println(jsonResponse);
        JSONObject bigObj = new JSONObject(jsonResponse);
        System.out.println(jsonResponse);
        JSONArray parentArr = bigObj.getJSONArray("uploaded");
        JSONObject smallObj = parentArr.getJSONObject(0);
        return smallObj.getString("id");
    }



}

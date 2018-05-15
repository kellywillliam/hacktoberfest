package advprog.example.bot.controller;

import advprog.example.bot.BotExampleApplication;
import com.google.common.io.ByteStreams;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.Uploader;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.logging.Logger;

import java.io.*;
import java.net.*;

import lombok.Value;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.imageio.ImageIO;

@Slf4j
@LineMessageHandler
public class EchoController {

    private static final String channelToken = "KSmqRwScpX3fa90zQNt2MDrmtpXwNTGtVyq2K0Jyi2/gfuMSO4cSEaPBkozhqSBrJDIcDJg/XrcVzmyrTTm7omrS1EcBpXLb/vpKckm1HUQIQAItWEZL3og1u7C2G+tK2c4TlJ+Xw0Dd/EpkFh1hsQdB04t89/1O/w1cDnyilFU=";
    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        String replyToken = event.getReplyToken();

        if(contentText.split(" ")[0].equalsIgnoreCase("url")){
            String imageSource = contentText.split(" ")[1];
            String imageID = event.getMessage().getId();
            convertImage(replyToken, imageSource);
        }

        String replyText = contentText.replace("/echo", "");
        return new TextMessage(replyText.substring(1));
    }


    final LineMessagingClient lineMessagingClient = LineMessagingClient
            .builder(channelToken)
            .build();

    @EventMapping
    public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) {
        LOGGER.fine(String.format("ImageMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        LOGGER.warning("masuk");

        final LineMessagingClient client = LineMessagingClient
                .builder(channelToken)
                .build();

        //final MessageContentResponse messageContentResponse;
        String replyToken = event.getReplyToken();
        //String imageID = event.getMessage().getId();

        // You need to install ImageMagick
        handleHeavyContent(
                event.getReplyToken(),
                event.getMessage().getId(),
                responseBody -> {
                    DownloadedContent jpg = saveContent("jpg", responseBody);
                    DownloadedContent previewImg = createTempFile("jpg");
                    system(
                            "convert",
                            "-resize", "240x",
                            jpg.path.toString(),
                            previewImg.path.toString());
                    File image = new File(jpg.path.toString());

                    JSONObject temp = null;
                    try {
                        temp = new JSONObject(Uploader.upload(image));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    JSONObject data = (JSONObject) temp.get("data");
                    String url = (String) data.get("link");
                    String result = null;
                    try {
                        result = obtainResult(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    final TextMessage textMessage = new TextMessage(result);
                    final ReplyMessage replyMessage = new ReplyMessage(
                            replyToken,
                            textMessage);

                    final BotApiResponse botApiResponse;
                    try {
                        botApiResponse = client.replyMessage(replyMessage).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        return;
                    }

                    System.out.println(botApiResponse);

//                    reply(((MessageEvent) event).getReplyToken(),
//                            new ImageMessage(jpg.getUri(), jpg.getUri()));
                });



    }


    private void convertImage(String replyToken, String url) throws IOException {
        String result = obtainResult(url);
        TextMessage jawabanDalamBentukTextMessage = new TextMessage(result);
        try {
            lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, jawabanDalamBentukTextMessage))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Ada error saat ingin membalas chat");
        }
    }

    private void system(String... args) {
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        try {
            Process start = processBuilder.start();
            int i = start.waitFor();
            log.info("result: {} =>  {}", Arrays.toString(args), i);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            log.info("Interrupted", e);
            Thread.currentThread().interrupt();
        }
    }



    private void handleHeavyContent(String replyToken, String messageId,
                                    Consumer<MessageContentResponse> messageConsumer) {
        final MessageContentResponse response;
        try {
            response = lineMessagingClient.getMessageContent(messageId)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            //reply(replyToken, new TextMessage("Cannot get image: " + e.getMessage()));
            throw new RuntimeException(e);
        }
        messageConsumer.accept(response);
    }

//    public void imgurUpload() throws Exception {
//        String IMGUR_POST_URI = "http://api.imgur.com/2/upload.xml";
//        String IMGUR_API_KEY = "MY API KEY";
//
//        String file = "C:\\Users\\Shane\\Pictures\\Misc\\001.JPG";
//
//        URL url = new URL("https://api.imgur.com/3/image");
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        String data = URLEncoder.encode("image", "UTF-8") + "="
//                + URLEncoder.encode(IMAGE_URL, "UTF-8");
//    }


    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public String accessAPI(){
        return "";
    }

    public static String obtainResult(String urlbaru) throws IOException {

        String credentialsToEncode = "acc_17a23647a8d18a1" + ":" + "0a829afc68674b632d1b10cda130bfad";
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        String endpoint_url = "https://api.imagga.com/v1/tagging";
        String image_url = urlbaru;

        String url = endpoint_url + "?url=" + image_url;
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        int responseCode = connection.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

        connectionInput.close();

        System.out.println(jsonResponse);

        return jsonResponse;
    }

    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).build()
                .toUriString();
    }

    private static DownloadedContent saveContent(String ext, MessageContentResponse responseBody) {
        log.info("Got content-type: {}", responseBody);

        DownloadedContent tempFile = createTempFile(ext);
        try (OutputStream outputStream = Files.newOutputStream(tempFile.path)) {
            ByteStreams.copy(responseBody.getStream(), outputStream);
            log.info("Saved {}: {}", ext, tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static DownloadedContent createTempFile(String ext) {
        String fileName = LocalDateTime.now().toString() + '-' + UUID.randomUUID().toString() + '.' + ext;
        Path tempFile = BotExampleApplication.downloadedContentDir.resolve(fileName);
        tempFile.toFile().deleteOnExit();
        return new DownloadedContent(
                tempFile,
                createUri("/downloaded/" + tempFile.getFileName()));
    }

    @Value
    public static class DownloadedContent {
        Path path;
        String uri;

        public DownloadedContent(Path tempFile, String uri) {
            this.path = tempFile;
            this.uri = uri;
        }
    }

}

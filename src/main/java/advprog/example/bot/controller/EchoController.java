package advprog.example.bot.controller;

import com.linecorp.bot.model.event.FollowEvent;
import org.springframework.beans.factory.annotation.Autowired;
import advprog.example.bot.BotExampleApplication;
import com.google.common.io.ByteStreams;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.client.MessageContentResponse;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
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

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.logging.Logger;

import java.io.*;
import java.net.*;

import lombok.Value;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@Slf4j
@LineMessageHandler
public class EchoController {

    // Core Variable Initialize
    private static final String channelToken = "KSmqRwScpX3fa90zQNt2MDrmtpXwNTGtVyq2K0Jyi2/gfuMSO4cSEaPBkozhqSBrJDIcDJg/XrcVzmyrTTm7omrS1EcBpXLb/vpKckm1HUQIQAItWEZL3og1u7C2G+tK2c4TlJ+Xw0Dd/EpkFh1hsQdB04t89/1O/w1cDnyilFU=";
    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());
    private boolean convertReq = false;

    @Autowired
    public LineMessagingClient lineMessagingClient;

    // Handle Text Input From User
    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String replyToken = event.getReplyToken();

        if(contentText.split(" ")[0].equalsIgnoreCase("/url") && contentText.length()==2){
            String imageSource = contentText.split(" ")[1];
            String result = obtainResult(imageSource);
            return new TextMessage(result);
        }

        if(contentText.equalsIgnoreCase("/ocr this")){
            this.convertReq = true;
            return new TextMessage("Send the img bitj");
        }

//        if(contentText.equalsIgnoreCase("/help")) {
//            String result = "Command : "
//                    + "\n/Ocr this : Memerintahkan bot untuk convert gambar tulisan tangan"
//                    + "yang dilanjutkan dengan user mengirimkan gambar setelah pemanggilan command ini"
//                    + "\n/Help : Memerintahkan bot untuk menampilkan command list";
//            return new TextMessage(result);
//        }
        String replyText = contentText.replace("/echo", "");
        return new TextMessage(replyText.substring(1));
    }

    // Handle Image Input From User
    @EventMapping
    public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws Exception {
//        LOGGER.warning("masuk ke image handling");
//        if(this.convertReq == false){
//            return;
//        }
//        this.convertReq = false;
//        LOGGER.fine(String.format("abbccc " + "ImageMessageContent(timestamp='%s',content='%s')",
//                event.getTimestamp(), event.getMessage()));
//
//        String replyToken = event.getReplyToken();
//        String imageID = event.getMessage().getId();
//
//        lineMessagingClient = LineMessagingClient
//                .builder(channelToken)
//                .build();
//
//        LOGGER.warning("masuk");
//        replyText(replyToken, "bisa");
//        handleHeavyContent(
//                replyToken,
//                imageID,
//                responseBody -> {
//                    DownloadedContent jpg = saveContent(replyToken,"jpg", responseBody);
//                    DownloadedContent previewImage = createTempFile(replyToken, "jpg");
//                    system(
//                            "convert",
//                            "-resize", "240x",
//                            jpg.path.toString(),
//                            previewImage.path.toString());
//                    File image = new File(jpg.path.toString());
//
//                    JSONObject temp = null;
//                    try {
//                        temp = new JSONObject(Uploader.upload(image));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    // Convert JSONObject Data into String n Extract Needed Data (URL)
//                    String url = JsonToLink(temp);
//                    String result = null;
//                    try {
//                        result = obtainResult(url);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    replyText(replyToken, result);
//                });
        String replyToken = event.getReplyToken();
        replyText(replyToken, "berhasil");

    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    @EventMapping
    public TextMessage handleFollowEvent(FollowEvent event) {
        return new TextMessage("Hai! Kenalin aku TulisanBot. \n Untuk menu mengetahui" +
                "command dan bantuan yang tersedia" +
                " bisa menggunakan command /help");
    }

    public String JsonToLink(JSONObject temp){
        JSONObject dataExtract = (JSONObject) temp.get("data");
        String url = (String) dataExtract.get("link");
        return url;
    }

    // Line Reply Message API
    public void replyText(String replyToken, String result){
        lineMessagingClient = LineMessagingClient
                .builder(channelToken)
                .build();

        final TextMessage textMessage = new TextMessage(result);
        final ReplyMessage replyMessage = new ReplyMessage(
                replyToken,
                textMessage);

        final BotApiResponse botApiResponse;
        try {
            botApiResponse = lineMessagingClient.replyMessage(replyMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }
    }

    public void system(String... args) {
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


    public void handleHeavyContent(String replyToken, String messageId,
                                   Consumer<MessageContentResponse> messageConsumer) {
        final MessageContentResponse response;
        try {
            response = lineMessagingClient.getMessageContent(messageId)
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            replyText(replyToken, "Cannot get image: " + e.getMessage());
            throw new RuntimeException(e);
        }
        messageConsumer.accept(response);
    }

    // Create URI using Servlet URI Builder
    public static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).build()
                .toUriString();
    }

    // Save URL to Downloaded Content Directory via createTempFile
    public static DownloadedContent saveContent(String replyToken, String ext, MessageContentResponse responseBody) {
        log.info("Got content-type: {}", responseBody);
        DownloadedContent tempFile = createTempFile(replyToken, ext);
        try (OutputStream outputStream = Files.newOutputStream(tempFile.path)) {
            ByteStreams.copy(responseBody.getStream(), outputStream);
            log.info("Saved {}: {}", ext, tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    // Create Temp File
    public static DownloadedContent createTempFile(String replyToken, String ext) {
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



    public static String obtainResult(String urlbaru) throws IOException {
        String credentialsToEncode = "acc_17a23647a8d18a1" + ":" + "0a829afc68674b632d1b10cda130bfad";
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        String endpoint_url = "https://api.imagga.com/v1/colors";
        String image_url = "https://imagga.com/static/images/tagging/wind-farm-538576_640.jpg";

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

        return jsonResponse;
    }
}

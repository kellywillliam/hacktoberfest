package advprog.example.bot.controller;

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

import advprog.example.bot.BotExampleApplication;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.jayway.jsonpath.internal.Path;
import com.linecorp.bot.client.LineMessagingClient;

import java.awt.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.logging.Logger;
import org.json.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;


@LineMessageHandler
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        

        String replyText = contentText.replace("/echo", "");
        return new TextMessage(replyText.substring(1) + " halo");
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
    
    public static class DownloadedContent {
        Path path;
        String uri;
    }
    
    private static DownloadedContent createTempFile(String ext) {
        String fileName = LocalDateTime.now().toString() + '-' + UUID.randomUUID().toString() + '.' + ext;
        Path tempFile = (Path) BotExampleApplication.downloadedContentDir.resolve(fileName);
        ((java.nio.file.Path) tempFile).toFile().deleteOnExit();
        return new DownloadedContent(
                tempFile,
                createUri("/downloaded/" + tempFile.getFileName()));
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
    
    @EventMapping
    public void handleImageMessageEvent(MessageEvent<ImageMessageContent> event) throws IOException {
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
                            previewImg.path.toString()
                    );
                                       
                    final LineMessagingClient client = LineMessagingClient
                            .builder("<channel access token>")
                            .build();

                    final TextMessage textMessage = new TextMessage("hello");
                    final ReplyMessage replyMessage = new ReplyMessage(
                            "<replyToken>",
                            textMessage);

                    final BotApiResponse botApiResponse;
                    try {
                        botApiResponse = client.replyMessage(replyMessage).get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        return;
                    }

                    System.out.println(botApiResponse);
                });
    }
    
    private void handleHeavyContent(String replyToken, String messageId,Consumer<MessageContentResponse> messageConsumer) {
    	LineMessagingClient lineMessagingClient;
    	final MessageContentResponse response;
		try {
			response = lineMessagingClient.getMessageContent(messageId).get();
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
		messageConsumer.accept(response);
}
    


    
    
    public String getImagga(String urlBaru) throws IOException{
    	String credentialsToEncode = "acc_17a23647a8d18a1" + ":" + "0a829afc68674b632d1b10cda130bfad";
    	String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

    	String endpoint_url = "https://api.imagga.com/v1/colors";
    	String image_url = urlBaru;

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
    	
    	return("a");
    }
    
}

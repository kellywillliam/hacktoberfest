package advprog.example.bot.controller;

import advprog.example.bot.BotExampleApplication;
import advprog.example.bot.confidence.percentage.ConfidencePercentage;
import advprog.example.bot.line.ImgurApi;

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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UncheckedIOException;

import java.nio.file.Files;
import java.nio.file.Path;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;





@LineMessageHandler
public class SfwCheckerController {

    private static final Logger LOGGER = Logger.getLogger(SfwCheckerController.class.getName());
    private static String reply = "";

    public static void main(String[] args) throws IOException {
        System.out.println(ConfidencePercentage.getConfidencePercentage("https://en.wikipedia.org/wiki/File:Brazilian_amazon_rainforest.jpg"));
    }

    @Autowired
    private LineMessagingClient lineMessagingClient;


    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
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
                    break;
                case "/echo":
                    replyText = contentText.replace("/echo", "")
                            .substring(1);
                    break;
                default:
                    break;
            }
            return new TextMessage(replyText);
        } catch (IOException ex) {
            return new TextMessage("api mati");
        }
    }

    @EventMapping
    public TextMessage handleImageMessage(MessageEvent<ImageMessageContent> event) {
        LOGGER.fine(String.format("ImageMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));

        final LineMessagingClient client = LineMessagingClient
                .builder("+uFmWifpVZJBF1ZuxCaIeiFA7v4FF6D4djy+NitngehBdGNjpKc7"
                        + "ICYgFZHLFP7L/yuaH+YAIxi22WOgCGGVkwHhjWuJyE+l38fBNOhb+A2G6gNJgwFBH"
                        + "Q2f+B5ud6ofr7V7oH3ZNKD9scEl+FMTkwdB04t89/1O/w1cDnyilFU=")
                .build();

        final MessageContentResponse messageContentResponse;
        String replyToken = event.getReplyToken();
        String imageID = event.getMessage().getId();


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
                    temp = new JSONObject(ImgurApi.upload(image));
                    JSONObject data = (JSONObject) temp.get("data");
                    String img = (String) data.get("link");
                    reply = ConfidencePercentage.getConfidencePercentage(img);

                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }




            });
        return new TextMessage(reply);

    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    private void system(String... args) {
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        try {
            Process start = processBuilder.start();
            int i = start.waitFor();
            //log.info("result: {} =>  {}", Arrays.toString(args), i);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (InterruptedException e) {
            //log.info("Interrupted", e);
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

            throw new RuntimeException(e);
        }
        messageConsumer.accept(response);
    }

    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(path).build()
                .toUriString();
    }

    private static DownloadedContent saveContent(String ext, MessageContentResponse responseBody) {
        //log.info("Got content-type: {}", responseBody);
        DownloadedContent tempFile = createTempFile(ext);
        try (OutputStream outputStream = Files.newOutputStream(tempFile.path)) {
            ByteStreams.copy(responseBody.getStream(), outputStream);
            //log.info("Saved {}: {}", ext, tempFile);
            return tempFile;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static DownloadedContent createTempFile(String ext) {
        String fileName = LocalDateTime.now().toString() + '-'
                + UUID.randomUUID().toString() + '.' + ext;
        Path tempFile = BotExampleApplication.downloadedContentDir.resolve(fileName);
        tempFile.toFile().deleteOnExit();
        return new DownloadedContent(
                tempFile,
                createUri("/downloaded/" + tempFile.getFileName()));
    }

    public static class DownloadedContent {
        Path path;
        String uri;

        public DownloadedContent(Path tempFile, String uri) {
            this.path = tempFile;
            this.uri = uri;
        }
    }



}

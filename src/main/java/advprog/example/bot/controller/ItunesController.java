package advprog.example.bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.AudioMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@LineMessageHandler
public class ItunesController {
    public static void main(String[] args) throws IOException{
        System.out.println(connectApi());
    }

    private static final Logger LOGGER = Logger.getLogger(ItunesController.class.getName());
    private static final String channelToken
            = "KSmqRwScpX3fa90zQNt2MDrmtpXwNTGtVy"
            + "q2K0Jyi2/gfuMSO4cSEaPBkozhqSBrJDIcDJg/"
            + "XrcVzmyrTTm7omrS1EcBpXLb/vpKckm1HUQIQAItWE"
            + "ZL3og1u7C2G+tK2c4TlJ+Xw0Dd"
            + "/EpkFh1hsQdB04t89/1O/w1cDnyilFU=";

    @Autowired
    public LineMessagingClient lineMessagingClient;

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        if (contentText.equalsIgnoreCase("hehe")) {
            return new TextMessage("bacot");
        }

        if (contentText.equalsIgnoreCase("check")) {
            String previewUrl = connectApi();
            replyAudio(event.getReplyToken(), previewUrl);
            return new TextMessage(previewUrl);
        }

        if (contentText.equalsIgnoreCase("checkwoi")) {
            String previewUrl = connectApi();
            replyAudio(event.getReplyToken(), previewUrl);
            return new TextMessage("bacotcuk");
        }

        String replyText = contentText.replace("/echo", "");
        return new TextMessage(replyText.substring(1));
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public static String connectApi() throws IOException {
        URL url = new URL("https://itunes.apple.com/search?term=jack+johnson");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONObject json = new JSONObject(response.toString());
        String previewUrl = convertJson(json);
        System.out.println(previewUrl);
        return previewUrl;
    }

    public static String convertJson(JSONObject temp) {
        JSONArray dataExtract = (JSONArray) temp.get("results");
        Random rand = new Random();

        int n = rand.nextInt(dataExtract.length());
        System.out.println("length : " + n);
        JSONObject randomAlbum = (JSONObject) dataExtract.get(n);
        String preview = (String) randomAlbum.get("previewUrl");
        return preview;
    }


    public void replyAudio(String replyToken, String result) {

        lineMessagingClient = LineMessagingClient
                .builder(channelToken)
                .build();

        final AudioMessage audioMessage = new AudioMessage(result, 10000);

        final ImageMessage imageMessage = new ImageMessage(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/Download_on_iTunes.svg/800px-Download_on_iTunes.svg.png"
                ,"https://upload.wikimedia.org/wikipedia/commons/thumb/5/55/Download_on_iTunes.svg/800px-Download_on_iTunes.svg.png");

        List<Message> message = new ArrayList<Message>();
        message.add(audioMessage);
        message.add(imageMessage);

        final ReplyMessage replyMessage = new ReplyMessage(
                replyToken,
                message);

        final BotApiResponse botApiResponse;
        try {
            botApiResponse = lineMessagingClient.replyMessage(replyMessage).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }
    }




}

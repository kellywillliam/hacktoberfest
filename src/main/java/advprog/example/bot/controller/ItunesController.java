package advprog.example.bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.soap.Text;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@LineMessageHandler
public class ItunesController {

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
            String check = connectApi(event.getReplyToken());
            replyText(event.getReplyToken(), check);
            return new TextMessage(check);
        }

        String replyText = contentText.replace("/echo", "");
        return new TextMessage(replyText.substring(1));
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    @EventMapping
    public void handleTextMessagetoAudio(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

//    public static SearchResults search(SearchParameters params) {
//        URL url;
//        url = createUrl(searchUrl, buildSearchStringParams(params));
//        HttpURLConnection connection = openConnection(url);
//        return parseResponseData(readResponse(connection));
//    }

    public static String connectApi(String replyToken) throws IOException {
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
        System.out.println(response.toString());
        return response.toString();
    }

    public void replyText(String replyToken, String result) {
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




}

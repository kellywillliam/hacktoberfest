package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@LineMessageHandler
public class BillboardCountryController {

    private static final Logger LOGGER = 
            Logger.getLogger(BillboardCountryController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        if (contentText.startsWith("/echo")) {
            return new TextMessage("echo dari billboard");
        }
        if (!contentText.startsWith("/billboard hotcountry")) {
            return new TextMessage("Wrong input, use /billboard hotcountry "
                    + "<desired artist> to access the feature");
        }
        String replyText = contentText.replace("/billboard hotcountry", "");
        return new TextMessage(getArtist(replyText.substring(1)));
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
    
    public static String getArtist(String name) throws Exception {
        Elements elements = screenScrapeGetArtists(makeGetCall());

        for (Element elm: elements) {
            String artist = elm.getElementsByClass("chart-row__artist").text();
            if (artist.equalsIgnoreCase(name)) {
                String title = elm.getElementsByClass("chart-row__song").text();
                String position = elm.getElementsByClass("chart-row__current-week").text();
                String result = artist + "\n" + title + "\n" + "This week's position: " + position;
                return result;
            }
        }

        return "Your artist is not on the top 50 Hot Country Songs list";
    }

    public static String makeGetCall() throws Exception {
        String url = "https://www.billboard.com/charts/country-songs";
        final HttpClient client = HttpClientBuilder.create().build();
        final HttpGet get = new HttpGet(url);

        HttpResponse response = client.execute(get);
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(new 
                InputStreamReader(response.getEntity().getContent()));
        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        System.out.println(result.toString());
        return result.toString();
    }

    public static Elements screenScrapeGetArtists(String html) {
        Document doc = Jsoup.parse(html);
        Elements content = doc.select(".chart-row__primary");
        return content;
    }
}

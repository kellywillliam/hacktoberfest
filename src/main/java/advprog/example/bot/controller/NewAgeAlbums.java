package advprog.example.bot.controller;

import advprog.example.bot.BotExampleApplication;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


@LineMessageHandler
public class NewAgeAlbums {

    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        ArrayList<String> arrArtist = new ArrayList<>();
        ArrayList<String> arrTitle = new ArrayList<>();

        if (contentText.equalsIgnoreCase("/newage")) {

            String url = "https://www.billboard.com/charts/new-age-albums";
            Document doc = Jsoup.connect(url).get();

            Elements artist = doc.getElementsByClass("chart-row__artist");
            Elements title = doc.getElementsByClass("chart-row__song");

            for (Element e: artist) {
                arrArtist.add(e.text());
            }

            for (Element e: title) {
                arrTitle.add(e.text());
            }

            for (int i = 0; i < arrArtist.size(); i++) {
                System.out.println("(" + (i + 1) + ") " + arrArtist.get(i) + " - " + arrTitle.get(i));
            }

        }

        String replyText = "";

        for (int i = 0; i < arrArtist.size(); i++) {
            replyText = replyText + ("(" + (i + 1) + ") " + arrArtist.get(i) + " - " + arrTitle.get(i)) + "\n";
        }

        replyText = replyText + "\nThank you for using our service";

        return new TextMessage(replyText);
    }
}

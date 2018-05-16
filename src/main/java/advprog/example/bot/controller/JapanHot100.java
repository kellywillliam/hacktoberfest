package advprog.example.bot.controller;

import advprog.example.bot.BotExampleApplication;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


@LineMessageHandler
public class JapanHot100 {
    private static final Logger LOGGER = Logger.getLogger(BotExampleApplication.class.getName());
    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException {
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        ArrayList<String> arrArtist = new ArrayList<>();
        ArrayList<String> arrTitle = new ArrayList<>();
        if (contentText.equalsIgnoreCase("/japan100")) {
            String url = "https://www.billboard.com/charts/japan-hot-100";
            Document doc = Jsoup.connect(url).get();
            Elements artist = doc.getElementsByClass("chart-row__artist");
            Elements title = doc.getElementsByClass("chart-row__song");
            for (int i = 0; i < 10; i++) {
                Element e = artist.get(i);
                arrArtist.add(e.text());
            }
            for (int i = 0; i < 10; i++) {
                Element e = title.get(i);
                arrTitle.add(e.text());
            }
        }
        String replyText = "";
        for (int i = 0; i < arrArtist.size(); i++) {
            replyText = replyText+("("+(i+1)+") "+arrArtist.get(i)+" - "+arrTitle.get(i))+"\n";
        }
        replyText = replyText+"\nThank you for using our service";
        return new TextMessage(replyText);
    }
}

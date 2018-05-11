package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class YoutubeScrappingController {

    private static final Logger LOGGER = Logger.getLogger(YoutubeScrappingController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
				try {
            final Document document = Jsoup.connect("https://scele.cs.ui.ac.id/").get();
            for (Element row : document.select("div#site-news-forum div.forumpost.clearfix.firstpost.starter")) {

                final Document doc1 = Jsoup.parse(row.select(".link").html());
                Element link1 = doc1.select("a").first();

                final Document doc2 = Jsoup.parse(row.select(".author").html());
                Element link2 = doc2.select("a").first();

                final String judul = row.select(".subject").text();
                final String alamatHtml = link1.attr("href");
                final String author = link2.text();

                title += judul + " \nby " + author + "\n" + alamatHtml + "\n\n";
                System.out.println(title);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return title;
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

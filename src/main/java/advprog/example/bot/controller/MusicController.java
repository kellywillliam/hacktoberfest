package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.IOException;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

import java.util.List;
import java.util.logging.Logger;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

@LineMessageHandler
public class MusicController {

    private static final Logger LOGGER = Logger.getLogger(MusicController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event)
            throws IOException {
        Date date = new Date(); // your date
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        Document document;
        String reply = "";
        try {
            //Get Document object after parsing the html from given url.
            String home = "https://vgmdb.net/db/calendar.php?year=" + year + "&month=" + month;
            document = Jsoup.connect(home).get();

            String title = document.title(); //Get title
            System.out.println("  Title: " + title); //Print title.

            List<String> albums = new ArrayList<>();

            Elements links = document.select("ul.album_infobit_detail");
            for (Element link : links) {
                Elements tt = link.children();
                int counter = 0;
                String hasil = "";
                for (Element yy : tt) {
                    if (counter == 0) {

                        hasil += yy.select("a").first().attr("title");
                        hasil += "   :   ";

                    } else if (counter == 1) {

                        Element yu = yy.select("acronym").first();
                        double number = 0.0;
                        try {
                            number = Double.parseDouble(yy.ownText().split(" ")[0]);
                        } catch (Exception e) {
                            number = 0.0;
                        }
                        try {
                            if (yu.attr("title").contains("USD")) {
                                number = number * 14173;
                            } else if (yu.attr("title").contains("JPY")) {
                                number = number * 127;
                            } else if (yu.attr("title").contains("EUR")) {
                                number = number * 16656;
                            } else if (yu.attr("title").contains("TWD")) {
                                number = number * 472;
                            }
                        } catch (Exception e) {
                            continue;
                        }

                        hasil += (number + " ");

                        if (yu != null) {
                            hasil += "IDR Indonesia";
                        }
                    }
                    counter++;
                }
                String z = hasil.toLowerCase();
                if (z.contains("original") && z.contains("soundtrack")) {
                    albums.add(hasil);
                }
            }

            for (String album : albums) {
                reply += "running... please wait\n" + album + "\n";
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("done");
        return new TextMessage(reply);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}
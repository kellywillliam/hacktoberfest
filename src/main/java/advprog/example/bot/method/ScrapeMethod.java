package advprog.example.bot.method;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


public class ScrapeMethod {

    public static Document getDoc(String link) {
        Document document;
        try {
            String url = link;
            document = Jsoup.connect(url).get();
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String showAnime() {
        Element element = getDoc("https://www.livechart.me/schedule/all")
                .getElementsByClass("chart compact").get(0);
        String anime = "";

        if (element == null) {
            anime = "no anime airing today";
        } else {
            Elements a = element.getElementsByTag("a");
            for (Element elem: a){
                String title = elem.getElementsByClass("schedule-card-title").text();
                anime += title + " \n";


            }
//            String link = a.attr("href");
//            String episode = getDoc("https://livechart.me/" + link)
//                    .getElementsByClass("anime-episodes").get(0).text();

        }
        return anime;

    }

    public static String scrapePrivate() {
        return null;
    }

}

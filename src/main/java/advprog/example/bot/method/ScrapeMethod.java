package advprog.example.bot.method;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class ScrapeMethod {

    public static Document getDoc() {
        Document document;
        try {
            String url = "https://www.livechart.me/";
            document = Jsoup.connect(url).get();
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Elements scrapeForGroup(Document document) {
        if (document != null) {
            Elements content = document.select(".chart");
            return content;
        }
        return null;
    }

    public static String showAnime() {
        Elements elements = scrapeForGroup(getDoc());
        String anime = "";

        if (elements == null) {
            anime = "no anime airing this season";
        } else {
            for (Element element : elements) {
                String title = element.getElementsByClass("main-title").text();
                String episode = element.getElementsByClass("anime-episodes").text();
                anime += title + " - " + episode + "\n";
            }
        }
        return anime;

    }

    public static String scrapePrivate() {
        return null;
    }

}

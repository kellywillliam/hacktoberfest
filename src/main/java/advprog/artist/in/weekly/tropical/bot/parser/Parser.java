package advprog.artist.in.weekly.tropical.bot.parser;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {
    ArrayList<String> arrOfArtist = new ArrayList<>();
    ArrayList<String> arrOfSong = new ArrayList<>();
    Document doc;

    public Parser() {
        doc = getHtml("https://www.billboard.com/charts/tropical-songs");
        Elements artists = doc.select("article.chart-row");
        for (Element artist: artists) {
            String song = artist.select("h2.chart-row__song").text().toLowerCase();
            String artisA = artist.select("a.chart-row__artist").text().toLowerCase();
            String artisSpan = artist.select("span.chart-row__artist").text().toLowerCase();
            if (artisA.equals("")) {
                arrOfArtist.add(artisSpan);
            } else {
                arrOfArtist.add(artisA);
            }
            arrOfSong.add(song);
        }
    }

    public ArrayList<String> getArrayArtist() {
        return arrOfArtist;
    }

    public ArrayList<String> getArraySong() {
        return arrOfSong;
    }

    public Document getHtml(String url) {
        Document doc;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
            doc = null;
        }
        return doc;
    }
}

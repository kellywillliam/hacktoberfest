package advprog.example.bot.countryhot;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HotNewAgeSong {
    private ArrayList<SongInfo> hotNewAge;

    public HotNewAgeSong() {
        hotNewAge = new ArrayList<>();
    }

    public ArrayList<SongInfo> getDataFromBillboard() {
        String targetUrl = "https://www.billboard.com/charts/new-age-albums";
        try {
            Document document = Jsoup.connect(targetUrl).get();
            Elements chartRows = document.getElementsByClass("chart-row");
            for (Element chartRow : chartRows) {
                String songArtist = chartRow.getElementsByClass("chart-row__artist").html();
                String songTitle = chartRow.getElementsByClass("chart-row__song").html();
                String currentRank = chartRow.getElementsByClass("chart-row__current-week").html();
                int rank = Integer.parseInt(currentRank);
                hotNewAge.add(new SongInfo(songArtist, songTitle, rank));
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return hotNewAge;


    }
}

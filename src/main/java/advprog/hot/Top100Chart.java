package advprog.hot;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Top100Chart {
    private ArrayList<SongInfo> top100;

    public Top100Chart() {
        top100 = new ArrayList<>();
    }

    public ArrayList<SongInfo> getDataFromBillboard() {
        String targetUrl = "https://www.billboard.com/charts/hot-100";
        try {
            Document document = Jsoup.connect(targetUrl).get();
            Elements chartRows = document.getElementsByClass("chart-row");
            for (Element chartRow : chartRows) {
                String songArtist = chartRow.getElementsByClass("chart-row__artist").html();
                String songTitle = chartRow.getElementsByClass("chart-row__song").html();
                String currentRank = chartRow.getElementsByClass("chart-row__current-week").html();
                int rank = Integer.parseInt(currentRank);
                top100.add(new SongInfo(songArtist, songTitle, rank));
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return top100;
    }
}
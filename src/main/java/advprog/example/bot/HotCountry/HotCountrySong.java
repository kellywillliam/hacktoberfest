package advprog.example.bot.HotCountry;


import java.io.IOException;
import java.util.ArrayList;
import java.net.SocketTimeoutException;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HotCountrySong {
    private ArrayList<SongInfo> HotCountry;

    public HotCountrySong() {
        HotCountry = new ArrayList<>();
    }

    public ArrayList<SongInfo> getDataFromBillboard() {
        String targetUrl = "https://www.billboard.com/charts/country-songs";
        try {
            Document document = Jsoup.connect(targetUrl).get();
            Elements chartRows = document.getElementsByClass("chart-row");
            for (Element chartRow : chartRows) {
                String songArtist = chartRow.getElementsByClass("chart-row__artist").html();
                String songTitle = chartRow.getElementsByClass("chart-row__song").html();
                String currentRank = chartRow.getElementsByClass("chart-row__current-week").html();
                int rank = Integer.parseInt(currentRank);
                HotCountry.add(new SongInfo(songArtist, songTitle, rank));
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return HotCountry;


    }
}

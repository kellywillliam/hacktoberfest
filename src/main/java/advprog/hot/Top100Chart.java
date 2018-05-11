package advprog.hot;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

class Top100Chart {
    private ArrayList<SongInfo> top100;

    public Top100Chart() {
        top100 = new ArrayList<>();
    }

    public void getDataFromBillboard() {
        String targetURL = "https://www.billboard.com/charts/hot-100";
        //get Data via JSOUP.
    }

    public void setTop100Array(Elements data) {
        for(int i = 0; i < data.size(); i++) {
            top100.set(i, new SongInfo(data.songArtist, data.songTitle, data.rank));
        }
    }

}
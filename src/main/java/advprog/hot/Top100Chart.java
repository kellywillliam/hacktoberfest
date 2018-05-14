package advprog.hot;

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

    public void getDataFromBillboard() {
        String targetURL = "https://www.billboard.com/charts/hot-100";
        //get Data via JSOUP.
    }

}

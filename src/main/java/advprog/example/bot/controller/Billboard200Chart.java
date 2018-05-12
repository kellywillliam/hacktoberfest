package advprog.example.bot.controller;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class Billboard200Chart {

    private static final String url = "https://www.billboard.com/charts/billboard-200";
    private static Billboard200Chart instance;

    private Billboard200Chart() {}

    public static Billboard200Chart getInstance() {
        if (instance == null) {
            instance = new Billboard200Chart();
        }
        return instance;
    }

    public String top10Tracks() {
        String res = "";
        try {
            Document document = Jsoup.connect("https://www.billboard.com/charts/billboard-200").get();
            int counter = 1;
            for (Element row : document.select("div.chart-row__title")) {
                String title = Jsoup.parse(row.select("h2.chart-row__song").html()).text();
                String singer = Jsoup.parse(row.select("a.chart-row__artist").html()).text();
                res += String.format("(%d) %s - %s \n",counter,singer,title);
                if (counter == 10) {
                    break;
                }
                counter++;
            }
            return res.trim();
        } catch (IOException e) {
            e.printStackTrace();
            return "Error on billboard URI, contact the bot owner !";
        }
    }
}

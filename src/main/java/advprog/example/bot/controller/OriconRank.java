package advprog.example.bot.controller;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OriconRank {
    private String param;
    private String date;
    private String url;

    public OriconRank() {
        this.url = "https://www.oricon.co.jp/rank/bd/";
    }

    public String weekly(String date) {
        this.param = "w";
        this.date = date;
        this.url += param + "/" + date + "/";
        return search();
    }

    public String daily(String date) {
        this.param = "d";
        this.date = date;
        this.url += param + "/" + date + "/";
        return search();
    }

    public String search() {
        String result = "";
        try {
            Document doc = Jsoup.connect(url).timeout(5000).get();
            Elements links = doc.getElementsByClass("box-rank-entry");
            ArrayList<String> str = new ArrayList<>();

            for (Element link : links) {
                String position = link.getElementsByClass("num").html();
                String title = link.getElementsByClass("title").html();
                String[] releaseArtist = link.getElementsByClass("list").html().split("\n");
                String artist = releaseArtist[1].replace("<li>", "")
                        .replace("</li>", "");
                String releaseDate = releaseArtist[0].replace("<li>発売日： ", "")
                        .replace(" </li>", "")
                        .replace("年","-")
                        .replace("月", "-")
                        .replace("日", "");
                str.add("(" + position + ")" + " " + title + " - " + artist + "- "
                        + releaseDate);
            }

            for (int i = 0; i < str.size(); i++) {
                if (i == str.size() - 1) {
                    result += str.get(str.size() - 1);
                } else {
                    result += str.get(i) + "\n";
                }
            }
        } catch (SocketTimeoutException e) {
            System.out.println("Timeout occured");
        } catch (IOException e) {
            System.out.println("Invalid input");
        }

        return result;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}

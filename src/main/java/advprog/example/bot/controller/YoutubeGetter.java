package advprog.example.bot.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class YoutubeGetter {

    public static void main(String[] args) throws IOException {

        YoutubeGetter yt = new YoutubeGetter();

        yt.getInfoYoutube("https://www.youtube.com/watch?v=kJ5PCbtiCpk");

    }

    public YoutubeGetter() {}

    public void getInfoYoutube(String url) throws IOException {

        String ua = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.181 Safari/537.36";

        Document doc = Jsoup.connect(url).userAgent(ua).get();
        //Document doc = Jsoup.parse(url);

//        String htmlParse = docs.toString();
//
//        Document doc = Jsoup.parse(htmlParse);

        Elements scripts = doc.getElementsByTag("script");

        String title ="";
        String author = "";

        for (Element script : scripts) {
            System.out.println(script.data());
            boolean titleCheck = script.data().contains("document.title");
            boolean authorCheck = script.data().contains("\"author\":\"");

            if(titleCheck) {
                String[] scrip = script.data().split("\n");

                for (String scri: scrip) {
                    int index2 = scri.indexOf("document.title");
                    if (index2 > 0) {
                        String judul = scri.substring(25);
                        title = judul.substring(1,judul.length()-2);
                        break;
                    }
                }
            }

            if(authorCheck) {
                String[] scrip = script.data().split(":");
                for (int i = 0; i < scrip.length; i++) {
                    if (scrip[i].contains("author")) {
                        String[] cenels = scrip[i+1].split(",");
                        String cenel = cenels[0];
                        author = cenel.substring(1, cenel.length()-2);
                    }
                }
            }
        }


//        for (Element script: scripts) {
//            String wow = script.toString();
//            String[] wows = wow.split("\n");
//            for (String we: wows) {
//                String[] wu = we.split(" ");
//                System.out.println(we);
//                System.out.println("////////////////////////////");
//                if (we.length() >= 23) {
//                    System.out.println("Lebih dari 23: " + we);
//                    if (we.substring(9,23).equalsIgnoreCase("document.title")) {
//                        title = we.substring(27);
//                    }
//                }
//            }
//        }

        System.out.println("title: " + title);
        System.out.println("author: " + author);


//        Elements title = doc.getElementsByClass("title");
//        System.out.println("Title: " + title.text());
//
//        Elements author = doc.select("a.yt-simple-endpoint.style-scope.yt-formatted-string");
//        System.out.println("Author: " + author.text());
//
//        Elements numOfViews = doc.getElementsByClass("view-count style-scope yt-view-count-renderer");
//        Elements numOfLikes = doc.getElementsByClass("style-scope ytd-toggle-button-renderer style-text");
//        Elements numOfDislikes = doc.getElementsByClass("style-scope ytd-toggle-button-renderer style-text");

//        String hasil = "";
//        hasil = "Title: " + title.text() + "\n";
//        hasil = "Author: " + author.text() + "\n";
//        hasil = "Num of views: " + numOfViews.text() + "\n";
//        hasil = "Num of likes: " + numOfLikes.text() + "\n";
//        hasil = "Num of dislikes: " + numOfDislikes.text();

    }

    public boolean isAvailable(String url) {

    return true;
    }
}

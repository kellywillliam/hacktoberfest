

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;


public class NewAgeAlbumApplication{
    public static void main(String[] args){
        try {
            String url = "https://www.billboard.com/charts/tropical-songs";
            Document doc = Jsoup.connect(url).timeout(5000).get();
            Elements links = doc.getElementsByClass("chart-row");
            for(Element link : links) {
                String song = link.getElementsByClass("chart-row__song").html();
                String artist = link.getElementsByClass("chart-row__artist").html();
                System.out.println(artist + " - " + song);
            }
        }
        catch (SocketTimeoutException e) {
            System.out.println("Timeout occured");
        }
        catch (IOException e) {
            System.out.println("Invalid input");
        }
    }
}

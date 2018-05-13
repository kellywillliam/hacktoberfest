import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ScreenScrapping {
    public static void main(String[] args) {

        Document document;
        try {
            //Get Document object after parsing the html from given url.
            document = Jsoup.connect("https://www.oricon.co.jp/rank/js/w/2018-05-07/").get();

            Elements box = document.select(".box-rank-entry"); //Get title

            for (int i = 0; i < 10; i++) {
                int counter = i + 1;
                Elements title = box.select(".title");
                Elements artist = box.select(".name");
                Elements infoBox = box.select(".list");

                String info = infoBox.get(i).text();
                if(info.contains("発売日： ")) {
                    String replace = info.replace("発売日： ","");
                    if(replace.contains("推定売上枚数：")) {
                        String secondReplace = replace.replace("推定売上枚数：","");
                        String[] tempInfo = secondReplace.split(" ");
                        String tanggal = tempInfo[0];
                        String estSales = tempInfo[1];

                        print("(" + counter + ") " + title.get(i).text() + " - "
                                        + artist.get(i).text() + " - "
                                        + tanggal.substring(0,4) + "-"
                                        + tanggal.substring(5,7)
                                        + "-" + tanggal.substring(8,10) + " - "
                                        + estSales.substring(0,estSales.length()-1));

                    } else {
                        String[] tempInfo = replace.split(" ");
                        String tanggal = tempInfo[0];
                        String estSales = "Unknown";

                        print("(" + counter + ") " + title.get(i).text() + " - "
                                + artist.get(i).text() + " - "
                                + tanggal.substring(0,4) + "-"
                                + tanggal.substring(5,7)
                                + "-" + tanggal.substring(8,10) + " - "
                                + estSales);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void print(String string) {
        System.out.println(string);
    }
}
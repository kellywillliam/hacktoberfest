//package advprog.example.bot;
//
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.PrintStream;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//
///**
// * This class is used for HTML parsing from URL using Jsoup.
// * @author Andrew Schwartz
// */
//public class Scrapping {
//    public static void main(String args[]){
//        print("running...");
//        Document document;
//        String result = "";
//        try {
//
//            document = Jsoup.connect("https://www.oricon.co.jp/rank/js/d/2018-05-16/").get();
//
//            Elements box = document.select(".box-rank-entry");
//
//            for (int i = 0; i < 10; i++) {
//                int counter = i + 1;
//                Elements title = box.select(".title");
//                Elements artist = box.select(".name");
//                Elements infoBox = box.select(".list");
//
//                String info = infoBox.get(i).text();
//                if (info.contains("発売日： ")) {
//                    String replace = info.replace("発売日： ","");
//                    if (replace.contains("推定売上枚数：")) {
//                        String secondReplace = replace.replace("推定売上枚数：","");
//                        String[] tempInfo = secondReplace.split(" ");
//                        String tanggal = tempInfo[0];
//                        String estSales = tempInfo[1];
//
//                        result += "(" + counter + ") " + title.get(i).text() + " - "
//                                + artist.get(i).text() + " - "
//                                + tanggal.substring(0,4) + "-"
//                                + tanggal.substring(5,7)
//                                + "-" + tanggal.substring(8,10) + " - "
//                                + estSales.substring(0,estSales.length() - 1);
//
//                    } else {
//                        String[] tempInfo = replace.split(" ");
//                        String tanggal = tempInfo[0];
//                        String estSales = "Unknown";
//
//                        result += "(" + counter + ") " + title.get(i).text() + " - "
//                                + artist.get(i).text() + " - "
//                                + tanggal.substring(0,4) + "-"
//                                + tanggal.substring(5,7)
//                                + "-" + tanggal.substring(8,10) + " - "
//                                + estSales;
//                    }
//                }
//
//                if (counter + 1 < 11) {
//                    result += "\n";
//                }
//            }
//
//        } catch (IOException e) {
//            result =  "Tanggal yang anda masukkan salah (Hint: Jika anda mencari informasi "
//                    + "terkait top 10 chart"
//                    + " WEEKLY maka informasi hanya dapat diperoleh untuk "
//                    + "tanggal yang jatuh pada hari SENIN";
//        }
//
//        System.out.print(result);
//    }
//
//
//    public static void print(String string) {
//        System.out.println(string);
//    }
//
//}
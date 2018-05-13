package advprog.example.bot.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;

public class TopChartController {

    public String topChartDaily(String tanggal) {
        if (tanggalChecker(tanggal)) {
            String linkWebsite = "https://www.oricon.co.jp/rank/js/d/" + tanggal + "/";
            return screenScrapping(linkWebsite);
        }

        return "Format yang anda masukkan salah";
    }

    public String topChartWeekly(String tanggal) {
        if(tanggalChecker(tanggal)) {
            String linkWebsite = "https://www.oricon.co.jp/rank/js/w/" + tanggal + "/";
            return screenScrapping(linkWebsite);
        }

        return "Format yang anda masukkan salah";
    }

    public String topChartMonthly(String tahun, String bulan) {
        String tanggal = tahun + '-' + bulan;

        if(tanggalChecker(tanggal)) {
            String linkWebsite = "https://www.oricon.co.jp/rank/js/m/" + tanggal + "/";
            return screenScrapping(linkWebsite);
        }

        return "Format yang anda masukkan salah";
    }

    public String topChartYear(String tahun) {
        if(tanggalChecker(tahun)) {
            String linkWebsite = "https://www.oricon.co.jp/rank/js/y/" + tahun + "/";
            return screenScrapping(linkWebsite);
        }

        return "Format yang anda masukkan salah";
    }

    public boolean tanggalChecker (String tanggal) {
        Calendar cal = Calendar.getInstance();
        String[] temp = tanggal.split("-");
        if(temp.length == 3) {
            int tahun = Integer.parseInt(temp[0]);
            int bulan = Integer.parseInt(temp[1]);
            int tgl = Integer.parseInt(temp[2]);
            if(tahun <= cal.get(Calendar.YEAR)) {
                if(bulan < 12) {
                    if(tgl < 31) {
                        return true;
                    }
                }
            }
        }else if(temp.length == 2) {
            int tahun = Integer.parseInt(temp[0]);
            int bulan = Integer.parseInt(temp[1]);

            if(tahun <= cal.get(Calendar.YEAR)) {
                if(bulan < 12 ) {
                    return true;
                }
            }
        } else {
            int tahun = Integer.parseInt(temp[0]);

            if(tahun <= cal.get(Calendar.YEAR)) {
                return true;
            }
        }
        return false;
    }

    public String screenScrapping (String link) {
        Document document;
        String result="";
        try {
            //Get Document object after parsing the html from given url.
            document = Jsoup.connect(link).get();

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

                        result+= "(" + counter + ") " + title.get(i).text() + " - "
                                + artist.get(i).text() + " - "
                                + tanggal.substring(0,4) + "-"
                                + tanggal.substring(5,7)
                                + "-" + tanggal.substring(8,10) + " - "
                                + estSales.substring(0,estSales.length()-1);

                    } else {
                        String[] tempInfo = replace.split(" ");
                        String tanggal = tempInfo[0];
                        String estSales = "Unknown";

                        result+="(" + counter + ") " + title.get(i).text() + " - "
                                + artist.get(i).text() + " - "
                                + tanggal.substring(0,4) + "-"
                                + tanggal.substring(5,7)
                                + "-" + tanggal.substring(8,10) + " - "
                                + estSales;
                    }
                }

                if(counter + 1 < 11){
                    result+="\n";
                }
            }

        } catch (IOException e) {
            return "Tanggal yang anda masukkan salah (Hint: Jika anda mencari informasi " +
                    "terkait top 10 chart" +
                    " WEEKLY maka informasi hanya dapat diperoleh untuk " +
                    "tanggal yang jatuh pada hari SENIN";
        }

        return result;
    }

}

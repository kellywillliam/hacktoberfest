package advprog.example.bot.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class OriconRank {
    private String date;
    private String url;

    String weeklyDailyWrong = "Pesan yang kamu kirimkan belum sesuai format. "
            + "Pastikan kamu menuliskan 'weekly' atau 'daily' dengan benar.";
    String notEnoughInput = "Pesan yang kamu kirimkan belum sesuai format. "
            + "Pastikan format yang kamu kirimkan sudah lengkap.";
    String randomInput = "Halo, terima kasih atas pesan yang dikirimkan. \n"
            + "Untuk menggunakan bot ini, silakkan kirimkan pesan dengan format"
            + "'/oricon bluray [weekly/daily] [YYYY-MM-DD]' \n"
            + "Contoh: /oricon bluray weekly 2018-05-14";
    String invalidDate = "Tanggal yang kamu masukkan tidak valid. Silakkan coba lagi.";
    String noRecord = "Tidak ada data yang ditemukan.";

    public OriconRank() {
        this.url = "https://www.oricon.co.jp/rank/bd/";
    }

    public String execute(String contentText) {
        String[] command = contentText.split(" ");
        String replyText = "";

        if (command[0].equals("/oricon") && command[1].equals("bluray")) {
            try {
                String dateGiven = command[3];
                if (isValidDate(dateGiven)) {
                    this.date = dateGiven;
                } else {
                    return invalidDate;
                }

                if (command[2].equals("weekly")) {
                    replyText = weekly();
                } else if (command[2].equals("daily")) {
                    replyText = daily();
                } else {
                    replyText = weeklyDailyWrong;
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                replyText = notEnoughInput;
            }

        } else {
            replyText = randomInput;
        }

        return replyText;
    }

    public String weekly() {
        this.url += "w" + "/" + date + "/";
        return search();
    }

    public String daily() {
        this.url += "d" + "/" + date + "/";
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

        } catch (IOException e) {
            return noRecord;
        }

        return result;
    }

    public boolean isValidDate(String inDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(inDate.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}

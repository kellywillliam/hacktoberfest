package advprog.example.bot.controller;

import fastily.jwiki.core.*;
import fastily.jwiki.dwrap.*;
import org.apache.tomcat.util.http.fileupload.FileUtils;

import java.io.*;
import java.util.Scanner;

public class MediaWikiController {
    String randomInput;
    String notEnoughInput;
    String addWikiSuccess;
    String addWikiFail;
    String addWikiOldUrl;

    public MediaWikiController() {
        this.randomInput = "Halo, terima kasih atas pesan yang dikirimkan. "
                + "Untuk menggunakan bot ini, ada dua perintah yang bisa dilakukan:\n"
                + "1. /add_wiki [URL-API-endpoint]\n"
                + "Contoh: /add_wiki http://marvel.wikia.com/api.php\n"
                + "2. /random_wiki_article";
        this.notEnoughInput = "Pesan yang kamu kirimkan belum sesuai format. "
                + "Pastikan format yang kamu kirimkan sudah lengkap.";
        this.addWikiSuccess = "URL berhasil ditambahkan.";
        this.addWikiFail = "URL yang kamu masukkan tidak valid. Silakan coba lagi.";
        this.addWikiOldUrl = "URL yang kamu masukkan sudah terdaftar.";
    }

    public String execute(String contentText) {
        String[] command = contentText.toLowerCase().split(" ");
        String replyText = "";

        if (command[0].equals("/add_wiki")) {
            try {
                String urlGiven = command[1];
                if (isValidUrl(urlGiven)) {
                    if (isNewUrl(urlGiven)) {
                        saveUrl(urlGiven);
                        replyText = addWikiSuccess;
                    } else {
                        replyText = addWikiOldUrl;
                    }

                } else {
                    replyText = addWikiFail;
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                replyText = notEnoughInput;
            }

        } else if (command[0].equals("/random_wiki_article")) {
            replyText = "test";
        } else {
            replyText = randomInput;
        }

        return replyText;
    }

    public boolean isValidUrl(String urlGiven) {
        try {
            Wiki wiki = new Wiki(urlGiven);
            wiki.getCategoriesOnPage("Main Page");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isNewUrl(String urlGiven) {
        File file = new File("./wikiUrl.csv");
        boolean res = false;
        try {
            Scanner scanner = new Scanner(file);
            int lineNum = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                lineNum++;
                if (line.equalsIgnoreCase(urlGiven)) {
                    res = false;
                    break;
                }
                else {
                    res = true;
                }
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }

    public void saveUrl(String url) {
        try {
            FileWriter fw = new FileWriter("./wikiUrl.csv", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(url);
            pw.flush();
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

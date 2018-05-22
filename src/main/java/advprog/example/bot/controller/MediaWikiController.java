package advprog.example.bot.controller;

import fastily.jwiki.core.NS;
import fastily.jwiki.core.Wiki;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class MediaWikiController {
    private String randomInput;
    private String notEnoughInput;
    private String addWikiSuccess;
    private String addWikiInvalidUrl;
    private String addWikiOldUrl;
    private String urlList;
    private Wiki wiki;

    public MediaWikiController() {
        this.randomInput = "Halo, terima kasih atas pesan yang dikirimkan. "
                + "Untuk menggunakan bot ini, ada dua perintah yang bisa dilakukan:\n"
                + "1. /add_wiki [URL-API-endpoint]\n"
                + "Contoh: /add_wiki http://marvel.wikia.com/api.php\n"
                + "2. /random_wiki_article";
        this.notEnoughInput = "Pesan yang kamu kirimkan belum sesuai format. "
                + "Pastikan format yang kamu kirimkan sudah lengkap.";
        this.addWikiSuccess = "URL berhasil ditambahkan.";
        this.addWikiInvalidUrl = "URL yang kamu masukkan tidak valid. Silakan coba lagi.";
        this.addWikiOldUrl = "URL yang kamu masukkan sudah terdaftar.";
        this.urlList = "./wikiUrl.csv";
    }

    public String execute(String contentText) {
        String[] command = contentText.toLowerCase().split(" ");
        String replyText = "";

        if (command[0].equals("/add_wiki")) {
            try {
                String urlGiven = command[1];
                replyText = addWiki(urlGiven);
            } catch (ArrayIndexOutOfBoundsException e) {
                replyText = notEnoughInput;
            }

        } else if (command[0].equals("/random_wiki_article")) {
            replyText = getArticle();

        } else {
            replyText = randomInput;
        }

        return replyText;
    }

    private String getArticle() {
        ArrayList<String> rand = wiki.getRandomPages(1, NS.MAIN);
        String title = rand.get(1);
        return wiki.getPageText(title);
    }

    private String addWiki(String urlGiven) {
        String replyText = "";
        if (isValidUrl(urlGiven)) {
            if (isNewUrl(urlGiven)) {
                saveUrl(urlGiven);
                replyText = addWikiSuccess;
            } else {
                replyText = addWikiOldUrl;
            }
        } else {
            replyText = addWikiInvalidUrl;
        }
        return replyText;
    }

    private boolean isValidUrl(String urlGiven) {
        try {
            Wiki wiki = new Wiki(urlGiven);
            wiki.getCategoriesOnPage("Main Page");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isNewUrl(String urlGiven) {
        File file = new File(urlList);
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
                } else {
                    res = true;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }

    private void saveUrl(String url) {
        try {
            FileWriter fw = new FileWriter(urlList, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(url);
            pw.flush();
            pw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUrlList() {
        return urlList;
    }

    public void setUrlList(String urlList) {
        this.urlList = urlList;
    }
}
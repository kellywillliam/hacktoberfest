package advprog.example.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UrlDatabase {
    private Map<String, FakeNews> listUrl;
    private final String path = "url.csv";
    private BufferedWriter writer;
    private BufferedReader reader;
    private FileWriter fileWriter;

    public UrlDatabase() {
        listUrl = new HashMap<>();
        this.loadingAllData();
    }

    private void loadingAllData() {
        try {
            this.reader = new BufferedReader(new FileReader(path));
            String readLine = this.reader.readLine();

            while (readLine != null) {
                collectFakeNews(readLine);
                readLine = this.reader.readLine();
            }
            System.out.println(listUrl.size());
            this.reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void collectFakeNews(String input) {
        String[] arr = input.split(",");
        String url = arr[0].trim();
        Set<String> types = new HashSet<>();
        for (int i = 1; i < 4; i++) {
            String masukan = arr[i].trim();
            if (!masukan.equals("")) {
                types.add(masukan);
            }
        }
        addToList(url, types);
    }

    public synchronized String checkUrl(String cmd, String url) {
        String hasil = "http://" + url + "not found in database\n";
        if (this.listUrl.containsKey(url)) {
            FakeNews news = this.listUrl.get(url);
            if (cmd.equals("conspiracy") && news.isConspiracy()) {
                hasil = "http://" + url + " is conspiracy news site\n";
            } else if (cmd.equals("satire") && news.isSatire()) {
                hasil = "http://" + url + " is satire news site\n";
            } else if (cmd.equals("fake") && news.isFake()) {
                hasil = "http://" + url + " is fake news site\n";
            } else {
                hasil = "http://" + url + " is not " + cmd + " news site\n";
            }
        }
        return hasil;
    }

    public synchronized String checkGroupUrl(String url) {
        String hasil = "";
        if (this.listUrl.containsKey(url)) {
            FakeNews news = this.listUrl.get(url);
            hasil = "Warning!!\nhttp://";
            hasil += url + " is " + news.getAllTypes() + " news site\n";
        }
        return hasil;
    }

    public synchronized String addFakeNews(String url, String type) {
        Set<String> types = new HashSet<>();
        types.add(type);
        this.addToList(url, types);
        if (saveToCsv(url, type)) {
            return "http://" + url + " added.\n";
        }
        return "Failed to add";
    }

    private synchronized void addToList(String url, Set<String> types) {
        if (this.listUrl.containsKey(url)) {
            this.listUrl.get(url).getTypes().addAll(types);
        } else {
            FakeNews fake = new FakeNews(url, types);
            this.listUrl.put(url, fake);
        }
    }

    public synchronized boolean saveToCsv(String url, String type) {
        try {
            String content = "\n" + url + "," + type + ", , , ,";
            this.fileWriter = new FileWriter(path, true);
            this.writer = new BufferedWriter(this.fileWriter);
            this.writer.write(content);
            this.writer.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }


}

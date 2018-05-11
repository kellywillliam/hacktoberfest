package advprog.example.bot.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiCaller {
    private String jsonString;

    public String checkSimilarity(String content1, String content2) {
        double temp = 0.0;
        StringBuilder builder = new StringBuilder();
        if (!isText(content1) && !isText(content2)) {
            temp = jsonGetRequest("https://api.dandelion.eu/datatxt/sim/v1/?url1=" + content1 + "&url2=" + content2 + "&token=16e5e85e020b4cef900aa5bcaaaae369");
        } else if (isText(content1) && isText(content2)) {
            temp = jsonGetRequest("https://api.dandelion.eu/datatxt/sim/v1/?text1=" + content1 + "&text2=" + content2 + "&token=16e5e85e020b4cef900aa5bcaaaae369");
        } else {
            builder.append("Kesalahan input\n");
            builder.append("untuk membandingkan text ketik : \\docs_sim 'text1' 'text2'\n");
            builder.append("untuk membandingkan url ketik : \\docs_sim 'url1' 'url2'");
            return builder.toString();
        }
        if (temp < 0) {
            return "Terjadi Error pada url yang dimasukkan";
        }
        int hsl = (int) Math.floor(temp * 100);
        return "" + hsl + "%";
    }

    public boolean isText(String input) {
        int n = input.length();
        return (n > 1 && input.charAt(0) == '\'' && input.charAt(n - 1) == '\'');
    }

    public String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }

    public Double jsonGetRequest(String urlQueryString) {
        Double hasil = 0.0;
        try {
            URL url = new URL(urlQueryString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.connect();
            InputStream inStream = connection.getInputStream();
            hasil = getSimilarity(streamToString(inStream));
        } catch (Exception ex) {
            hasil = -1.0;
        }
        return hasil;
    }

    public Double getSimilarity(String str) {
        Double hasil = -1.0;
        String temp = str.replace("{", "").replace("}", "");
        String[] arr = temp.split(",");
        for (String p : arr) {
            if (p.contains("\"similarity\"")) {
                p = p.replace("\"similarity\"", "");
                p = p.replace(":", "");
                p = p.replace(" ", "");
                p = p.replace("\t", "");
                hasil = Double.parseDouble(p);
                break;
            }
        }
        return hasil;
    }
}

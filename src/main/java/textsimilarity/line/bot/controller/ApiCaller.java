package textsimilarity.line.bot.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ApiCaller {
    private String jsonString;

    public String checkSimilarity(String content1, String content2) {
        double temp = 0.0;
        if (!content1.contains("'") && !content2.contains("'")) {
            temp = jsonGetRequest("https://api.dandelion.eu/datatxt/sim/v1/?url1=" + content1 + "&url2=" + content2 + "&token=16e5e85e020b4cef900aa5bcaaaae369");
        } else if (content1.contains("'") && content2.contains("'")) {
            content1 = content1.replace("'", "");
            content2 = content2.replace("'", "");
            temp = jsonGetRequest("https://api.dandelion.eu/datatxt/sim/v1/?text1=" + content1 + "&text2=" + content2 + "&token=16e5e85e020b4cef900aa5bcaaaae369");
        } else {
            temp = -1.0;
        }
        if (temp < 0) {
            return "Terjadi Error";
        }
        int hsl = (int) Math.floor(temp * 100);
        return "" + hsl + "%";
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

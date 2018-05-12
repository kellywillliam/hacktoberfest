package advprog.example.bot.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;
import java.util.*;
import java.net.URL;
import java.net.URLEncoder;
import javax.net.ssl.HttpsURLConnection;

public class ApiCaller {
	private String urlApi;
	
	public ApiCaller() {
		urlApi = "https://api.dandelion.eu/datatxt/sim/v1/?";
		urlApi += "token=16e5e85e020b4cef900aa5bcaaaae369&";
		urlApi += "lang=en&";
	}

    public String checkSimilarity(String content) {
        double temp = 0.0;
		if(content.contains("'")){
			List<String>words = getContent(content);
			if(words.size()==2){
				String text1 = toUrlString(words.get(0));
				String text2 = toUrlString(words.get(1));
				temp = jsonGetRequest(urlApi + "text1=" + text1 + "&text2=" + text2);
			} else {
				temp = -1.0;
			}
		} else {
			content = content.trim();
			String[]urls = content.split(" ");
			temp = jsonGetRequest(urlApi + "url1=" + urls[0] + "&url2=" + urls[1]);
        }
		return similarOut(temp);
    }
	
	public String similarOut(double nilai){
        StringBuilder builder = new StringBuilder();
        if (nilai < 0 && nilai > -2.0) {
            builder.append("Kesalahan input\n");
            builder.append("untuk membandingkan text ketik : \\docs_sim 'text1' 'text2'\n");
            builder.append("untuk membandingkan url ketik : \\docs_sim 'url1' 'url2'");
        } else if (nilai < -2.0) {
			builder.append("Error, Kesalahan jaringan  atau URL error");
		} else {
			int hsl = (int) Math.floor(nilai * 100);
			builder.append(hsl).append("%");
		}
		return builder.toString();
		
	}

    public String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }

    public Double jsonGetRequest(String urlQueryString) {
        Double hasil = 0.0;
        try {
			URL myUrl = new URL(urlQueryString);
			HttpsURLConnection connection = (HttpsURLConnection)myUrl.openConnection();
			InputStream inStream = connection.getInputStream();
            hasil = getSimilarity(streamToString(inStream));
        } catch (Exception ex) {
            hasil = -5.0;
        }
        return hasil;
    }

    public Double getSimilarity(String inStream) {
		JSONObject jsonObj = new JSONObject(inStream);
		if(jsonObj.has("similarity")){
			return jsonObj.getDouble("similarity");
		}
		return -1.0;
    }
	
	public String toUrlString(String str){
		String hasil = "";
		try{
			hasil = URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hasil;
	}
	
	public List<String> getContent(String content){
		content = content.trim();
		char[] chars = content.toCharArray();
		int size = chars.length;
		String[] str = content.split("'");
		
		List<String> words = new ArrayList<>();
		if(chars[0] == '\'' && chars[size-1] == '\'' && str.length == 4) {
			words.add(str[1]);
			words.add(str[3]);
		}
		return words;
	}
}

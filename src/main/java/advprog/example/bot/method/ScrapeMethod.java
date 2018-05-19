package advprog.example.bot.method;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.concurrent.Exchanger;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;


public class ScrapeMethod {

    public static Document getDoc(String link) {
        Document document;
        try {
            String url = link;
            document = Jsoup.connect(url).get();
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String showAnime() {
        Element element = getDoc("https://www.livechart.me/schedule/all")
                .getElementsByClass("chart compact").get(0);
        String anime = "";

        if (element == null) {
            anime = "no anime airing today";
        } else {
            Elements a = element.getElementsByTag("a");
            for (Element elem: a) {
                String title = elem.getElementsByClass("schedule-card-title").text();
                anime += title + " \n";


            }

        }
        return anime;

    }



    public static String getAnime(String anime) {
        String plainCreds = "smactavish321:mactavish321";
        String basicAuth = "Basic " + printBase64Binary(plainCreds.getBytes());
        final String endpoint_url = "https://myanimelist.net/api/anime/search.xml?q=";
        final String anime_url = anime;
        String url = endpoint_url + anime_url;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization",basicAuth);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (response.getBody() == null) {
            return "anime cannot be fouund weebo";
        } else {
            JSONObject xmlJSONObj = XML.toJSONObject(response.getBody());
            //String jsonPrettyPrintString = xmlJSONObj.toString(4);
            //return jsonPrettyPrintString;
            JSONArray entryArr = xmlJSONObj.getJSONObject("anime")
                    .getJSONArray("entry");
            JSONObject animeObj = entryArr.getJSONObject(0);
            String title = animeObj.getString("title");
            String status = animeObj.getString("status");
            String startDate = animeObj.getString("start_date");
            String endDate = animeObj.getString("end_date");
            if(status.equalsIgnoreCase("Currently Airing")){
                return title + " is airing from " + startDate + " until " + endDate;
            }else
            if(status.equalsIgnoreCase("Not yet aired")){
                return title + " will air starting at " + startDate;
            }else
                return title + " has finished airing at " + endDate;
        }

    }

}

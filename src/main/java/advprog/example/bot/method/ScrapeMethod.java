package advprog.example.bot.method;

import static javax.xml.bind.DatatypeConverter.printBase64Binary;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;




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


    public static String showAnime(String link) {
        try {
            Element element = getDoc(link)
                    .getElementsByClass("chart compact").get(0);
            String anime = "";
            ArrayList<String> episodes = new ArrayList<>();
            ArrayList<String> titles = new ArrayList<>();
            if (element == null) {
                anime = "no anime airing today";
            } else {
                Elements a = element.getElementsByTag("a");
                for (Element elem: a) {
                    String getTitle = elem.getElementsByClass("schedule-card-title").text();
                    titles.add(getTitle);

                }
                Elements eps = element.getElementsByClass("schedule-card-countdown");
                for (Element elem: eps) {
                    episodes.add(elem.text().substring(0,3).replace("EP",""));
                }

                for (int i = 0; i < episodes.size();i++) {
                    anime += titles.get(i) + " " + episodes.get(i) + " \n";
                }


            }
            return anime;
        } catch (Exception e) {
            e.printStackTrace();
            return "404 not found";
        }

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

        ResponseEntity<String> response = restTemplate
                .exchange(url, HttpMethod.GET, entity, String.class);
        if (response.getBody() == null) {
            return "anime cannot be fouund weebo";
        } else {
            JSONObject xmlJsonObj = XML.toJSONObject(response.getBody());
            try {
                JSONArray entryArr = xmlJsonObj.getJSONObject("anime")
                        .getJSONArray("entry");
                JSONObject animeObj = entryArr.getJSONObject(0);
                String title = animeObj.getString("title");
                String status = animeObj.getString("status");
                String startDate = animeObj.getString("start_date");
                String endDate = animeObj.getString("end_date");
                if (status.equalsIgnoreCase("Currently Airing")) {
                    return title + " is airing from " + startDate + " until " + endDate;
                } else if (status.equalsIgnoreCase("Not yet aired")) {
                    return title + " will air starting at " + startDate;
                } else {
                    return title + " has finished airing at " + endDate;
                }
            } catch (JSONException ex) {
                JSONObject animeObj = xmlJsonObj.getJSONObject("anime").getJSONObject("entry");
                String title = animeObj.getString("title");
                String status = animeObj.getString("status");
                String startDate = animeObj.getString("start_date");
                String endDate = animeObj.getString("end_date");
                if (status.equalsIgnoreCase("Currently Airing")) {
                    return title + " is airing from " + startDate + " until " + endDate;
                } else if (status.equalsIgnoreCase("Not yet aired")) {
                    if (startDate.equalsIgnoreCase("0000-00-00")) {
                        return title + " will air soon";
                    } else {
                        return title + " will air starting at " + startDate;
                    }

                } else {
                    return title + " has finished airing at " + endDate;
                }
            }

        }

    }

}

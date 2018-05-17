package advprog.example.bot.bing.news;

import com.linecorp.bot.model.message.TextMessage;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class BingNews {
    private static final String BING_NEWS_URL =
            "https://api.cognitive.microsoft.com/bing/v7.0/news/search?q=%s&count=5&mkt=en-US";
    private static final String API_KEY = "34ac18f889344404a2d4f2560b11d51b";

    public static TextMessage getFiveNews(String input) {
        return null;
    }

    public static String fetchNews(String query) throws HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        StringBuilder replyString = new StringBuilder();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    String.format(BING_NEWS_URL, query),
                    HttpMethod.GET,
                    entity,
                    String.class
            );

            JSONObject jsonObject = new JSONObject(response.getBody());
            int index = 1;
            for (Object object : jsonObject.getJSONArray("value")) {
                replyString.append(String.format("News %d\n", index++));
                replyString.append(
                        String.format("Title: \"%s\"\n",
                                ((JSONObject) object).getString("name"))
                );
                replyString.append(
                        String.format("Description: %s\n",
                                ((JSONObject) object).getString("description"))
                );
                replyString.append(
                        String.format("URL: %s\n\n",
                                ((JSONObject) object).getString("url"))
                );
            }
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        return replyString.toString();
    }
}

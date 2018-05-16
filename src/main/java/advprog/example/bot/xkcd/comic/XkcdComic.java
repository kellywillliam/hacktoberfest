package advprog.example.bot.xkcd.comic;

import com.linecorp.bot.model.message.Message;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class XkcdComic {
    private static final String XKCD_JSON_URL = "https://xkcd.com/%d/info.0.json";

    public static Message getComic(String input) {
        return null;
    }

    private static String fetchComicImageUrl(int id) throws HttpClientErrorException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                String.format(XKCD_JSON_URL, id),
                HttpMethod.GET,
                null,
                String.class);
        try {
            JSONObject jsonObject = new JSONObject(responseEntity.getBody());
            return jsonObject.getString("img");
        } catch (Exception e) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
    }
}

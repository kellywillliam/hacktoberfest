package advprog.example.bot.xkcd.comic;

import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

public class XkcdComic {
    private static final String XKCD_JSON_URL = "https://xkcd.com/%d/info.0.json";
    private static final String INVALID_MESSAGE = "Comic ID is invalid :(";

    public static Message getComic(String input) {
        try {
            int id = Integer.parseInt(input);
            String comicUrl = fetchComicImageUrl(id);
            return new ImageMessage(comicUrl, comicUrl);
        } catch (Exception e) {
            return new TextMessage(INVALID_MESSAGE);
        }
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

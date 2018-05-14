package advprog.example.bot.fake.json;

import com.linecorp.bot.model.message.TextMessage;
import java.util.Random;
import org.springframework.web.client.RestTemplate;


public class FakeJson {
    private static final String FAKE_JSON_URL = "https://jsonplaceholder.typicode.com/posts/";

    public static TextMessage getFakeJson() {
        RestTemplate restTemplate = new RestTemplate();
        Random rand = new Random();
        String jsonString = restTemplate.getForObject(FAKE_JSON_URL
                + (rand.nextInt(99) + 1), String.class);
        return new TextMessage(jsonString);
    }

}

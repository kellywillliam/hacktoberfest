package advprog.example.bot.sentiment.analysis;

import com.linecorp.bot.model.message.TextMessage;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class SentimentAnalysis {
    private final static String AZURE_API_URL = "";

    public static TextMessage analyzeText(String text) {
        String requestJson = String.format("{\"documents\":[{\n"
                + "      \"language\": \"en\",\n"
                + "      \"id\": \"1\",\n"
                + "      \"text\": \"%s\"\n"
                + "    }]}",text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson, headers);
        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.postForObject(AZURE_API_URL, entity, String.class);
        JSONObject jsonObject = new JSONObject(response);
        String result = jsonObject
                .getJSONArray("documents")
                .getJSONObject(0)
                .getString("score");

        return new TextMessage(result);
    }
}

package advprog.example.bot.sentiment.analysis;

import com.linecorp.bot.model.message.TextMessage;

import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SentimentAnalysis {
    private static final String AZURE_API_URL =
            "https://southeastasia.api.cognitive.microsoft.com/text/analytics/v2.0/sentiment";
    private static final String API_KEY =
            "28cf1f357a4c4af88f7e47dcee5dc7bc";
    private static final String TEMPLATE =
            "{\"documents\":[{\n"
                    + "      \"language\": \"en\",\n"
                    + "      \"id\": \"1\",\n"
                    + "      \"text\": \"%s\"\n"
                    + "    }]}";
    private static final String GOOD_TEMPLATE = "The sentiment is good :), score: %.2g";
    private static final String BAD_TEMPLATE = "The sentiment is bad :(, score: %.2g";


    public static TextMessage analyzeText(String text) {
        Double score = getScore(text);
        if (score < 0.5) {
            return new TextMessage(String.format(BAD_TEMPLATE, score));
        } else {
            return new TextMessage(String.format(GOOD_TEMPLATE, score));
        }
    }

    private static Double getScore(String text) {
        String requestJson = String.format(TEMPLATE, text);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Ocp-Apim-Subscription-Key", API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        ResponseEntity<String> response = postForSentiment(entity);
        try {
            JSONObject jsonObject = new JSONObject(response.getBody());

            return jsonObject
                    .getJSONArray("documents")
                    .getJSONObject(0)
                    .getDouble("score");

        } catch (Exception e) {
            return -1.0;
        }
    }

    private static ResponseEntity<String> postForSentiment(HttpEntity<String> entity) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate
                .exchange(AZURE_API_URL, HttpMethod.POST, entity, String.class);
    }
}

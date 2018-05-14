package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;


@LineMessageHandler
public class ImageTagController {

    private static final Logger LOGGER = Logger.getLogger(ImageTagController.class.getName());

    @EventMapping
    public TextMessage handleImageMessageEvent(MessageEvent<ImageMessageContent> event)
            throws JSONException, IOException {
        LOGGER.fine(String.format("ImageMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        ImageMessageContent content = event.getMessage();
        // Get content id of image sent by user
        String contentId = content.getId();
        // Write image file to src/main/resources
        getContentFromLine(contentId);
        // Read image file and upload to imagga
        String uploadImageResponse = uploadContentToImagga();
        // Parse response body from API call to upload image to imagga
        JSONObject uploadImageJsonObject = new JSONObject(uploadImageResponse);
        JSONArray uploadedArray = uploadImageJsonObject.getJSONArray("uploaded");
        String id = uploadedArray.getJSONObject(0).getString("id");
        // Pass id to be processed to API call to get image tags from imagga
        String imageTagsresponse = getImageTags(id);
        JSONObject imageTagsJsonResponse = new JSONObject(imageTagsresponse);
        JSONArray resultsArray = imageTagsJsonResponse.getJSONArray("results");
        JSONArray tagsArray = resultsArray.getJSONObject(0).getJSONArray("tags");
        String result = "Here's the related tags of your image: \n";
        for (int i = 0; i < 5; i++) {
            String tag = tagsArray.getJSONObject(i).getString("tag");
            Double confidence = tagsArray.getJSONObject(i).getDouble("confidence");
            result += "Tag: " + tag + " Confidence: " + Double.toString(confidence) + "\n";
        }
        return new TextMessage(result);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public void getContentFromLine(String contentId) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
                "Bearer JDXrmd0FpLk0e6v16czZQq19k9+19NRP7+"
                        + "L364LorekLQUS+FfUd708u30hGXHHLVQQ4hzv3o1g1EJ4sEAhghU"
                        + "47bviQfwAD+0tWt3v51QN+bsyZBgnfKaHHYK6C2cMYK3NA4OjuNvff"
                        + "NSk+bw/oj49PbdgDzCFqoOLOYbqAITQ=");
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        ResponseEntity<byte[]> result = restTemplate.exchange("https://api.line.me/v2/bot/message/{id}/content", HttpMethod.GET, entity, byte[].class, contentId);
        Files.write(Paths.get("src/main/resources/image.jpg"), result.getBody());
    }

    public String uploadContentToImagga() throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
                "Basic YWNjXzA4Y2VmYjEzYjBjYjljMDplNDNjY2Y3MjU"
                        + "0Y2U0OGNkYjZiY2NhMDliODQ4ZjRkOA==");
        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ClassPathResource("image.jpg"));
        HttpEntity<?> entity = new HttpEntity<Object>(body, headers);
        ResponseEntity<String> result = restTemplate.exchange("https://api.imagga.com/v1/content", HttpMethod.POST, entity, String.class);
        return result.getBody();
    }

    public String getImageTags(String contentId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
                "Basic YWNjXzA4Y2VmYjEzYjBjYjljMDplNDNj"
                        + "Y2Y3MjU0Y2U0OGNkYjZiY2NhMDliODQ4ZjRkOA==");
        HttpEntity<?> entity = new HttpEntity<Object>("parameters", headers);
        ResponseEntity<String> result = restTemplate.exchange("https://api.imagga.com/v1/tagging?content={contentId}", HttpMethod.GET, entity, String.class, contentId);
        return result.getBody();
    }
}

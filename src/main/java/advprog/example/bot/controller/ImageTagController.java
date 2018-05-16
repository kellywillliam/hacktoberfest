package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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
    static boolean isTags = false;
    static boolean isFileWritten = false;
    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private String fileName = "";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        if (contentText.equalsIgnoreCase("/tags")) {
            isTags = true;
            return new TextMessage("Now upload your image");
        }
        return new TextMessage("not available");
    }

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
        if (isTags) {
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
            String result = "Here's the related tags of your image: \n\n";
            for (int i = 0; i < 5; i++) {
                String tag = tagsArray.getJSONObject(i).getString("tag");
                Double confidence = tagsArray.getJSONObject(i).getDouble("confidence");
                result += "Tag: " + tag + "\nConfidence: " + Double.toString(confidence) + "\n\n";
            }
            isTags = false;
            // delete file
            File fileToBeDeleted = new File("src/main/resources/image.jpg");
            fileToBeDeleted.delete();
            return new TextMessage(result);
        }
        return new TextMessage("succesfully uploaded your image");
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
        fileName = randomAlphaNumeric(10) + ".jpg";
        FileOutputStream fos = new FileOutputStream(new File("src/main/resources/image.jpg"));
        fos.write(result.getBody());
        fos.flush();
        fos.close();
    }

    public String uploadContentToImagga() throws IOException {
        File imageFile = new File("src/main/resources/image.jpg");
        if (imageFile.exists()) {
            String credentialsToEncode =
                    "acc_08cefb13b0cb9c0" + ":" + "e43ccf7254ce48cdb6bcca09b848f4d8";
            String basicAuth = Base64.getEncoder().encodeToString(
                    credentialsToEncode.getBytes(StandardCharsets.UTF_8));
            URL urlObject = new URL("https://api.imagga.com/v1/content");
            HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
            connection.setRequestProperty("Authorization", "Basic " + basicAuth);

            String boundaryString = "-Image Upload-";
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.addRequestProperty(
                    "Content-Type", "multipart/form-data; boundary=" + boundaryString);
            OutputStream outputStreamToRequestBody = connection.getOutputStream();
            BufferedWriter httpRequestBodyWriter =
                    new BufferedWriter(new OutputStreamWriter(outputStreamToRequestBody));

            httpRequestBodyWriter.write("\n--" + boundaryString + "\n");
            String filepath = "src/main/resources/image.jpg";
            File fileToUpload = new File(filepath);
            httpRequestBodyWriter.write("Content-Disposition: form-data;"
                    + "name=\"myFile\";"
                    + "filename=\"" + fileToUpload.getName() + "\""
                    + "\nContent-Type: text/plain\n\n");
            httpRequestBodyWriter.flush();

            FileInputStream inputStreamToLogFile = new FileInputStream(fileToUpload);

            int bytesRead;
            byte[] dataBuffer = new byte[1024];
            while ((bytesRead = inputStreamToLogFile.read(dataBuffer)) != -1) {
                outputStreamToRequestBody.write(dataBuffer, 0, bytesRead);
            }

            outputStreamToRequestBody.flush();

            httpRequestBodyWriter.write("\n--" + boundaryString + "--\n");
            httpRequestBodyWriter.flush();

            outputStreamToRequestBody.close();
            httpRequestBodyWriter.close();

            BufferedReader connectionInput = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            String jsonResponse = connectionInput.readLine();

            connectionInput.close();

            return jsonResponse;
            //RestTemplate restTemplate = new RestTemplate();
            //HttpHeaders headers = new HttpHeaders();
            //headers.add("Authorization",
            //        "Basic YWNjXzA4Y2VmYjEzYjBjYjljMDplNDNjY2Y3MjU"
            //                + "0Y2U0OGNkYjZiY2NhMDliODQ4ZjRkOA==");
            //LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            //body.add("image", imageFile.getCanonicalPath());
            //body.add("image", new ClassPathResource("image.jpg"));
            //HttpEntity<?> entity = new HttpEntity<Object>(body, headers);
            //ResponseEntity<String> result = restTemplate.exchange("https://api.imagga.com/v1/content", HttpMethod.POST, entity, String.class);
            //return result.getBody();
        }
        return "file does not exists";
    }

    public String getImageTags(String contentId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
                "Basic YWNjXzA4Y2VmYjEzYjBjYjljMDplNDNjY2Y3"
                        + "MjU0Y2U0OGNkYjZiY2NhMDliODQ4ZjRkOA==");
        HttpEntity<?> entity = new HttpEntity<Object>("parameters", headers);
        ResponseEntity<String> result = restTemplate.exchange("https://api.imagga.com/v1/tagging?content={contentId}", HttpMethod.GET, entity, String.class, contentId);
        return result.getBody();
    }
}

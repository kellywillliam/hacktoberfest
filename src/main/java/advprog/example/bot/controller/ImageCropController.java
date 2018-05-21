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
public class ImageCropController {

    private static final Logger LOGGER = Logger.getLogger(ImageCropController.class.getName());
    static boolean isCrop = false;

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        if (contentText.equalsIgnoreCase("/crop")) {
            isCrop = true;
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
        if (isCrop) {
            // Read image file and upload to imagga
            String uploadImageResponse = uploadContentToImgur();
            // Parse response body from API call to upload image to imgur
            JSONObject uploadImageJsonObject = new JSONObject(uploadImageResponse);
            JSONObject cropData = uploadImageJsonObject.getJSONObject("data");
            String imageUrl = cropData.getString("link");
            // Pass image url to be processed to API call to get image tags from imagga
            String imageCropresponse = getImageCrop(imageUrl);
            JSONObject imageCropJsonResponse = new JSONObject(imageCropresponse);
            JSONArray resultsArray = imageCropJsonResponse.getJSONArray("results");
            JSONArray cropArray = resultsArray.getJSONObject(0).getJSONArray("croppings");
            Double coordinateX1 = cropArray.getJSONObject(0).getDouble("x1");
            Double coordinateX2 = cropArray.getJSONObject(0).getDouble("x2");
            Double coordinateY1 = cropArray.getJSONObject(0).getDouble("y1");
            Double coordinateY2 = cropArray.getJSONObject(0).getDouble("y2");
            String result = "Here's where you should crop your image: \n\n"
                    + "Start Coordinate: x1= " + coordinateX1 + " y1= " + coordinateY1 + "\n"
                    + "End Coordinate: x2= " + coordinateX2 + " y2= " + coordinateY2 + "\n";
            isCrop = false;
            // delete file
//            File fileToBeDeleted = new File("src/main/resources/imagecrop.jpg");
//            fileToBeDeleted.delete();
//            return new TextMessage(result);
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
        FileOutputStream fos =
                new FileOutputStream(new File("src/main/resources/imagecrop.jpg"));
        fos.write(result.getBody());
        fos.flush();
        fos.close();
    }

    public String uploadContentToImgur() throws IOException {
        File imageFile = new File("src/main/resources/imagecrop.jpg");
        String basicAuth = "a1966a7fc22c5bc";
        URL urlObject = new URL("https://api.imgur.com/3/image");
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestProperty("Authorization", "Client-ID " + basicAuth);

        String boundaryString = "--WebKitFormBoundary7MA4YWxkTrZu0gW";
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.addRequestProperty(
                "Content-Type", "multipart/form-data; boundary=" + boundaryString);
        OutputStream outputStreamToRequestBody = connection.getOutputStream();
        BufferedWriter httpRequestBodyWriter =
                new BufferedWriter(new OutputStreamWriter(outputStreamToRequestBody));

        httpRequestBodyWriter.write("\n--" + boundaryString + "\n");
        String filepath = "src/main/resources/imagecrop.jpg";
        File fileToUpload = new File(filepath);
        httpRequestBodyWriter.write("Content-Disposition: form-data;"
                + "name=\"image\";"
                + "filename=\"" + fileToUpload.getName() + "\""
                + "\nContent-Type: image/jpeg\n\n");
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
    }

    public String getImageCrop(String imageUrl) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization",
                "Basic YWNjXzA4Y2VmYjEzYjBjYjljMDplNDNjY2Y3"
                        + "MjU0Y2U0OGNkYjZiY2NhMDliODQ4ZjRkOA==");
        HttpEntity<?> entity = new HttpEntity<Object>("parameters", headers);
        ResponseEntity<String> result = restTemplate.exchange("https://api.imagga.com/v1/croppings?url={imageUrl}", HttpMethod.GET, entity, String.class, imageUrl);
        return result.getBody();
    }
}

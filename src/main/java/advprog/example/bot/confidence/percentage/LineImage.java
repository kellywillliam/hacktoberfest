package advprog.example.bot.confidence.percentage;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class LineImage {

    public static String getImage(String id) throws Exception {
        String url = "https://api.line.me/v2/bot/message/"+ id + "/content";
        RestTemplate restTemplate = new RestTemplate();
        byte[] imageBytes = restTemplate.getForObject(url, byte[].class);
        Files.write(Paths.get("src/main/resources/image.jpg"), imageBytes);
        String idImage = uploadImage("src/main/resources/image.jpg");
        return idImage;
    }

    public static String uploadImage(String path) throws Exception{
        /* Api calling */

        String credentialsToEncode = "acc_7131bd91f718dd6" + ":" + "0438f48b7ba34d253d4df8f7e52485af";
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        URL urlObject = new URL("https://api.imagga.com/v1/content");
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();
        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        String boundaryString = "-Image Upload-";
        String filepath = path;
        File fileToUpload = new File(filepath);

        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.addRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);

        OutputStream outputStreamToRequestBody = connection.getOutputStream();
        BufferedWriter httpRequestBodyWriter =
                new BufferedWriter(new OutputStreamWriter(outputStreamToRequestBody));

        httpRequestBodyWriter.write("\n--" + boundaryString + "\n");
        httpRequestBodyWriter.write("Content-Disposition: form-data;"
                + "name=\"myFile\";"
                + "filename=\""+ fileToUpload.getName() +"\""
                + "\nContent-Type: text/plain\n\n");
        httpRequestBodyWriter.flush();

        FileInputStream inputStreamToLogFile = new FileInputStream(fileToUpload);

        int bytesRead;
        byte[] dataBuffer = new byte[1024];
        while((bytesRead = inputStreamToLogFile.read(dataBuffer)) != -1) {
            outputStreamToRequestBody.write(dataBuffer, 0, bytesRead);
        }

        outputStreamToRequestBody.flush();

        httpRequestBodyWriter.write("\n--" + boundaryString + "--\n");
        httpRequestBodyWriter.flush();

        outputStreamToRequestBody.close();
        httpRequestBodyWriter.close();

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

        connectionInput.close();

        //System.out.println(jsonResponse);
        JSONObject bigObj = new JSONObject(jsonResponse);
        System.out.println(jsonResponse);
        JSONArray parentArr = bigObj.getJSONArray("uploaded");
        JSONObject smallObj = parentArr.getJSONObject(0);
        return smallObj.getString("id");
    }
}

package advprog.example.bot.confidence.percentage;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;

import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class ConfidencePercentage {

    public static String getConfidencePercentage(String img) throws java.io.IOException {

        //api calling
        String credentialsToEncode = "acc_7131bd91f718dd6" + ":"
                + "0438f48b7ba34d253d4df8f7e52485af";
        String basicAuth = Base64.getEncoder()
                .encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        final String endpoint_url = "https://api.imagga.com/v1/categorizations/nsfw_beta";
        final String image_url = img;

        String url = endpoint_url + "?url=" + image_url;
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        int responseCode = connection.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader connectionInput =
                new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

        connectionInput.close();

        JSONObject bigObj = new JSONObject(jsonResponse);
        System.out.println(jsonResponse);
        JSONArray parentArr = bigObj.getJSONArray("results");
        if (parentArr.length() == 0) {
            return "Error terjadi";
        } else {
            JSONObject smallObj = parentArr.getJSONObject(0);
            return isSfw(smallObj);
        }

    }

    public static String getFromUserImage(String id){
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.imagga.com/v1/categorizations/nsfw_beta?content=" + id;
        String credentialsToEncode = "acc_7131bd91f718dd6" + ":"
                + "0438f48b7ba34d253d4df8f7e52485af";
        String basicAuth = Base64.getEncoder()
                .encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + basicAuth);
        HttpEntity<String> request = new HttpEntity<String>(headers);

        String jsonResponse = restTemplate.exchange(url, HttpMethod.GET, request, String.class).getBody();
        return jsonResponse;
    }

    public static String isSfw(JSONObject smallObj) {
        double safe = 0;
        double nsfw = 0;
        JSONArray categories = smallObj.getJSONArray("categories");
        for (int i = 0; i < categories.length(); i++) {
            String nama = categories.getJSONObject(i).getString("name");
            double confidence = categories.getJSONObject(i)
                    .getDouble("confidence");
            if (nama.equalsIgnoreCase("nsfw")) {
                nsfw = confidence;
            }
            if (nama.equalsIgnoreCase("safe")) {
                safe = confidence;
            }
        }
        if (nsfw > safe) {
            return "NSFW";
        } else {
            return "SFW";
        }

    }
}

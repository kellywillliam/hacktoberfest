package advprog.example.bot.confidence.percentage;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ConfidencePercentage {

    public static String getConfidencePercentage(String img) throws java.io.IOException{

        //api calling
        String credentialsToEncode = "acc_7131bd91f718dd6" + ":" + "0438f48b7ba34d253d4df8f7e52485af";
        String basicAuth = Base64.getEncoder().encodeToString(credentialsToEncode.getBytes(StandardCharsets.UTF_8));

        String endpoint_url = "https://api.imagga.com/v1/categorizations/nsfw_beta";
        String image_url = img;

        String url = endpoint_url + "?url=" + image_url;
        URL urlObject = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObject.openConnection();

        connection.setRequestProperty("Authorization", "Basic " + basicAuth);

        int responseCode = connection.getResponseCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader connectionInput = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String jsonResponse = connectionInput.readLine();

        connectionInput.close();

        double safe = 0;
        double nsfw = 0;

        JSONObject bigObj = new JSONObject(jsonResponse);
        System.out.println(jsonResponse);
        JSONArray parentArr = bigObj.getJSONArray("results");
        if (parentArr.length() == 0) {
            return "ini bukan image oi";
        }else{
            JSONObject smallObj = parentArr.getJSONObject(0);
            return isSfw(smallObj);

        }





/* After receiving the JSON response you'll need to parse it in order to get the values you need.
You can do this by using a JSON library/package of your choice.
You can find a list of such tools at json.org */

    }
    public static String isSfw(JSONObject smallObj){
        double safe = 0;
        double nsfw = 0;
        JSONArray categories = smallObj.getJSONArray("categories");
        for( int i = 0; i < categories.length(); i++) {
            String nama = categories.getJSONObject(i).getString("name");
            double confidence = categories.getJSONObject(i).getDouble("confidence");
            if(nama.equalsIgnoreCase("nsfw")){
                nsfw = confidence;
            }
            if(nama.equalsIgnoreCase("safe")){
                safe = confidence;
            }
        }
        if(nsfw > safe){
            return "NSFW";
        }else
            return "SFW";
    }
}

package advprog.example.bot.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;

public class DetectLanguage {

    static String token = "fe9bdeee32704534b05f2161ad3288de";

    public static String detectLang(String text)  {
        String res = "";
        String typeText = text.contains("www") ? "url" : "text";
        HttpResponse<JsonNode> jsonResponse = null;
        try {
            jsonResponse = Unirest.get("https://api.dandelion.eu/datatxt/li/v1")
                    .queryString(typeText,text)
                    .queryString("token",token)
                    .asJson();
            JSONArray jsonArray = jsonResponse.getBody().getObject().getJSONArray("detectedLangs");
            String languange = jsonArray.getJSONObject(0).getString("lang");
            String confidenceLevel = String.valueOf(jsonArray.getJSONObject(0)
                    .getDouble("confidence"));
            res += languange + " " + confidenceLevel;
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return "Some errors've occured, make sure your text is a valid text!";
        }
    }
}

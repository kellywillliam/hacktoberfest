package advprog.example.bot.controller;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

public class ApiEnterKomputer {

    private String baseUriApiEnterkomputer = "https://enterkomputer.com/api/product/";

    public String getDetailItemByCategoryAndName(String category, String name) {
        String url = baseUriApiEnterkomputer + category.toLowerCase() + ".json";
        try {
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .asJson();
            StringBuilder result = new StringBuilder();
            int[] counter = {1};
            response.getBody().getArray().forEach(item -> {
                JSONObject itemJson = ((JSONObject)item);
                if (itemJson.getString("name")
                        .toLowerCase()
                        .contains(name.toLowerCase())) {
                    String itemName = itemJson.getString("name");
                    String itemPrice = itemJson.getString("price");
                    result.append(String
                            .format("(%d) %s - %s", counter[0], itemName,itemPrice));
                    result.append("\n");
                    counter[0]++;
                }
            });
            if (result.toString().equals("")) {
                return "No items found!";
            } else {
                return result.toString().substring(0,result.length() - 1);
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return "No such category!";
    }
}

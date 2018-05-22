package advprog.example.bot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.postback.PostbackContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.ButtonsTemplate;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@LineMessageHandler
public class UberController {
    @Autowired
    private LineMessagingClient lineMessagingClient;

    private static final Logger LOGGER = Logger.getLogger(UberController.class.getName());
    private static final File FILE = new File("src/main/resources/data.json");
    private static String destination;
    private static double start_latitude;
    private static double start_longitude;
    private static String end_latitude;
    private static String end_longitude;
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static LinkedHashMap<String, Object> addedLocation = new LinkedHashMap<>();
    private static LinkedHashMap<String, LinkedHashMap<String, Object>> 
        locations = new LinkedHashMap<>();
    private static LinkedHashMap<String, LinkedHashMap<String, LinkedHashMap<String, Object>>> 
        users = new LinkedHashMap<>();
    private static int pointer = -1;
    private static int pbPointer = -1;

    static {
        initiateData();
    }
    
    @SuppressWarnings("unchecked")
    private static void initiateData() {
        try {
            users = objectMapper.readValue(FILE, LinkedHashMap.class);
        } catch (Exception e) {
            System.out.println("error");
        }
    }
    
    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();

        handleTextContent(content, event.getReplyToken(), event.getSource().getUserId());
    }
     
    @EventMapping
    @SuppressWarnings("unchecked")
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LocationMessageContent locationMessage = event.getMessage();
        String userId = event.getSource().getUserId();
        String replyToken = event.getReplyToken();
        if (pointer == 0) {
            start_latitude = locationMessage.getLatitude();
            start_longitude = locationMessage.getLongitude();
            chooseDestination(replyToken, userId);
        } else if (pointer == 1) {
            addedLocation = objectMapper.convertValue(locationMessage, LinkedHashMap.class);
            pointer = 2;
            replyText(replyToken, "What is the name of the location?");
        } else {
            replyText(replyToken, "Incorrect input");
        }
        
    }
    
    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) throws Exception {
        PostbackContent postbackContent = event.getPostbackContent();
        String replyToken = event.getReplyToken();
        String userId = event.getSource().getUserId();
        try {
            String[] data = postbackContent.getData().split("@");
            destination = data[0];
            end_latitude = data[2];
            end_longitude = data[3];

            if (pbPointer == 0) {
                estimateRide(replyToken);
            } else if (pbPointer == 1) {
                locations = users.get(userId);
                locations.remove(destination);
                users.put(userId, locations);
                objectMapper.writeValue(FILE, users);
                replyText(replyToken, "Successfully removed destination: " + destination);
                pointer = -1;
                pbPointer = -1;
            } else if (pbPointer == -1) {
                replyText(replyToken, "Please input a command first");
            }
        } catch (NullPointerException e) {
            replyText(replyToken, "An error occured when reading the location");
            pbPointer = -1;
            pointer = -1;
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    private void handleTextContent(TextMessageContent content, String replyToken, String userId) throws Exception{
        String cmd = content.getText();
        if (cmd.equalsIgnoreCase("/uber") && pointer == -1) {
            pointer = 0;
            String imageUrl = createUri("static/buttons/location.jpg");
            ButtonsTemplate buttonsTemplate = new ButtonsTemplate(
                    imageUrl,
                    "Choose Your Starting Point",
                    "Tap to set your location.",
                    Arrays.asList(
                            new URIAction("Share Location",
                                    "line://nv/location")
                            )
                    );
            TemplateMessage templateMessage = new TemplateMessage(
                    "Share Location", buttonsTemplate
                    );
            reply(replyToken, templateMessage);
        } else if (cmd.equalsIgnoreCase("/add_destination") && pointer == -1) {
            pointer = 1;
            if (!users.containsKey(userId)) {
                users.put(userId, new LinkedHashMap<>());
            }

            String imageUrl = createUri("/static/buttons/location.jpg");
            ButtonsTemplate buttonTemplate = new ButtonsTemplate(
                    imageUrl, "Add a New Destination", "Tap to set location of the new destination",
                    Arrays.asList(
                        new URIAction("Add Destination", "line://nv/location")));
    		
            TemplateMessage templateMessage = new TemplateMessage("Add Destination", buttonTemplate);
            reply(replyToken, templateMessage);
        } else if (cmd.equalsIgnoreCase("/remove_destination") && pointer == -1) {
            if (!users.containsKey(userId)) {
                replyText(replyToken, "There are no destinations added yet");
            }
            locations = users.get(userId);
            if (locations.size() == 0) {
                replyText(replyToken, "There are no destinations added yet");
            } else {
                pointer = 3;
                pbPointer = 1;
                CarouselTemplate carouselTemplate = new CarouselTemplate(
                        Arrays.asList(createCarouselItemList()));
                TemplateMessage templateMessage = new TemplateMessage(
                        "Remove Destination", carouselTemplate);
                reply(replyToken, templateMessage);
            }
        } else if (pointer == 2) {
            String locationName = content.getText();
            locations = users.get(userId);
            if (locations.containsKey(locationName)) {
                replyText(replyToken, 
                        "There is already a destination by that name, please try again");
            } else {
                locations.put(locationName, addedLocation);
                replyText(replyToken,
                        "Successfully added destination");
                users.put(userId, locations);
                objectMapper.writeValue(FILE, users);
            }
            pointer = -1;
        } else if (pointer == -1) {
            replyText(replyToken, 
                    "The commands are\n"
                    + "/uber - To Estimate Price\n"
                    + "/add_destination - To add a new Destination\n"
                    + "/remove_destination - To remove an already added destination");
        }
    }

    private void chooseDestination(String replyToken, String userId) {
        if (!users.containsKey(userId)) {
            replyText(replyToken, "There are no added destination");
            pointer = -1;
            return;
        }
        locations = users.get(userId);
        if (locations.size() == 0) {
            replyText(replyToken, "There are no added destination");
            pointer = -1;
            return;
        }
        pbPointer = 0;
        CarouselTemplate carouselTemplate = new CarouselTemplate(
                Arrays.asList(
    			        createCarouselItemList()
                ));
        TemplateMessage templateMessage = new TemplateMessage(
                "Choose Destination", carouselTemplate
                );
        this.reply(replyToken, templateMessage);
    }

    private CarouselColumn[] createCarouselItemList() {
        String imageUrl = createUri("/static/buttons/location.jpg");
        CarouselColumn[] itemList = new CarouselColumn[locations.size()];
        int idx = 0;

        for (String key : locations.keySet()) {
            LinkedHashMap<String, Object> location = locations.get(key);
            String data = key + "@" + location.get("address") + "@"
                    + location.get("latitude") + "@" + location.get("location");
            PostbackAction pba = new PostbackAction("Choose", data, key);
            String address = "" + location.get("address");
            PostbackAction[] pbaList = new PostbackAction[1];
            pbaList[0] = pba;
            itemList[idx++] = new CarouselColumn(imageUrl, key, address, 
                    Arrays.asList(pbaList));
        }
        return itemList;
    }

    private void estimateRide(String replyToken) throws Exception {
        pbPointer = -1;
        pointer = -1;	
        URL url = new URL("https://api.uber.com/v1.2/estimates/price?start_latitude="
                + start_latitude + "&start_longitude=" + start_longitude + "&end_latitude="
                + end_latitude + "&end_longitude=" + end_longitude);
        String serverToken = System.getenv("SERVER_TOKEN");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Authorization", "Token " + serverToken);

        int httpResult = connection.getResponseCode();
        String output;

        if (httpResult == HttpURLConnection.HTTP_OK) {
            JsonArray jsonArray = readJsonFromConnection(connection);
            output = createMessage(jsonArray);
        }
        else {
            output = "There is a problem while processing your request, please try again.";
        }
        replyText(replyToken, output);
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
    
    public static JsonArray readJsonFromConnection(HttpURLConnection connection) throws Exception {
        BufferedReader rd = 
                new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
        String jsonText = readAll(rd);
        JsonObject jsonObject = new JsonParser().parse(jsonText.toString()).getAsJsonObject();
        JsonArray jsonArray = jsonObject.get("prices").getAsJsonArray();
        return jsonArray;   
    }

    private String createMessage(JsonArray jsonArray) throws Exception {
        if (jsonArray.size() == 0) {
            String output = "This service is unavailable for your location";
            return output;
        }
        else {
            double distance = Double.parseDouble(
                    jsonArray.get(0).getAsJsonObject().get("distance").toString());
            StringBuilder message = new StringBuilder(
                    String.format("Destination: %s (%.2f kilometers from current position)\n\n"
                    + "Estimated travel time and fares for each Uber services:\n\n", 
                    destination, (distance * 1.60934)));

            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                String name = jsonObject.get("display_name").getAsString();
                int duration = Integer.parseInt(jsonObject.get("duration").getAsString())/60;
                int highEstimate = Integer.parseInt(jsonObject.get("high_estimate").getAsString()) * 14084;
                int lowEstimate = Integer.parseInt(jsonObject.get("low_estimate").getAsString()) * 14084;
                String price;
                if (highEstimate == lowEstimate) {
                    price = "" + highEstimate;
                }
                else {
                    price = "" + lowEstimate + "-" + highEstimate;
                }
                String info = String.format("- %s (%s minutes, %s rupiah)\n",
                        name, duration, price).replaceAll("\"", " ");
                message.append(info);
            }
            return message.toString();
        }
    }

    private static String createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                                          .path(path).build()
                                          .toUriString();
    }

    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(@NonNull String replyToken, @NonNull String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "";
        }
        this.reply(replyToken, new TextMessage(message));
    }
}

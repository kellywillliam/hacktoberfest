package advprog.example.bot.controller;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import advprog.example.bot.countryhot.Hospital;
import advprog.example.bot.countryhot.HotCountrySong;
import advprog.example.bot.countryhot.HotNewAgeSong;
import advprog.example.bot.countryhot.SongInfo;
import advprog.example.bot.countryhot.TopSong;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.*;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.ArrayList;

import org.json.JSONObject;


@LineMessageHandler
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());
    private static String currentStage = "";
    private ObjectMapper objectMapper = new ObjectMapper();
    private String path = "./src/main/java/advprog/example/bot/hotcountry/list_rs.json";
    private BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
    private Hospital[] hospitals = objectMapper.readValue(bufferedReader, Hospital[].class);
    private Hospital[] randomHospital = new Hospital[3];
    private Hospital chosenRandomHospital;

    public EchoController() throws IOException {
    }


    @EventMapping
    public List<Message> handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        String[] replyText = contentText.split(" ");

        if (replyText[0].equalsIgnoreCase("/echo")) {
            String replyEchoText = contentText.replace("/echo","");
            return Collections.singletonList(new TextMessage(replyEchoText.substring(1)));
        } else if (replyText[0].equalsIgnoreCase("/billboard")
                && replyText[1].equalsIgnoreCase("hotCountry")
                && replyText.length == 2) {
            HotCountrySong topCountry = new HotCountrySong();
            ArrayList<SongInfo> allTopCountry = topCountry.getDataFromBillboard();
            String replyTopTenBillboardText = "";

            for (int i = 0; i < 10; i++) {
                replyTopTenBillboardText += ("(" + (i + 1) + ")"
                        + " " + allTopCountry.get(i).getSongArtist() + " - "
                        + allTopCountry.get(i).getSongTitle() + "\n");
            }

            return Collections.singletonList(new TextMessage(replyTopTenBillboardText));
        } else if (replyText[0].equalsIgnoreCase("/billboard")
                && replyText[1].equalsIgnoreCase("bill200")
                && replyText.length > 2) {
            TopSong top200Chart = new TopSong();
            ArrayList<SongInfo> top200 = top200Chart.getDataFromBillboard();
            ArrayList<SongInfo> listLagu = new ArrayList<>();
            String contentArtist = contentText.replace("/billboard bill200","");
            String artistName = contentArtist.substring(1);
            String replyBillboardText = "";

            for (int i = 0; i < top200.size(); i++) {
                if (top200.get(i).getSongArtist().contains(artistName)) {
                    listLagu.add(top200.get(i));
                }
            }

            if (listLagu.size() == 0) {
                return Collections.singletonList(new TextMessage("Artist " + artistName + " tidak terdapat dalam billboard"));
            }

            for (int j = 0; j < listLagu.size(); j++) {
                replyBillboardText += listLagu.get(j).getSongArtist()
                        + ("\n" + listLagu.get(j).getSongTitle() + "\n"
                        + listLagu.get(j).getRank() + "\n\n");
            }

            return Collections.singletonList(new TextMessage(replyBillboardText));
        } else if (replyText[0].equalsIgnoreCase("/billboard")
                && replyText[1].equalsIgnoreCase("newage")
                && replyText.length > 2) {
            HotNewAgeSong topNewAge = new HotNewAgeSong();
            ArrayList<SongInfo> topNew = topNewAge.getDataFromBillboard();
            ArrayList<SongInfo> listLagu = new ArrayList<>();
            String contentArtist = contentText.replace("/billboard newage","");
            String artistName = contentArtist.substring(1);
            String replyBillboardText = "";

            for (int i = 0; i < topNew.size(); i++) {
                if (topNew.get(i).getSongArtist().contains(artistName)) {
                    listLagu.add(topNew.get(i));
                }
            }

            if (listLagu.size() == 0) {
                return Collections.singletonList(new TextMessage("Artist " + artistName + " tidak terdapat dalam billboard"));
            }

            for (int j = 0; j < listLagu.size(); j++) {
                replyBillboardText += listLagu.get(j).getSongArtist()
                        + ("\n" + listLagu.get(j).getSongTitle() + "\n"
                        + listLagu.get(j).getRank() + "\n\n");
            }

            return Collections.singletonList(new TextMessage(replyBillboardText));
        } else if ((replyText[0].equalsIgnoreCase("/hospital") && event.getSource() instanceof UserSource )||  (contentText.contains("darurat") && event.getSource() instanceof GroupSource)
                && currentStage.isEmpty()){
            currentStage = "nearest_hospital";
            return requestLocationMessage();

        }
        else {
            return Collections.singletonList(new TextMessage("input tidak dapat dibaca"));
        }
    }

    private List<Message> requestLocationMessage() {
        List<Message> messageList = new ArrayList<>();
        TextMessage textMessage = new TextMessage("Please send your location'");
        CarouselTemplate carouselTemplate = new CarouselTemplate(
                Arrays.asList(
                        new CarouselColumn("http://www.leptonsoftware.com/wp-content/uploads/2016/06/location-min.jpg",
                                "Please Send Location", "Let us help you find the nearest hospital",
                                Collections.singletonList(new URIAction("Send Location",
                                        "https://line.me/R/nv/location")))
                )
        );
        TemplateMessage templateMessage =
                new TemplateMessage("Send your location", carouselTemplate);
        messageList.add(textMessage);
        messageList.add(templateMessage);
        return messageList;

    }
    @EventMapping
    public List<Message> handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) throws Exception {

        LocationMessageContent locationMessage = event.getMessage();
        double currentLatitude = locationMessage.getLatitude();
        double currentLongitude = locationMessage.getLongitude();
        countDistanceToHospital(currentLatitude, currentLongitude);

        if (currentStage.equals("nearest_hospital")) {
            Arrays.sort(hospitals);
            Hospital nearestHospital = hospitals[0];
            return sendHospitalInfo(nearestHospital);
        } else if (currentStage.equals("random_hospital")) {
            return sendHospitalInfo(chosenRandomHospital);
        } else {
            return Collections.singletonList(new TextMessage("Perintah tidak ditemukan!"));
        }
    }
    private void countDistanceToHospital(double currentLatitude, double currentLongitude)
            throws IOException {
        for (Hospital hospital : hospitals) {
            double hospitalLatitude = hospital.getLatitude();
            double hospitalLongitude = hospital.getLongitude();

            String apiUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metrics";
            String origin = String.format("&origins=%s,%s", currentLatitude, currentLongitude);
            String destination = String.format("&destinations=%s,%s", hospitalLatitude, hospitalLongitude);
            String apiKey = "&key=AIzaSyCtkDu8O6LnSH7s7SaUnC734Z6uRJwRPMc";
            String url = String.format("%s%s%s%s", apiUrl, origin, destination, apiKey);

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Cache-Control", "no-cache")
                    .build();

            Response response = client.newCall(request).execute();
            assert response.body() != null;
            JSONObject jsonObject = new JSONObject(response.body().string());
            int distanceFromOrigin = (int) jsonObject.getJSONArray("rows")
                    .getJSONObject(0)
                    .getJSONArray("elements")
                    .getJSONObject(0)
                    .getJSONObject("distance")
                    .get("value");

            hospital.setDistance(distanceFromOrigin);
        }
    }
    private List<Message> sendHospitalInfo(Hospital hospital) {
        List<Message> messageList = new ArrayList<>();

        ImageMessage hospitalImage = new ImageMessage(hospital.getImageLink(),
                hospital.getImageLink());
        LocationMessage hospitalLocation = new LocationMessage(
                hospital.getName(), hospital.getAddress(),
                hospital.getLatitude(), hospital.getLongitude()
        );
        TextMessage hospitalDetail = new TextMessage(
                String.format("Hospital recommendation: %s\n\n" +
                                "Address: %s\n\n%s\n\n" +
                                "Distance: %s metre",
                        hospital.getName(), hospital.getAddress(),
                        hospital.getDescription(),
                        hospital.getDistance())
        );
        messageList.add(hospitalImage);
        messageList.add(hospitalLocation);
        messageList.add(hospitalDetail);
        currentStage = "";
        return messageList;
    }




    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

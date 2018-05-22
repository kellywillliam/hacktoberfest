package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.logging.Logger;


@LineMessageHandler
public class EchoController {
    private static HashMap<String,String> userConfig = new HashMap<>();

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String COUNTRYURL = "https://restcountries.eu/rest/v2/alpha/";
    private static final String APIWEATHERKEY = "&appid=e2379a68cf1e649b79bd43beff3a0407";

    private boolean flag = false;

    TopChartController eventHandler = new TopChartController();

    String errorMessage = "Format yang anda masukkan salah.\n"
            + "Untuk format yang benar adalah sbb :\n"
            + "(1) /oricon jpsingles YYYY (untuk info tahunan)\n"
            + "(2) /oricon jpsingles YYYY-MM (untuk info bulanan)\n"
            + "(3) /oricon jpsingles weekly YYYY-MM-DD (untuk info mingguan ,"
            + "ps: untuk info ini hanya ada untuk tanggal yang jatuh di hari senin)\n"
            + "(4) /oricon jpsingles daily YYYY-MM-DD\n"
            + "(5) /weather (untuk informasi cuaca)\n"
            + "(6) /configure_weather (untuk mengupdate satuan informasi cuaca)";

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText().toLowerCase();


        String[] temp = contentText.split(" ");
        String replyText;

        if (temp[0].equalsIgnoreCase("/oricon")
                && temp[1].equalsIgnoreCase("jpsingles")) {
            if (temp.length > 3) {
                if (temp[2].equalsIgnoreCase("weekly")) {
                    String tanggal = temp[3];
                    replyText = eventHandler.topChartWeekly(tanggal);
                } else if (temp[2].equalsIgnoreCase("daily")) {
                    String tanggal = temp[3];
                    replyText = eventHandler.topChartDaily(tanggal);
                } else {
                    replyText = errorMessage;
                }
            } else {
                String[] tempTanggal = temp[2].split("-");
                if (tempTanggal.length > 1) {
                    replyText = eventHandler.topChartMonthly(tempTanggal[0], tempTanggal[1]);
                } else if (tempTanggal.length == 1) {
                    replyText = eventHandler.topChartYear(tempTanggal[0]);
                } else {
                    replyText = errorMessage;
                }
            }

        } else if (temp[0].equalsIgnoreCase("/weather")) {
            replyText = "Silahkan kirim lokasi kamu agar Sana"
                    + " dapat memberitahu kamu kondisi cuaca ditempat kamu";
            flag = true;

        } else if (temp[0].equalsIgnoreCase("/configure_weather")) {
            replyText = "Kamu ingin ganti satuan suhu dan satuan kecepatan angin ?"
                        + ", Sana bisa membantu kamu dengan mengetik opsi berikut :\n"
                        + "(1) /configure STANDARD (untuk suhu Kelvin "
                        + "dan kecepatan angin Meter/sec\n"
                        + "(2) /configure METRIC (untuk suhu "
                        + "Celcius dan kecepatan angin Meter/sec\n"
                        + "(3) /configure IMPERIAL (untuk suhu "
                        + "Fahrenheit dan kecepatan Miles/hour"
                        + "contoh jika kamu ingin suhunya Celcius dan kecepatan angin Meter/sec "
                        + "maka kamu cukup mengetik : /configure METRIC";

            return new TextMessage(replyText);

        } else if (temp[0].equalsIgnoreCase("/configure")) {
            String userId = content.getId();
            String tipe = temp[1];
            if (tipe.equalsIgnoreCase("STANDARD")) {
                replyText = updateUserConfig(userId,"STANDARD");
            } else if (tipe.equalsIgnoreCase("METRIC")) {
                replyText = updateUserConfig(userId,"METRIC");
            } else if (tipe.equalsIgnoreCase("IMPERIAL")) {
                replyText = updateUserConfig(userId,"IMPERIAL");
            } else {
                replyText = "Opsi yang kamu pilih tidak tersedia :( ";
            }

            return new TextMessage(replyText);

        } else if (temp[0].equalsIgnoreCase("/echo")) {
            replyText = contentText.replace("/echo", "");
            return new TextMessage(replyText.substring(1));

        } else {
            replyText = errorMessage;
            return new TextMessage(replyText);
        }

        return new TextMessage(replyText);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    @EventMapping
    public TextMessage handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LOGGER.fine(String.format("LocationMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));


        if (flag) {
            String replyText;

            LocationMessageContent content = event.getMessage();
            String longitude = Double.toString(content.getLongitude());
            String latitude = Double.toString(content.getLatitude());
            String userId = content.getId();
            String tipe;
            if(!userConfig.containsKey(userId)) {
                userConfig.put(userId,"STANDARD");
                tipe = userConfig.get(userId);
            } else {
                tipe = userConfig.get(userId);
            }
            replyText = getData(longitude,latitude, userId,tipe);

            flag = false;

            return new TextMessage(replyText + " Hashmap: " + getUserConfig().get(userId));
        }

        return new TextMessage("Info yang kamu masukkan salah");

    }

    public HashMap<String, String> getUserConfig() {
        return userConfig;
    }

    public String updateUserConfig(String userId, String tipe) {
        System.out.println(userConfig.get(userId));
        userConfig.put(userId,tipe);
        System.out.println("WOIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
        System.out.println(userConfig.get(userId));
        return "Konfigurasi data kamu sudah di-update YEAY !";
    }

    public String getLocation(JSONObject json) {
        String city = (String) json.get("name"); // Ambil nama kota

        JSONObject sys = (JSONObject) json.get("sys");
        String kodeNegara = (String) sys.get("country");

        String urlNegara = COUNTRYURL + kodeNegara;
        String jsonNegara = getJsonFromApi(urlNegara);

        JSONObject jsonCountry = new JSONObject(jsonNegara);
        String country = (String) jsonCountry.get("name");
        return city + ", " + country;

    }

    public String getWeather(JSONObject json) {
        JSONArray weather = (JSONArray) json.get("weather");
        JSONObject weatherArr = (JSONObject) weather.get(0);
        String hasil = (String) weatherArr.get("main");
        return hasil;
    }

    public double getTemperature(JSONObject json) {
        JSONObject data = (JSONObject) json.get("main");
        double temperature = Double.parseDouble(data.get("temp").toString());
        return temperature;
    }

    public double getWindSpeed(JSONObject json) {
        JSONObject wind = (JSONObject) json.get("wind");
        double windSpeed = Double.parseDouble(wind.get("speed").toString());
        return windSpeed;
    }

    public double getHumidity(JSONObject json) {
        JSONObject data = (JSONObject) json.get("main");
        double humidity = Double.parseDouble(data.get("humidity").toString());
        return humidity;
    }


    public String getJsonFromApi(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        return restTemplate.getForObject(url, String.class, header);
    }



    public String getData(String lon,String lat, String userId,String tipe) {
        String jsonData;
        String userUnit;
//
//        if (!userConfig.containsKey(userId)) {
//            userConfig.put(userId,"STANDARD");
//            userUnit = userConfig.get(userId);
//        } else {
//            userUnit = userConfig.get(userId);
//        }

        String urlApi =  URL + "lat=" + lat + "&lon="
                + lon + "&units=" + tipe + APIWEATHERKEY;
        jsonData = getJsonFromApi(urlApi);

        JSONObject json = new JSONObject(jsonData);
        String location = getLocation(json);
        String weather = getWeather(json);
        double temperature = getTemperature(json);
        double wind = getWindSpeed(json);
        double humidity = getHumidity(json);

        String windUnit = null;
        String tempUnit = null;

        if (tipe.equalsIgnoreCase("STANDARD")) {
            windUnit = "meter/sec";
            tempUnit = "Kelvin";
        } else if (tipe.equalsIgnoreCase("METRIC")) {
            windUnit = "meter/sec";
            tempUnit = "Celcius";
        } else if (tipe.equalsIgnoreCase("IMPERIAL")) {
            windUnit = "miles/hour";
            tempUnit = "Fahrenheit";
        }
//        } else {
//             windUnit = "meter/sec";
//             tempUnit = "Kelvin";
//         }

        String result = text(location,weather,temperature,wind,humidity,windUnit,tempUnit);

        return result;
    }

    public String text(String location, String weather, double temperature, double wind,
                       double humidity, String windUnit, String tempUnit) {
        String result = "Weather at your position (" + location + "):\n"
                + weather + " :" + weather + ":" + "\n" + wind + " " + windUnit
                + "\n" + temperature + " " + tempUnit + "\n"
                + humidity + "%";

        return result;
    }
}

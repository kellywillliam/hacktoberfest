package advprog.example.bot.controller;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

public class WeatherController {

    private static HashMap<String,String> userConfig = new HashMap<>();
    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String COUNTRYURL = "https://restcountries.eu/rest/v2/alpha/";
    private static final String APIWEATHERKEY = "&appid=e2379a68cf1e649b79bd43beff3a0407";

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

    public HashMap<String, String> getUserConfig() {
        return userConfig;
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

    public String updateUserConfig(String userId, String tipe) {
        System.out.println(userConfig.get(userId));
        userConfig.put(userId,tipe);
        System.out.println("WOIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");
        System.out.println(userConfig.get(userId));
        return "Konfigurasi data kamu sudah di-update YEAY !";
    }

    public String getData(String lon,String lat, String userId) {
        String jsonData;
        String userUnit;

        if (!userConfig.containsKey(userId)) {
            userConfig.put(userId,"STANDARD");
            userUnit = userConfig.get(userId);
        } else {
            userUnit = userConfig.get(userId);
        }

        String urlApi =  URL + "lat=" + lat + "&lon="
                    + lon + "&units=" + userUnit + APIWEATHERKEY;
        jsonData = getJsonFromApi(urlApi);

        JSONObject json = new JSONObject(jsonData);
        String location = getLocation(json);
        String weather = getWeather(json);
        double temperature = getTemperature(json);
        double wind = getWindSpeed(json);
        double humidity = getHumidity(json);

        String windUnit = null;
        String tempUnit = null;

        if (userConfig.get(userId).equalsIgnoreCase("STANDARD")) {
            windUnit = "meter/sec";
            tempUnit = "Kelvin";
        } else if (userConfig.get(userId).equalsIgnoreCase("METRIC")) {
            windUnit = "meter/sec";
            tempUnit = "Celcius";
        } else if (userConfig.get(userId).equalsIgnoreCase("IMPERIAL")) {
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

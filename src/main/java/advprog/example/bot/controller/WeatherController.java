package advprog.example.bot.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class WeatherController {

    private HashMap<String,String> userConfig = new HashMap<>();
    private static final String URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String COUNTRYURL = "https://restcountries.eu/rest/v2/alpha/";
    private static final String APIWEATHERKEY = "&appid=e2379a68cf1e649b79bd43beff3a0407";

    public String getLocation(JSONObject json) {
        String city = (String) json.get("name"); // Ambil nama kota

        JSONObject sys = (JSONObject) json.get("sys");
        String kodeNegara = (String) sys.get("country");

        String urlNegara = COUNTRYURL + kodeNegara;
        String jsonNegara = getJSONfromAPI(urlNegara);

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


    public String getJSONfromAPI(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders header = new HttpHeaders();
        return restTemplate.getForObject(url, String.class, header);
    }

    public void updateUserConfig(String userId, String tipe) {
        userConfig.put(userId,tipe);
    }

    public String getData(String dataInformation, String userId) {
        String[] info = dataInformation.split(";");
        String jsonData;
        String userUnit;

        if(!userConfig.containsKey(userId)) {
            userConfig.put(userId,"STANDARD");
            userUnit = userConfig.get(userId);
        } else {
            userUnit = userConfig.get(userId);
        }

        if(info.length == 2) {
            String urlApi =  URL + "lat=" + info[0] + "&lon=" + info[1] + "&units=" + userUnit + APIWEATHERKEY;
            jsonData = getJSONfromAPI(urlApi);
        } else if (info.length == 1) {
            String urlAPI = URL + "q=" + info[0] + "&units=" + userUnit + APIWEATHERKEY;
            jsonData = getJSONfromAPI(urlAPI);
        } else {
            return "Data yang kamu masukkan tidak dapat Sana temukan :( ";
        }

        JSONObject json = new JSONObject(jsonData);
        String location = getLocation(json);
        String weather = getWeather(json);
        double temperature = getTemperature(json);
        double wind = getWindSpeed(json);
        double humidity = getHumidity(json);

        String windUnit = null;
        String tempUnit = null;

        if(userConfig.get(userId).equalsIgnoreCase("STANDARD")) {
            windUnit = "meter/sec";
            tempUnit = "Kelvin";
        } else if(userConfig.get(userId).equalsIgnoreCase("METRIC")) {
            windUnit = "meter/sec";
            tempUnit = "Celcius";
        } else if(userConfig.get(userId).equalsIgnoreCase("IMPERIAL")) {
            windUnit = "miles/hour";
            tempUnit = "Fahrenheit";
        }

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

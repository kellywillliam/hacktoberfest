//package advprog.example.bot;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.client.RestTemplate;
//
//
//
//public class ApiTest {
//    public static void main(String[] args) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders header = new HttpHeaders();
//        JSONObject json = new JSONObject(restTemplate.getForObject("http://api.openweathermap.org/data/2.5/weather?q=JaKaRTa&appid=e2379a68cf1e649b79bd43beff3a0407", String.class, header));
//        System.out.println(json);
//
//        //Ambil Weathernya
//        JSONArray cuaca = (JSONArray) json.get("weather");
//        JSONObject hasil = (JSONObject) cuaca.get(0);
//        System.out.println(hasil.get("main"));
//
//        String kota = (String) json.get("name"); // Ambil nama kota
//        System.out.println(kota);
//
//        //ambil data
//        JSONObject data = (JSONObject) json.get("main");
//        double temp = Double.parseDouble(data.get("temp").toString());
//        double humidity = Double.parseDouble(data.get("humidity").toString());
//        System.out.println(temp);
//        System.out.println(humidity);
//
//        JSONObject angin = (JSONObject) json.get("wind");
//        System.out.println(angin);
//        double windSpeed = Double.parseDouble(angin.get("speed").toString());
//        System.out.println(windSpeed + " meter/sec");
//
//        //ambil kode Negara
//        JSONObject sys = (JSONObject) json.get("sys");
//        String kodeNegara = (String) sys.get("country");
//
//        JSONObject jsonCountry = new JSONObject(restTemplate.getForObject("https://restcountries.eu/rest/v2/alpha/ID", String.class, header));
//
//        String namaNegara = (String) jsonCountry.get("name");
//        System.out.println(kodeNegara);
//        System.out.println(namaNegara);
//    }
//
//}

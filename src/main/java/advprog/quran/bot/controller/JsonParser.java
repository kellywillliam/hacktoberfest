package advprog.quran.bot.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonParser {

    private JSONArray jsonArray;

    public JsonParser() {
        jsonArray = loadData("Quran.json");
    }

    private JSONArray loadData(String namaFile) {
        JSONArray jsonArray = null;
        try {
            JSONParser parser = new JSONParser();
            jsonArray = (JSONArray) parser.parse(new FileReader(namaFile));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return jsonArray;
    }

    public String getSuratNoByName(String namaSurat) {

        for (Object o : jsonArray) {
            JSONObject surat = (JSONObject) o;
            if (surat.get("name").equals(namaSurat)) {
                return (String) surat.get("index");
            }
        }
        return "";
    }

    public JSONObject getSuratByNo(String noSurat) {
        JSONObject suratFound = null;
        for (Object o : jsonArray) {
            JSONObject surat = (JSONObject) o;
            if (surat.get("index").equals(noSurat)) {
                return surat;
            }
        }
        return suratFound;
    }

    public String getAyatFromDatabase(String noSurat, String noAyat) {

        JSONObject surat = getSuratByNo(noSurat);

        if (surat == null) {
            return "Surat yang diminta tidak tersedia";
        }

        String ayat = (String) ((JSONObject) surat.get("verse")).get(noAyat);
        if (ayat == null) {
            return "Ayat yang diminta tidak tersedia";
        }

        ayat += "\n" + (String) ((JSONObject) surat.get("text")).get(noAyat);

        return ayat;
    }


    public String getRandomSuratNo() {
        int randomSuratNo = ThreadLocalRandom.current().nextInt(1, jsonArray.size() + 1);

        int counter = 1;
        String randomIndex = "";
        for (Object o : jsonArray) {
            JSONObject surat = (JSONObject) o;
            if (counter++ == randomSuratNo) {
                randomIndex = (String) surat.get("index");
                break;
            }
        }
        return randomIndex;
    }

    public String getRandomAyat(String noSurat) {

        JSONObject surat = getSuratByNo(noSurat);
        long jumlahAyatSurat = (long) surat.get("count");
        String randomNoAyat = ThreadLocalRandom.current().nextInt(1,
                (int) jumlahAyatSurat + 1) + "";

        String ayat = (String) ((JSONObject) surat.get("verse")).get(randomNoAyat);
        ayat += "\n" + (String) ((JSONObject) surat.get("text")).get(randomNoAyat);

        return ayat;
    }
}
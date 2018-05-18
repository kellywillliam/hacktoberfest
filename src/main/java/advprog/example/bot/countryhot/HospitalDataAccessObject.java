package advprog.example.bot.countryhot;

import java.io.*;
import java.util.ArrayList;

import org.json.*;

public class HospitalDataAccessObject {
    public static ArrayList<Hospital> arrays;

    public static void parseHospitalsJson(){
        try {
            String fileName = "list_rs.json";
            FileReader fileReader =
                    new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =
                    new BufferedReader(fileReader);

            JSONObject myjson = new JSONObject(String.valueOf(bufferedReader.read()));
            JSONArray the_json_array = myjson.getJSONArray("Hospitals");
            int size = the_json_array.length();
            arrays = new ArrayList<Hospital>();
            for (int i = 0; i < size; i++) {
                String alamat = (String) the_json_array.get(0);
                int id = (int) the_json_array.get(2);
                String jenis = (String) the_json_array.get(3);
                String kota = (String) the_json_array.get(7);
                long latitude = (long) the_json_array.get(8);
                long longitude = (long) the_json_array.get(9);
                String nama = (String) the_json_array.get(10);
                String image = (String) the_json_array.get(14);

                Hospital hos = new Hospital(alamat,id,jenis,kota,latitude,longitude,nama,image);
                arrays.add(hos);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

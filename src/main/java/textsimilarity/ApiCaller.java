package textsimilarity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
public class ApiCaller {
    private String jsonString;
    public ApiCaller(String content1, String content2) {
        //TODO
        //Check if content1 and content2 url or text

    }

    private void textSimilarity(String text1, String text2){

    }

    private void urlSimilarity(String url1, String url2){

    }

    private String streamToString(InputStream inputStream) {
        String text = new Scanner(inputStream, "UTF-8").useDelimiter("\\Z").next();
        return text;
    }

    private String jsonGetRequest(String urlQueryString) {
        String json = null;
        try {
            URL url = new URL(urlQueryString);
            //TODO url call

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }
}

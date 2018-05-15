package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

@LineMessageHandler
public class MusicBrainzController {

    private static final Logger LOGGER = Logger.getLogger(MusicBrainzController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) 
            throws IOException, JSONException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        //        TextMessageContent content = event.getMessage();
        //        String contentText = content.getText();
        //        String replyText = contentText.replace("/echo", "");
        //        return new TextMessage(replyText.substring(1));
        
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        //        String[] splitContentText = contentText.split(" ");
        String artistName = contentText.replace("/10albums ", "");
        return new TextMessage(albumSearch(artistName.toLowerCase()));
    }

    public String albumSearch(String namaArtis) throws IOException, JSONException {
        JSONObject artistSearch = null;
        try {
            artistSearch = readJsonFromUrl("https://musicbrainz.org/ws/2/artist/?query=" + formatString(namaArtis) + "&fmt=json");
        } catch (IOException e) {
            return "Artist tidak ditemukan";
        }
        String artistId = "";
        int artistCount = artistSearch.getInt("count");
    
        if (artistCount == 0) {
            return "Artist tidak ditemukan";
        }
    
        for (int x = 0; x < artistCount; x++) {
            JSONObject tempArtist = artistSearch.getJSONArray("artists").getJSONObject(x);
            if (tempArtist.getString("name").toLowerCase().equals(namaArtis)) {
                artistId = tempArtist.getString("id");
                namaArtis = tempArtist.getString("name");
            }
        }

        JSONObject releaseGroupSearch = readJsonFromUrl("https://musicbrainz.org/ws/2/release-group/?query=arid:" + artistId + "%20AND%20type:Album&fmt=json");

        List<String> albums = new ArrayList<String>();

        int releaseGroupCount = releaseGroupSearch.getInt("count");
        for (int x = 0; x < releaseGroupCount; x++) {
            String releaseGroupName = "";
            String releaseYear = "";
            try {
                JSONObject releaseGroup = 
                        releaseGroupSearch.getJSONArray("release-groups").getJSONObject(x);
                JSONObject release = releaseGroup.getJSONArray("releases").getJSONObject(0);
                String releaseId = release.getString("id");
                System.out.println(releaseId);

                String url = "http://musicbrainz.org/ws/2/release/?query=reid:" + releaseId + "&fmt=json";
                JSONObject releaseSearch = readJsonFromUrl(url);

                release = releaseSearch.getJSONArray("releases").getJSONObject(0);
                releaseGroupName = releaseGroup.getString("title");
                releaseYear = "";

                releaseYear = release.getString("date").substring(0, 4);
            } catch (JSONException e) {
                continue;
            }
            System.out.println(releaseYear);
            albums.add(namaArtis + " - " + releaseGroupName + " - " + releaseYear);
            System.out.println(albums.size());
        }

        Collections.sort(albums, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.substring(o2.length() - 4, o2.length())
                        .compareTo(o1.substring(o1.length() - 4, o1.length()));
            }
        });

        String result = "";
        int counter = 0;
        for (int x = 0; x < albums.size(); x++) {
            result += (x + 1) + ". " + albums.get(x) + "\n";
            counter++;
            if (counter == 10) {
                break;
            }
        }

        return result.substring(0, result.length() - 1);
    }
    
    private static String formatString(String input) {
        String[] str = input.split(" ");
        String res = "%22";
        for (String x : str) {
            res += x + "%20";
        }
        return res.substring(0, res.length() - 1) + "2";
    }
    
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = 
                    new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    
    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

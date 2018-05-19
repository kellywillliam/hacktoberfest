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
import java.util.HashMap;
import java.util.logging.Logger;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

@LineMessageHandler
public class AnisonRadioController {
    private static final Logger LOGGER = Logger.getLogger(AnisonRadioController.class.getName());
    private HashMap<String, ArrayList<String>> userIDtoSongs = new HashMap<String, ArrayList<String>>();
    private HashMap<String, String> songsToItunesID = new HashMap<String, String>();
    private boolean askTitleInputState = false;
    
    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws IOException, JSONException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        if (contentText.startsWith("/echo")) {
            return new TextMessage("echo dari anison radio");
        }

        if (event.getSource().toString().contains("groupId")) {
            if (songsToItunesID.containsKey(contentText)) {
                return new TextMessage("We have that song, please chat me "
                        + "and add to your songs to listen!");
            }
        }
        String userId = event.getSource().getUserId();
        if (contentText.equalsIgnoreCase("/add_song")) {
            if (askTitleInputState == false) {
                //enter state where bot waits for song title 
                //and put userId to the HashMap for personal song list
                if (!userIDtoSongs.containsKey(userId)) {
                	userIDtoSongs.put(userId, new ArrayList<String>());
                    askTitleInputState = true;
                    return new TextMessage("Please enter song title");
                } else {
                    askTitleInputState = true;
                    return new TextMessage("Please enter song title");
                }
            } else {
                return new TextMessage("Please enter song title first for us to find");
            }
        } else if (contentText.equalsIgnoreCase("/remove_song")) {
            if (askTitleInputState == false) {
                //replies as carousel from list of songs this userID has to delete
            } else {
                return new TextMessage("Please enter song title first for us to find");
            }
        } else if (contentText.equalsIgnoreCase("/listen_song")) {
            if (askTitleInputState == false) {
                //replies as carousel from list of songs this userID has to listen
            } else {
                return new TextMessage("Please enter song title first for us to find");
            } 
        } else { //else if not a command, it is a song title
            if (askTitleInputState) {
                if (!userIDtoSongs.get(userId).contains(contentText)) { 
                    //checks song title is a Love Live song or not if yes add to his/her map
                	String itunesID = loveLiveSongOrNot(contentText);
                	if (itunesID != null) {
                		userIDtoSongs.get(userId).add(contentText);
                		songsToItunesID.put(contentText, itunesID);
                	} else {
                   	    return new TextMessage("Your song is not "
                	            + "available as a Love Live song, search for another one!");
                	}
                    askTitleInputState = false;
                    return new TextMessage("Your new song is added, your user id: " + userId +
                    		", your song: " + contentText + ", itunes id: " + songsToItunesID.get(contentText));
                } else {
                	return new TextMessage("You have that song already, listen with /listen_song");
                }
            }
        }
        return new TextMessage("Please enter a valid input, "
                + "such as /add_song or /remove_song or /listen_song");
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
    
    public String loveLiveSongOrNot(String title) throws IOException, JSONException {
        // uses api to find the song from Love Live School Idol API
    	JSONObject jsonResponse = readJsonFromUrl("http://schoolido.lu/api/songs/?search=" + title);
    	String itunesID = "";
    	
    	int count = jsonResponse.getInt("count");
		JSONArray result;
		if (count >= 1) {
		    result = jsonResponse.getJSONArray("result");
		    itunesID = result.getJSONObject(0).getString("itunes_id");
		} else {
			itunesID = null;
		}
        System.out.println(itunesID);

    	return itunesID;
    }
    
    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
      }
    
    public String findMusicUrl(String title) {
        // uses api to find the song url sample from iTunes API
        return null;
    }

    public HashMap<String, ArrayList<String>> getMap() {
        return userIDtoSongs;
    }
}

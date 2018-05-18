package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@LineMessageHandler
public class AnisonRadioController {
    private static final Logger LOGGER = Logger.getLogger(AnisonRadioController.class.getName());
    private HashMap<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>();

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        if (contentText.startsWith("/echo")) {
            return new TextMessage("echo dari anison radio");
        }

        if (event.getSource().toString().contains("groupId")) {
            if (map.containsValue(contentText)) {
                //SHOULD BE ARRAYLIST CONTAINING, WILL BE FIXED IN THE FUTURE
                return new TextMessage("We have that song, please chat me"
                        + " personally to hear the music!");
            }
        }

        Integer userId = Integer.parseInt(event.getSource().getUserId());
        
        if (contentText.equalsIgnoreCase("/add_song")) {
            //enter state where bot waits for song title 
            //and put userId to the HashMap for personal song list
            if (!map.containsKey(userId)) {
                map.put(userId, new ArrayList<String>());
            }
        } else if (contentText.equalsIgnoreCase("/remove_song")) {
            //replies as carousel from list of songs this userID has to delete
        } else if (contentText.equalsIgnoreCase("/listen_song")) {
            //replies as carousel from list of songs this userID has to listen
        } else { //else if not a command, it is a song title
            if (map.containsValue(contentText)) { 
                //SHOULD BE ARRAYLIST CONTAINING, WILL BE FIXED IN THE FUTURE
                //checks song title is a Love Live song or not if yes add to his/her map
            }
        }
        return new TextMessage("It is not a Love Live song, try to find another one");
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
    
    public String loveLiveSongOrNot(String title) {
        // uses api to find the song from Love Live School Idol API
        return null;
    }

    public String findMusicUrl(String title) {
        // uses api to find the song url sample from iTunes API
        return null;
    }
}

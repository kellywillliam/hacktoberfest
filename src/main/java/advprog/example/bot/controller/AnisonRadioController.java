package advprog.example.bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.message.AudioMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

@LineMessageHandler
public class AnisonRadioController {
    private static final Logger LOGGER = Logger.getLogger(AnisonRadioController.class.getName());
    private HashMap<String, ArrayList<String>> userIDtoSongs = 
            new HashMap<String, ArrayList<String>>();
    private HashMap<String, String> songsToPreviewUrl = new HashMap<String, String>();
    private HashMap<String, String> songsToAlbumCover = new HashMap<String, String>();
    private HashMap<String, String> songsToArtist = new HashMap<String, String>();
    private boolean askTitleInputState = false;
    private boolean removeOrNot = false;
    
    @Autowired
    private LineMessagingClient lineMessagingClient;
    
    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) 
            throws IOException, JSONException {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        if (contentText.startsWith("/echo")) {
            return new TextMessage("echo dari anison radio");
        }

        if (event.getSource() instanceof GroupSource) {
            if (songsToPreviewUrl.containsKey(contentText)) {
                return new TextMessage("We have that song, chat me "
                        + "and add to your songs to listen!");
            }
        } else {
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
                    ArrayList<CarouselColumn> carouselList = new ArrayList<CarouselColumn>();
                    if (!userIDtoSongs.containsKey(userId) 
                            || userIDtoSongs.get(userId).size() == 0) {
                        return new TextMessage("You have no songs! /add_song to add new songs");
                    }
                    for (int i = 0; i < userIDtoSongs.get(userId).size(); i++) {
                        String song = userIDtoSongs.get(userId).get(i);
                        carouselList.add(new CarouselColumn(songsToAlbumCover.get(song),
                                song, "Artist: " + songsToArtist.get(song), Arrays.asList(
                                new PostbackAction("Remove", "#remove#" + userId + "#" + (song)))));
                    }
                    CarouselTemplate carouselTemplate = new CarouselTemplate(carouselList);
                    TemplateMessage templateMessage = 
                            new TemplateMessage("Carousel alt text", carouselTemplate);
                    this.reply(event.getReplyToken(), templateMessage);
                } else {
                    return new TextMessage("Please enter song title first for us to find");
                }
            } else if (contentText.equalsIgnoreCase("/listen_song")) {
                if (askTitleInputState == false) {
                    ArrayList<CarouselColumn> carouselList 
                            = new ArrayList<CarouselColumn>();
                    if (!userIDtoSongs.containsKey(userId) 
                            || userIDtoSongs.get(userId).size() == 0) {
                        return new TextMessage("You have no songs! /add_song to add new songs");
                    }
                    for (int i = 0; i < userIDtoSongs.get(userId).size(); i++) {
                        String song = userIDtoSongs.get(userId).get(i);
                        carouselList.add(new CarouselColumn(songsToAlbumCover.get(song),
                                song, "Artist: " + songsToArtist.get(song), Arrays.asList(
                                new PostbackAction("Play",(song)))));
                    }
                    CarouselTemplate carouselTemplate = new CarouselTemplate(carouselList);
                    TemplateMessage templateMessage = 
                            new TemplateMessage("Carousel alt text", carouselTemplate);
                    this.reply(event.getReplyToken(), templateMessage);
                } else {
                    return new TextMessage("Please enter song title first for us to find");
                } 
            } else { //else if not a command, it is a song title
                if (askTitleInputState) {
                    if (!userIDtoSongs.get(userId).contains(contentText)) { 
                        //checks song title is a Love Live song or not if yes add to his/her map
                        String answer = loveLiveSongOrNot(contentText);
                        if (answer != null) {
                            String[] index = answer.split("#");
                            String itunesId = index[0];
                            String albumCover = index[1];
                            final String artist = index[2];
                            userIDtoSongs.get(userId).add(contentText);
                            String previewUrl = findMusicUrl(itunesId);
                            songsToPreviewUrl.put(contentText, previewUrl);
                            songsToAlbumCover.put(contentText, albumCover);
                            songsToArtist.put(contentText, artist);
                        } else {
                            return new TextMessage("Your song is not "
                                    + "available as a Love Live song, search for another one!");
                        }
                        askTitleInputState = false;
                        return new TextMessage("Your new song is added,"
                                + " you can listen your new song from /listen_song");
                    } else {
                        return new TextMessage("You have that "
                                + "song already, listen with /listen_song");
                    }
                }
            }
            return new TextMessage("Please enter a valid input, "
                    + "such as /add_song or /remove_song or /listen_song");
        }
        return null;
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
    
    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) {
        String replyToken = event.getReplyToken();
        String content = event.getPostbackContent().getData();
        if (content.startsWith("#remove#")) {
            content = content.replaceAll("#remove#", "");
            String[] userIdAndSong = content.split("#");
            String userId = userIdAndSong[0];
            String song = userIdAndSong[1];
            userIDtoSongs.get(userId).remove(song);
            this.reply(replyToken, new TextMessage("Your song has been removed"));
        }
        this.reply(replyToken, new AudioMessage(songsToPreviewUrl.get(content), 30000));
    }

    private void reply(String replyToken, Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

    private void reply(String replyToken, List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
    
    private void replyText(String replyToken, String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "……";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    public String loveLiveSongOrNot(String title) throws IOException, JSONException {
        // uses api to find the song from Love Live School Idol API
        InputStream is = null;
        title = title.replace(" ", "%20");
        String url = "http://schoolido.lu/api/songs/?search=" + title;

        //http post
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        is = entity.getContent();

        //convert response to string
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        String result = sb.toString();

        //try parse the string to a JSON object
        JSONObject jsonResponse = new JSONObject(result);
        String itunesId = "";
        String albumCover = "";
        String artist = "";
        String answer = "";
        int count = jsonResponse.getInt("count");
        JSONArray results;
        if (count >= 1) {
            results = jsonResponse.getJSONArray("results");
            itunesId = results.getJSONObject(0).get("itunes_id").toString();
            albumCover = "https:" + results.getJSONObject(0).get("image").toString();
            artist = results.getJSONObject(0).get("main_unit").toString();
            answer = itunesId + "#" + albumCover + "#" + artist;
        } else {
            answer = null;
        }
        return answer;
    }

    public String findMusicUrl(String title) 
            throws ClientProtocolException, IOException, JSONException {
        // uses api to find the song url sample from iTunes API
        // uses api to find the song from Love Live School Idol API
        InputStream is = null;
        String url = "https://itunes.apple.com/lookup?id=" + title;

        //http post
        HttpClient httpclient = HttpClientBuilder.create().build();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        is = entity.getContent();

        //convert response to string
        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"),8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        String result = sb.toString();

        //try parse the string to a JSON object
        JSONObject jsonResponse = new JSONObject(result);
        String previewUrl = "";
        int count = jsonResponse.getInt("resultCount");
        JSONArray results;
        if (count >= 1) {
            results = jsonResponse.getJSONArray("results");
            previewUrl = results.getJSONObject(0).get("previewUrl").toString();
        } else {
            previewUrl = null;
        }
        System.out.println(previewUrl);
        return previewUrl;
    }

    public HashMap<String, ArrayList<String>> getMap() {
        return userIDtoSongs;
    }
}
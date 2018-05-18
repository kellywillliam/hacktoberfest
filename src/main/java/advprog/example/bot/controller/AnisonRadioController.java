package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

@LineMessageHandler
public class AnisonRadioController {
    private static final Logger LOGGER = Logger.getLogger(AnisonRadioController.class.getName());
    private HashMap<Integer, ArrayList<String>> map = new HashMap<Integer, ArrayList<String>>();
    private boolean askTitleInputState = false;
    
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
            if (askTitleInputState == false) {
                //enter state where bot waits for song title 
                //and put userId to the HashMap for personal song list
                if (!map.containsKey(userId)) {
                    map.put(userId, new ArrayList<String>());
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
                if (!map.get(userId).contains(contentText)) { 
                    //checks song title is a Love Live song or not if yes add to his/her map
                    map.get(userId).add(contentText);
                    askTitleInputState = false;
                    return new TextMessage("Your new song is added");
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
    
    public String loveLiveSongOrNot(String title) {
        // uses api to find the song from Love Live School Idol API
        return null;
    }

    public String findMusicUrl(String title) {
        // uses api to find the song url sample from iTunes API
        return null;
    }

    public HashMap<Integer, ArrayList<String>> getMap() {
        return map;
    }
}

package advprog.top10.popular.tracks.weekly.bot.controller;

import advprog.top10.popular.tracks.weekly.bot.parser.Parser;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.ArrayList;
import java.util.logging.Logger;

@LineMessageHandler
public class Top100PopularTracksWeeklyController {
    private static final Logger LOGGER
            = Logger.getLogger(Top100PopularTracksWeeklyController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        if (contentText.equalsIgnoreCase("/billboard tropical")) {
            String result = "";
            Parser parser = new Parser();
            ArrayList<String> lstArtist = parser.getArrayArtist();
            ArrayList<String> lstSong  = parser.getArraySong();
            for (int i = 0; i < 10; i++) {
                int num = i+1;
                result += "(" + num + ") " + lstArtist.get(i);
                result += " - " + lstSong.get(i);
                if (i != 9) {
                    result += "\n";
                }
            }
            return new TextMessage(result);
        } else {
            return new TextMessage("Error! Perintah Tidak Ditemukan");
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }


}

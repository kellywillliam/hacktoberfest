package advprog.artist.in.top100.japan.bot.controller;

import advprog.artist.in.top100.japan.bot.parser.Parser;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.ArrayList;
import java.util.logging.Logger;

@LineMessageHandler
public class ArtistTop100Controller {

    private static final Logger LOGGER = Logger.getLogger(ArtistTop100Controller.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();


        if (contentText.contains("/billboard japan100 ")) {
            Parser parser = new Parser();
            ArrayList<String> arrArtist = parser.getArrayArtist();
            String inputArtist = contentText.replace("/billboard japan100 ", "").toLowerCase();
            if (arrArtist.contains(inputArtist)) {
                ArrayList<String> arrSong = parser.getArraySong();
                int position = arrArtist.indexOf(inputArtist) + 1;
                return new TextMessage(inputArtist + "\n"
                        + arrSong.get(position - 1) + "\n" + position);
            }
            String error = "Sorry, your artist doesn't make it to the top 100";
            return new TextMessage(error);
        }
        return new TextMessage("Error! Perintah Tidak Ditemukan");

    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

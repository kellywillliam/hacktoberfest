package advprog.example.bot.controller;

import advprog.example.bot.hotcountry.HotCountrySong;
import advprog.example.bot.hotcountry.SongInfo;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.ArrayList;
import java.util.logging.Logger;

@LineMessageHandler
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        String[] replyText = contentText.split(" ");

        if (replyText[0].equalsIgnoreCase("/echo")) {
            String replyEchoText = contentText.replace("/echo","");
            return new TextMessage(replyEchoText.substring(1));
        } else if (replyText[0].equalsIgnoreCase("/billboard")
                && replyText[1].equalsIgnoreCase("hotcountry")
                && replyText.length == 2) {
            HotCountrySong topCountry = new HotCountrySong();
            ArrayList<SongInfo> allTopCountry = topCountry.getDataFromBillboard();
            String replyTopTenBillboardText = "";

            for (int i = 0; i < 10; i++) {
                replyTopTenBillboardText += ("(" + (i + 1) + ")"
                        + " " + allTopCountry.get(i).getSongArtist() + " - "
                        + allTopCountry.get(i).getSongTitle() + "\n");
            }

            return new TextMessage(replyTopTenBillboardText);
        } else {
            return new TextMessage("input tidak dapat dibaca");
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

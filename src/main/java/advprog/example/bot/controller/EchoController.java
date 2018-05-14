package advprog.example.bot.controller;

import advprog.hot.SongInfo;
import advprog.hot.Top100Chart;
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
                && replyText[1].equalsIgnoreCase("hot100")
                && replyText.length > 2) {
            Top100Chart top100Chart = new Top100Chart();
            ArrayList<SongInfo> top100 = top100Chart.getDataFromBillboard();
            ArrayList<SongInfo> listLagu = new ArrayList<>();
            String contentArtist = contentText.replace("/billboard hot100","");
            String artistName = contentArtist.substring(1);
            String replyBillboardText = "";

            for (int i = 0; i < top100.size(); i++) {
                if (top100.get(i).getSongArtist().contains(artistName)) {
                    listLagu.add(top100.get(i));
                }
            }

            if (listLagu.size() == 0) {
                return new TextMessage("Artist " + artistName + " tidak terdapat dalam billboard");
            }

            for (int j = 0; j < listLagu.size(); j++) {
                replyBillboardText += listLagu.get(j).getSongArtist()
                        + ("\n" + listLagu.get(j).getSongTitle() + "\n"
                        + listLagu.get(j).getRank() + "\n\n");
            }

            return new TextMessage(replyBillboardText);
        } else if (replyText[0].equalsIgnoreCase("/billboard")
                && replyText[1].equalsIgnoreCase("hot100")
                && replyText.length == 2) {
            Top100Chart top100Chart = new Top100Chart();
            ArrayList<SongInfo> top100 = top100Chart.getDataFromBillboard();
            String replyTopTenBillboardText = "";

            for (int i = 0; i < 10; i++) {
                replyTopTenBillboardText += ("(" + (i + 1) + ")"
                        + " " + top100.get(i).getSongArtist() + " - "
                        + top100.get(i).getSongTitle() + "\n");
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

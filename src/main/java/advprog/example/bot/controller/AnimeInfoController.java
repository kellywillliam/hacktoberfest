package advprog.example.bot.controller;

import advprog.example.bot.method.ScrapeMethod;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

@LineMessageHandler
public class AnimeInfoController {

    private static final Logger LOGGER = Logger.getLogger(AnimeInfoController.class.getName());

    public static void main(String[] args){
        System.out.println(ScrapeMethod.showAnime());
    }


    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String replyText = "";
        if (event.getSource() instanceof UserSource) {
            String[] chatText = contentText.split(" ");
            switch (chatText[0].toLowerCase()) {
                case "/is_airing":
                    break;
                case "/echo":
                    replyText = contentText.replace("/echo", "")
                            .substring(1);
                    break;
                default:
                    break;
            }
            return new TextMessage(replyText);
        } else {
            if(contentText.contains("hari ini nonton apa?")){
                String show = ScrapeMethod.showAnime().substring(0,10);
                return new TextMessage(show);
            } else
            return new TextMessage("gak ada anime");
        }




    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }


}

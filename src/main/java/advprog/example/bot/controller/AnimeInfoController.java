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
        String lala = "/is_airing fullmetal alchemist";
        String[] chatText = lala.split(" ");
        String animeName = "";
        String replyText = "";
        if(chatText.length == 2){
            animeName = chatText[1];
            replyText = ScrapeMethod.getAnime(animeName);
            System.out.println(ScrapeMethod.getAnime(replyText));
        }else
            for (int i = 1 ; i < chatText.length; i++){
                if ( chatText.length - 1 == i){
                    animeName += chatText[i];
                } else {
                    animeName += chatText[i] + " ";
                }
            }
            replyText = animeName;
        System.out.println(ScrapeMethod.getAnime(replyText));
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
                    String animeName = "";
                    if(chatText.length == 2){
                        animeName = chatText[1];
                        replyText = ScrapeMethod.getAnime(animeName);
                        return new TextMessage(replyText);
                    } else {
                        for (int i = 1 ; i < chatText.length; i++){
                            if ( chatText.length - 1 == i){
                                animeName += chatText[i];
                            } else {
                                animeName += chatText[i] + " ";
                            }
                        }
                        replyText = ScrapeMethod.getAnime(animeName);
                        return new TextMessage(replyText);
                    }
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
            if (contentText.contains("hari ini nonton apa")
                    || contentText.contains("Hari in   i nonton apa")) {
                replyText = ScrapeMethod.showAnime();
                return new TextMessage(replyText);
            } else if (contentText.contains("/cmd")) {
                replyText = "halo kawan kawan! jika kamu "
                        + "ingin tau apa saja anime yg airing "
                        + "hari ini di jepang, bilang saja 'hari ini nonton apa?";
            }
            return new TextMessage(replyText);
        }




    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }


}

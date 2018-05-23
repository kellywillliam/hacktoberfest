package advprog.example.bot.controller;

import advprog.example.bot.composer.AnimeSeasonComposer;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.io.IOException;
import java.util.logging.Logger;

@LineMessageHandler
public class AnimeSeasonController {

    private static final Logger LOGGER = Logger.getLogger(AnimeSeasonController.class.getName());
    static boolean isLookUpAnime = false;

    AnimeSeasonComposer animeSeasonComposer = new AnimeSeasonComposer();

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();

        if (contentText.equalsIgnoreCase("/lookup_anime")) {
            isLookUpAnime = true;
            return new TextMessage("Please input your preferred anime genre");
        }
        if (!contentText.equalsIgnoreCase("/lookup_anime")) {
            if (isLookUpAnime) {
                animeSeasonComposer.setGenre(contentText);
                CarouselTemplate carouselTemplate = animeSeasonComposer.carouselTemplate(animeSeasonComposer.carouselColumn());
                return new TemplateMessage("Please pick year and season", carouselTemplate);
            }
            else {
                return new TextMessage("What?");
            }
        }
        else {
            return new TextMessage("not available");
        }
    }

    @EventMapping
    public Message handlePostBackEvent(PostbackEvent event) throws IOException {
        String data = event.getPostbackContent().getData();
        isLookUpAnime = false;
        String info = animeSeasonComposer.getWebsite(data);
        return new TextMessage(info);
    }

        @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}
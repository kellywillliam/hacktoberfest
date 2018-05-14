package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;

import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

@LineMessageHandler
public class ImageTagController {

    private static final Logger LOGGER = Logger.getLogger(ImageTagController.class.getName());

    @EventMapping
    public TextMessage handleImageMessageEvent(MessageEvent<ImageMessageContent> event) {
        LOGGER.fine(String.format("ImageMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        ImageMessageContent content = event.getMessage();
        String contentId = content.getId();
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer JDXrmd0FpLk0e6v16czZQq19k9+19NRP7+L364LorekLQUS+FfUd708u30hGXHHLVQQ4hzv3o1g1EJ4sEAhghU47bviQfwAD+0tWt3v51QN+bsyZBgnfKaHHYK6C2cMYK3NA4OjuNvffNSk+bw/oj49PbdgDzCFqoOLOYbqAITQ=");
        String result = restTemplate.getForObject("https://api.line.me/v2/bot/message/{id}/content", String.class, contentId);
//        String replyText = contentText.replace("/tags", "");
        return new TextMessage(result);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

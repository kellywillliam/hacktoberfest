package advprog.quran.bot.controller;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;



@LineMessageHandler
public class QuranController {

    @Autowired
    private LineMessagingClient lineMessagingClient;

    private static final Logger LOGGER = Logger.getLogger(QuranController.class.getName());
    public static boolean isWaitingForReply = false;
    public static QuranMain currentFitur = null;

    QuranMain quranClass = new QuranMain();

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) {

        String format = "TextMessageContent(timestamp='%s',content='%s')";
        LOGGER.fine(String.format(format, event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String[] separated = contentText.split(" ");

        Message replyText = null;

        if (isWaitingForReply) {
            replyText = currentFitur.reply(event);
        } else {
            switch (separated[0]) {
                case "/qs":
                    replyText = quranClass.reply(event);
                    break;
                default:
                    if (event.getSource() instanceof GroupSource
                            && separated[0].toLowerCase().contains("ngaji")) {
                        replyText = quranClass.reply(event);
                    }
                    break;
            }
        }

        if (replyText != null) {
            reply(event.getReplyToken(), replyText);
        }
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages))
                    .get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, Collections.singletonList(message));
    }

}

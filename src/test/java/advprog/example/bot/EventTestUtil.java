package advprog.example.bot;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.postback.PostbackContent;
import com.linecorp.bot.model.event.source.UserSource;


import java.time.Instant;

public class EventTestUtil {

    private EventTestUtil() {
        // Default private constructor
    }

    public static MessageEvent<TextMessageContent> createDummyTextMessage(String text) {
        return new MessageEvent<>("replyToken", new UserSource("userId"),
                new TextMessageContent("id", text), Instant.parse("2018-01-01T00:00:00.000Z"));
    }

    public static MessageEvent<LocationMessageContent> locationMessage(LocationMessageContent loc) {
        return new MessageEvent<>("replyToken", new UserSource("userId"), loc,
                Instant.parse("2018-01-01T00:00:00.000Z"));
    }

    public static PostbackEvent postbackMessage(PostbackContent content) {
        return new PostbackEvent("replyToken", new UserSource("userId"), content,
                Instant.parse("2018-01-01T00:00:00.000Z"));
    }
}

package advprog.example.bot;

import java.time.Instant;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;

public class EventTestUtil {

    private EventTestUtil() {
        // Default private constructor
    }

    public static MessageEvent<TextMessageContent> createDummyTextMessage(String text) {
        return new MessageEvent<>("replyToken", new UserSource("userId"),
                new TextMessageContent("id", text),
                Instant.parse("2018-01-01T00:00:00.000Z"));
    }
    
    public static MessageEvent<LocationMessageContent> 
            createDummyLocationMessage(String title, String address, double latitude, double longitude) {
    	return new MessageEvent<>("replyToken", new UserSource("userId"),
                new LocationMessageContent("0", title, address, latitude, longitude),
                Instant.parse("2018-01-01T00:00:00:000Z"));
    }
}

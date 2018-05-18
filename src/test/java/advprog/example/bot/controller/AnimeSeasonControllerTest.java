package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class AnimeSeasonControllerTest {

    static {
        System.setProperty(
                "line.bot.channelSecret",
                "46da48a24a9f5c1628dda6acc7ba02cf");
        System.setProperty(
                "line.bot.channelToken",
                "2EoGg84oz9jI0+4uv0q2iSemaTwry34G5O6XhOyv3JIWrz5AM11NCVzMJvT7PQ"
                        + "//VQQ4hzv3o1g1EJ4sEAhghU47bviQfwAD+0tWt3v51QNxTERu3SE8uvlJ"
                        + "+OjbHUR9weySIoM5YzuXsRrbjSyaYAdB04t89/1O/w1cDnyilFU=");
    }

    @Autowired
    private AnimeSeasonController animeSeasonController;

    @Test
    void testContextLoads() {
        assertNotNull(animeSeasonController);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/lookup_anime");

        TextMessage reply = animeSeasonController.handleTextMessageEvent(event);

        assertEquals("Please input your preferred anime genre", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        animeSeasonController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
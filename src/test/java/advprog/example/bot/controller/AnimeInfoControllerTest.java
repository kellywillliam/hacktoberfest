package advprog.example.bot.controller;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class AnimeInfoControllerTest {
    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private AnimeInfoController AnimeInfoController;

    @Test
    void testContextLoads() {
        assertNotNull(AnimeInfoController);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Lorem Ipsum");

        TextMessage reply = AnimeInfoController.handleTextMessageEvent(event);

        assertEquals("Lorem Ipsum", reply.getText());

        MessageEvent<TextMessageContent> eventTwo =
                EventTestUtil.createDummyTextMessage("/is_airing boruto");

        TextMessage replyTwo = AnimeInfoController.handleTextMessageEvent(eventTwo);

        assertEquals("boruto is airing", replyTwo.getText());

        MessageEvent<TextMessageContent> eventThree =
                EventTestUtil.createDummyTextMessage("nonton");

        TextMessage replyThree = AnimeInfoController.handleTextMessageEvent(eventThree);

        assertEquals("Steins Gate 25", replyThree.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        AnimeInfoController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}

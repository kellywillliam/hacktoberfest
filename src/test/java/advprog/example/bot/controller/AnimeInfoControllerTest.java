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
public class AnimeInfoControllerTest {
    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private AnimeInfoController animeInfoController;

    @Test
    void testContextLoads() {
        assertNotNull(animeInfoController);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Lorem Ipsum");

        TextMessage reply = animeInfoController.handleTextMessageEvent(event);

        assertEquals("Lorem Ipsum", reply.getText());

        event = EventTestUtil.createDummyTextMessage("/is_airing naruto");

        reply = animeInfoController.handleTextMessageEvent(event);

        assertEquals("Naruto has finished airing at 2007-02-08", reply.getText());

        event = EventTestUtil.createDummyTextMessage("/is_airing zzzzzzz");

        reply = animeInfoController.handleTextMessageEvent(event);

        assertEquals("anime cannot be fouund weebo", reply.getText());

        event = EventTestUtil.createDummyTextMessage("/is_airing");

        reply = animeInfoController.handleTextMessageEvent(event);

        assertEquals("anime cannot be fouund weebo", reply.getText());

        event = EventTestUtil.createDummyTextMessage("/is_airing megalo box");

        reply = animeInfoController.handleTextMessageEvent(event);

        assertEquals("Megalo Box is airing from 2018-04-06 until 0000-00-00", reply.getText());


    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        animeInfoController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}

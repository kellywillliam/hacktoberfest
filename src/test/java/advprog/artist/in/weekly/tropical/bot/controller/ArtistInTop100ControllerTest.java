package advprog.artist.in.weekly.tropical.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.artist.in.weekly.tropical.bot.ArtistInTop100AppTest;
import advprog.artist.in.weekly.tropical.bot.controller.ArtistInTop100Controller;

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
public class ArtistInTop100ControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private ArtistInTop100Controller artistInTop100Controller;

    @Test
    void testContextLoads() {
        assertNotNull(artistInTop100Controller);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                ArtistInTop100AppTest.createDummyTextMessage("/billboard tropical Drake");

        TextMessage reply = artistInTop100Controller.handleTextMessageEvent(event);

        assertEquals("We're sorry to tell you that Drake isn't on the list", reply.getText());
    }

    @Test
    void testHandleTextMessageEventSuccess() {
        MessageEvent<TextMessageContent> event =
                ArtistInTop100AppTest.createDummyTextMessage("/billboard hotcountry Coldplay");

        TextMessage reply = artistInTop100Controller.handleTextMessageEvent(event);

        assertEquals("Coldplay\nYellow\n1", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        artistInTop100Controller.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
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
public class MusicBrainzControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private MusicBrainzController musicBrainzController;

    @Test
    void testContextLoads() {
        assertNotNull(musicBrainzController);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/10albums Fall Out Boy");

        TextMessage reply = musicBrainzController.handleTextMessageEvent(event);

        //assertEquals("Lorem Ipsum", reply.getText());
        //System.out.println(reply.getText());
        assertEquals("Fall Out Boy-M A N I A-2018\nFall Out Boy-American Beauty/American Psycho-2015\n"
        		+ "Fall Out Boy-Save Rock and Roll-2013\nFall Out Boy-Folie à Deux-2008\n"
        		+ "Fall Out Boy-Infinity On High-2007\nFall Out Boy-From Under The Cork Tree-2005\n"
        		+ "Fall Out Boy-Take This to Your Grave-2003", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        musicBrainzController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
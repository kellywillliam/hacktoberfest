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

import java.io.IOException;

import org.json.JSONException;
import org.junit.Before;
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
    void testHandleTextMessageEvent() throws IOException, JSONException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/10albums Fall Out Boy");

        TextMessage reply = musicBrainzController.handleTextMessageEvent(event);

        System.out.println(reply.getText());
        String[] lines = reply.getText().split("\n");
        assertEquals(lines.length, 8);
    }
    
    @Test
    void testHandleTextMessageEventIfArtistDontExist() throws IOException, JSONException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/10albums testabc12345");

        TextMessage reply = musicBrainzController.handleTextMessageEvent(event);

        assertEquals(reply.getText(), "Artist tidak ditemukan");
    }
    
    //    @Test
    //    void testHandleTextMessageEventIfArtistHaveTenOrMoreAlbum() 
    //throws IOException, JSONException {
    //        MessageEvent<TextMessageContent> event =
    //                EventTestUtil.createDummyTextMessage("/10albums mariah carey");
    //
    //        TextMessage reply = musicBrainzController.handleTextMessageEvent(event);
    //
    //        System.out.println(reply.getText());
    //        String[] lines = reply.getText().split("\n");
    //        assertEquals(lines.length, 10);
    //    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        musicBrainzController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
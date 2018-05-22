package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.json.JSONException;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import junit.framework.TestCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class AnisonRadioControllerTest extends TestCase {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private AnisonRadioController anisonRadioController = new AnisonRadioController();

    @Test
    public void testContextLoads() {
        assertNotNull(anisonRadioController);
    }

    @Test
    public void testHandleTextMessageEvent() throws IOException, JSONException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Test");
        TextMessage reply = (TextMessage) anisonRadioController.handleTextMessageEvent(event); //kalau ada error ini casted
        assertEquals("echo dari anison radio", reply.getText());
        
        event = EventTestUtil.createDummyTextMessage("/add_song");
        assertEquals(anisonRadioController.getMap().size(), 0);
        reply = (TextMessage) anisonRadioController.handleTextMessageEvent(event);
        assertEquals("Please enter song title", reply.getText());
        
        event = EventTestUtil.createDummyTextMessage("/add_song");
        reply = (TextMessage) anisonRadioController.handleTextMessageEvent(event);
        assertEquals("Please enter song title first for us to find", reply.getText());
        
        event = EventTestUtil.createDummyTextMessage("/remove_song");
        reply = (TextMessage) anisonRadioController.handleTextMessageEvent(event);
        assertEquals("Please enter song title first for us to find", reply.getText());
        
        event = EventTestUtil.createDummyTextMessage("/listen_song");
        reply = (TextMessage) anisonRadioController.handleTextMessageEvent(event);
        assertEquals("Please enter song title first for us to find", reply.getText());
        
        event = EventTestUtil.createDummyTextMessage("Anything");
        reply = (TextMessage) anisonRadioController.handleTextMessageEvent(event);
        assertEquals("Your new song is added", reply.getText());
        
        event = EventTestUtil.createDummyTextMessage("/add_song");
        assertEquals(anisonRadioController.getMap().size(), 1);
        reply = (TextMessage) anisonRadioController.handleTextMessageEvent(event);
        assertEquals("Please enter song title", reply.getText());
        
        event = EventTestUtil.createDummyTextMessage("Anything");
        reply = (TextMessage) anisonRadioController.handleTextMessageEvent(event);
        assertEquals("Please enter a valid input, "
                + "such as /add_song or /remove_song or /listen_song", reply.getText());
    }

    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        anisonRadioController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
    
    @Test
    public void testLoveLiveSongOrNot() throws IOException, JSONException {
        assertEquals(anisonRadioController.loveLiveSongOrNot("Anything"), null);
    }
    
//    @Test
//    public void testFindMusicUrl() {
//        assertEquals(anisonRadioController.findMusicUrl("Anything"), null);
//    }
}
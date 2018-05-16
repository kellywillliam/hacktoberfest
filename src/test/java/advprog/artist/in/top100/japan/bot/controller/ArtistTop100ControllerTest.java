package advprog.artist.in.top100.japan.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.artist.in.top100.japan.bot.ArtistTop100AppTest;
import advprog.artist.in.top100.japan.bot.parser.Parser;
import advprog.artist.in.top100.japan.bot.controller.ArtistTop100Controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class ArtistTop100ControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private ArtistTop100Controller artistTop100Controller;

    @Test
    void testContextLoads() {
        assertNotNull(artistTop100Controller);
    }

    @Test
    void testHandleTextMessageEventError() {
        MessageEvent<TextMessageContent> event =
                ArtistTop100AppTest.createDummyTextMessage("/billboard japan100 Azhar");
        TextMessage reply = artistTop100Controller.handleTextMessageEvent(event);
        assertEquals("Sorry, your artist doesn't make it to the top 100", reply.getText());
    }

    @Test
    void testHandleWrongInput() {
        MessageEvent<TextMessageContent> event =
                ArtistTop100AppTest.createDummyTextMessage("/billboard japan00 Azhar");
        TextMessage reply = artistTop100Controller.handleTextMessageEvent(event);
        assertEquals("Error! Perintah Tidak Ditemukan", reply.getText());
    }
    @Test
    void testHandleTextMessageEventSuccess() {
        Parser parser = new Parser();
        ArrayList<String> arrArtist = parser.getArrayArtist();
        MessageEvent<TextMessageContent> event =
                ArtistTop100AppTest.createDummyTextMessage("/billboard japan100 "
                + arrArtist.get(0).toLowerCase());

        TextMessage reply = artistTop100Controller.handleTextMessageEvent(event);
        ArrayList<String> arrSong = parser.getArraySong();
        int position = arrArtist.indexOf(arrArtist.get(0)) + 1;
        String result = (arrArtist.get(0) + "\n"
                + arrSong.get(position - 1) + "\n" + position);
        assertEquals(result, reply.getText());


    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        artistTop100Controller.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
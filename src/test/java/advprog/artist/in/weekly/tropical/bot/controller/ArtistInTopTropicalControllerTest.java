package advprog.artist.in.weekly.tropical.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.artist.in.weekly.tropical.bot.ArtistInTopTropicalAppTest;
import advprog.artist.in.weekly.tropical.bot.controller.ArtistInTopTropicalController;
import advprog.artist.in.weekly.tropical.bot.parser.Parser;

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
public class ArtistInTopTropicalControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private ArtistInTopTropicalController artistTop100Controller;

    @Test
    void testContextLoads() {
        assertNotNull(artistTop100Controller);
    }

    @Test
    void testHandleTextMessageEventError() {
        MessageEvent<TextMessageContent> event =
                ArtistInTopTropicalAppTest.createDummyTextMessage("/billboard tropical Azhar");
        TextMessage reply = artistTop100Controller.handleTextMessageEvent(event);
        assertEquals("Sorry, your artist doesn't make it to the top 100", reply.getText());
    }

    @Test
    void testHandleWrongInput() {
        MessageEvent<TextMessageContent> event =
                ArtistInTopTropicalAppTest.createDummyTextMessage("/billboard trop Azhar");
        TextMessage reply = artistTop100Controller.handleTextMessageEvent(event);
        assertEquals("Error! Perintah Tidak Ditemukan", reply.getText());
    }

    @Test
    void testHandleTextMessageEventSuccess() {
        Parser parser = new Parser();
        ArrayList<String> arrArtist = parser.getArrayArtist();
        MessageEvent<TextMessageContent> event =
                ArtistInTopTropicalAppTest.createDummyTextMessage("/billboard tropical "
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
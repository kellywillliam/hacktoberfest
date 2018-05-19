package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class ItunesControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private ItunesController itunesController;

    @Test
    void testContextLoads() {
        assertNotNull(itunesController);
    }

    @Test
    void testHandleTextMessageEvent() throws IOException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Lorem Ipsum");

        TextMessage reply = itunesController.handleTextMessageEvent(event);

        assertEquals("Input yang anda masukkan salah, coba menu bantuan dengan cmd /help", reply.getText());
    }

    @Test
    void testHandleTextMessageEventPreview() throws IOException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/itunes_preview ariana grande");

        TextMessage reply = itunesController.handleTextMessageEvent(event);
    }

    @Test
    void testHandleTextMessageEventHelp() throws IOException {
        String result = "Haii !! Selamat datang di cmd bantuan SONGongBOTjdiorg\n"
                + "Di bot ini kalian bisa menggunakan fitur untuk pencarian preview lagu "
                + "berdasarkan nama artis yang kalian masukkan loh.\n"
                + "Kalau mau coba, berikut commandnya :\n\n"
                + "/itunes_preview 'nama artist'\n"
                + "ex : /itunes_preview ariana grande";

        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/help");
        TextMessage output = itunesController.handleTextMessageEvent(event);
        assertEquals(result, output);
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        itunesController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    void testUrlBuilder() {
        String[] param = {"a", "b"};
        String url =  itunesController.urlBuilder(param);
        assertEquals(url, "https://itunes.apple.com/search?term=a+b");
    }

    @Test
    void testGetSongInformation() throws IOException {
        String[] param = {"ariana", "grande"};
        JSONObject dummy = itunesController.connectApi(param);
        ItunesController.SongInformation test = itunesController.getSongInformation(dummy);
        assertNotNull(test);
    }

}
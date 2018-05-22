package advprog.quran.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.quran.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class QuranControllerTest {

    QuranMain quranClass = new QuranMain();

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private QuranController quranController;

    @Test
    void testContextLoads() {
        assertNotNull(quranController);
    }

    @Test
    void testHandleTextMessageEventForQsWithSuratSuccess() {
        assertNotNull(quranController);
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/qs 114:5");
        assertNotNull(event);
        TextMessage reply = (TextMessage) quranClass.reply(event);
        assertTrue(reply.getText().contains("yang membisikkan (kejahatan) "
                + "ke dalam dada manusia,"));

    }

    @Test
    void testHandleTextMessageEventForQsWithSuratFail() {
        assertNotNull(quranController);
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/qs 10:10xxx");
        assertNotNull(event);

        TextMessage reply = (TextMessage) quranClass.reply(event);
        assertEquals("To display Quran verse please use format "
                + "'/qs ChapterNumber:VerseNumber' ex: '/qs 144:2'", reply.getText());

        event = EventTestUtil.createDummyTextMessage("/qs 10:10");
        reply = (TextMessage) quranClass.reply(event);
        assertEquals("Surat yang diminta tidak tersedia", reply.getText());

        event = EventTestUtil.createDummyTextMessage("/qs 114:10");
        reply = (TextMessage) quranClass.reply(event);
        assertEquals("Ayat yang diminta tidak tersedia", reply.getText());
    }

    @Test
    void testHandleTextMessageEventForQsWithoutSuratSuccess() {
        assertNotNull(quranController);
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/qs");
        TemplateMessage replyCarousel = (TemplateMessage) quranClass.reply(event);
        assertNotNull(replyCarousel);

        event = EventTestUtil.createDummyTextMessage("An-Nas");
        assertNotNull(event);

        TextMessage reply = (TextMessage) quranClass.reply(event);
        assertEquals("Tolong pilih ayat dari surat An-Nas", reply.getText());

        event = EventTestUtil.createDummyTextMessage("5");
        reply = (TextMessage) quranClass.reply(event);
        assertTrue(reply.getText().contains("yang membisikkan (kejahatan) "
                + "ke dalam dada manusia,"));
    }

    @Test
    void testHandleTextMessageEventForQsWithoutSuratFail() {
        assertNotNull(quranController);
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/qs");
        assertNotNull(event);

        TemplateMessage reply = (TemplateMessage) quranClass.reply(event);
        assertNotNull(reply);

        event = EventTestUtil.createDummyTextMessage("qs fail surat");
        TextMessage reply2 = (TextMessage) quranClass.reply(event);
        assertEquals("Surat 'qs fail surat' tidak tersedia", reply2.getText());
    }

    @Test
    void testHandleTextMessageEventForNgajiFail() {
        assertNotNull(quranController);
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("ngaji");
        assertNotNull(event);

        TextMessage reply = (TextMessage) quranClass.reply(event);
        assertNotNull(reply.getText());

        event = EventTestUtil.createDummyTextMessage("Dummy test fail");
        reply = (TextMessage) quranClass.reply(event);
        assertEquals("Jawaban salah!", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        assertNotNull(quranController);
        Event event = mock(Event.class);
        assertNotNull(event);


        quranController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}

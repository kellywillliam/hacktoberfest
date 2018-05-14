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
public class EchoControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private EchoController echoController;

    @Test
    void testContextLoads() {
        assertNotNull(echoController);
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        echoController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    void testHandleTextMessageEventInputWithoutFormat() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("Sample text message");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("Halo, terima kasih atas pesan yang dikirimkan. \n" +
                "Untuk menggunakan bot ini, silakkan kirimkan pesan dengan format" +
                "'/oricon bluray [weekly/daily] [YYYY-MM-DD]' \n" +
                "Contoh: /oricon bluray weekly 2018-05-14", reply.getText());
    }

    @Test
    void testHandleTextMessageEventInputFail() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon bluray weekly");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("Pesan yang kamu kirimkan belum sesuai format." +
                "Pastikan format yang kamu kirimkan sudah lengkap.", reply.getText());
    }

    @Test
    void testHandleTextMessageEventInputWeeklyDaily() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon bluray weekl 2018-05-14");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("Pesan yang kamu kirimkan belum sesuai format." +
                "Pastikan kamu menuliskan 'weekly' atau 'daily' dengan benar.", reply.getText());
    }

    @Test
    void testHandleTextMessageUserInputSuccess() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon bluray weekly 2018-05-14");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        String[] lines = reply.getText().split("\r\n|\r|\n");
        int num = lines.length;

        assertEquals(10, num);
    }

}
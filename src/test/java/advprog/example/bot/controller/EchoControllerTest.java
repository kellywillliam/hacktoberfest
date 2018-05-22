package advprog.example.bot.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import java.time.Instant;
import java.util.List;

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
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Lorem Ipsum");

        List<Message> reply = echoController.handleTextMessageEvent(event);
        System.out.print(reply.get(0));


        //assertEquals("Lorem Ipsum", reply.getText());
    }

    @Test
    void testHandleTextMessageEvent2() {
        MessageEvent<TextMessageContent> event2 =
                EventTestUtil.createDummyTextMessage("/billboard bill200 kezia");

        List<Message> reply = echoController.handleTextMessageEvent(event2);
        System.out.print(reply.get(0));

        //assertEquals("TextMessage(text=Artist kezia tidak terdapat dalam billboard)",
        // reply.get(0));
    }

    @Test
    void testHandleTextMessageEvent3() {
        MessageEvent<TextMessageContent> event3 =
                EventTestUtil.createDummyTextMessage("mengapamengapa");

        List<Message> reply = echoController.handleTextMessageEvent(event3);
        System.out.print(reply.get(0));

        //assertEquals("input tidak dapat dibaca", reply.get(0));
    }

    @Test
    void testHandleTextMessageEvent4() {
        MessageEvent<TextMessageContent> event4 =
                EventTestUtil.createDummyTextMessage("/billboard hotCountry");

        List<Message> reply = echoController.handleTextMessageEvent(event4);
        System.out.print(reply.get(0));


        /*assertTrue(reply.getText().contains("(1)"));
        assertTrue(reply.getText().contains("(2)"));
        assertTrue(reply.getText().contains("(3)"));
        assertTrue(reply.getText().contains("(4)"));
        assertTrue(reply.getText().contains("(5)"));
        assertTrue(reply.getText().contains("(6)"));
        assertTrue(reply.getText().contains("(7)"));
        assertTrue(reply.getText().contains("(8)"));
        assertTrue(reply.getText().contains("(9)"));
        assertTrue(reply.getText().contains("(10)"));*/
    }

    @Test
    void testHandleTextMessageEvent5() {
        MessageEvent<TextMessageContent> event2 =
                EventTestUtil.createDummyTextMessage("/billboard newage kezia");

        List<Message> reply = echoController.handleTextMessageEvent(event2);
        System.out.print(reply.get(0));


        //assertEquals("Artist kezia tidak terdapat dalam billboard", reply.getText());
    }

    @Test
    void testHandleRequestHospitalByUser() throws Exception {
        TextMessageContent textMessageContent = new TextMessageContent("123", "/hospital");
        MessageEvent<TextMessageContent> event = new MessageEvent<>(
                "123", new UserSource("1234"), textMessageContent, Instant.now()
        );
        echoController.handleTextMessageEvent(event);
        LocationMessageContent locationMessageContent =
                new LocationMessageContent("123",
                        "Faculty of Computer Science, University of Indonesia",
                        "Kampus UI Depok, Pd. Cina, Beji, Kota Depok, Jawa Barat 16424",
                        -6.3646009, 106.8264999);
        MessageEvent<LocationMessageContent> event2 = new MessageEvent<>(
                "123", new UserSource("1234"), locationMessageContent, Instant.now()
        );
        echoController.handleLocationMessageEvent(event2);
    }

    @Test
    void testHandleTextMessageRequestRandomHospitalByUser() {
        TextMessageContent textMessageContent = new TextMessageContent("123", "/random_hospital");
        MessageEvent<TextMessageContent> event = new MessageEvent<>(
                "123", new UserSource("1234"), textMessageContent, Instant.now()
        );
        echoController.handleTextMessageEvent(event);
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        echoController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}

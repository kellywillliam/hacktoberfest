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
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Lorem Ipsum");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("Lorem Ipsum", reply.getText());
    }
    
    @Test
    void testHandleTextMessageEvent3() {
        MessageEvent<TextMessageContent> event3 =
                EventTestUtil.createDummyTextMessage("asdfhalsdfjalshf");

        TextMessage reply = echoController.handleTextMessageEvent(event3);

        assertEquals("input tidak dapat dibaca", reply.getText());
    }

    @Test
    void testHandleTextMessageEvent4() {
        MessageEvent<TextMessageContent> event4 =
                EventTestUtil.createDummyTextMessage("/billboard hotCountry");

        TextMessage reply = echoController.handleTextMessageEvent(event4);

        assertTrue(reply.getText().contains("(1)"));
        assertTrue(reply.getText().contains("(2)"));
        assertTrue(reply.getText().contains("(3)"));
        assertTrue(reply.getText().contains("(4)"));
        assertTrue(reply.getText().contains("(5)"));
        assertTrue(reply.getText().contains("(6)"));
        assertTrue(reply.getText().contains("(7)"));
        assertTrue(reply.getText().contains("(8)"));
        assertTrue(reply.getText().contains("(9)"));
        assertTrue(reply.getText().contains("(10)"));
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        echoController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}

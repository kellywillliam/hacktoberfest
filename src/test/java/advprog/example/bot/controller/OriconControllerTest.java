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
public class OriconControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private OriconController oriconController;

    @Test
    void testContextLoads() {
        assertNotNull(oriconController);
    }

    @Test
    void testHandleTextMessageEvent() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon books weekly 2018-05-14");

        TextMessage reply = oriconController.handleTextMessageEvent(event);
        String[] lines = reply.getText().split("\n");
        assertEquals(10, lines.length);
    }
    
    @Test
    void testBebas() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon books weekly 2018-05-13");
        TextMessage reply = oriconController.handleTextMessageEvent(event);
        assertEquals(reply.getText(),
                "weekly ranking on that date is not exist,"
                + " please input a date that has monday as the day.");
    }

    @Test
    void testBebas1() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/hehe");
        TextMessage reply = oriconController.handleTextMessageEvent(event);
        assertEquals(reply.getText(), 
                "Wrong input, use /oricon books weekly <date(YYYY-MM-DD)> to access the feature");
    }
    
    @Test
    void testBebas2() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo books weekly 2018-05-13");
        TextMessage reply = oriconController.handleTextMessageEvent(event);
        assertEquals(reply.getText(), "echo from oricon");
    }
    
    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        oriconController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
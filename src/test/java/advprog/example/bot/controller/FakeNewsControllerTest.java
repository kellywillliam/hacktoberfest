package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import java.io.BufferedReader;
import java.io.FileReader;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class FakeNewsControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private FakeNewsController fakeNewsController;

    @Test
    void testContextLoads() {
        assertNotNull(fakeNewsController);
    }

    @Test
    void testIsConspiracyNews() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/is_conspiracy http://365usanews.com");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);
        assertTrue(reply.getText().contains("http://365usanews.com"));
        assertTrue(reply.getText().contains("is conspiracy news site"));
    }

    @Test
    void testIsFakeNews() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/is_fake http://conservativespirit.com");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);
        assertTrue(reply.getText().contains("http://conservativespirit.com"));
        assertTrue(reply.getText().contains("is fake news site"));
    }

    @Test
    void testIsSatireNews() {
        //TODO test for satire news site
    }

    @Test
    void testGroupChat() {
        //TODO test for group chat
    }

    @Test
    void testAddFakeNews() {
        //TODO test for adding new FakeNews
    }

    @Test
    void testBadInput() {
        //TODO test for bad input in private chat
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        fakeNewsController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
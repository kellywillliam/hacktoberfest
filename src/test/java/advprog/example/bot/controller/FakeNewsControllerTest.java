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
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.message.TextMessage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Instant;

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
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/is_satire http://washingtonsblog.com");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);
        assertTrue(reply.getText().contains("http://washingtonsblog.com"));
        assertTrue(reply.getText().contains("is satire news site"));
    }

    @Test
    void testGroupChat() {
        MessageEvent event = new MessageEvent<>(
                "replyToken",
                new GroupSource("groupId", "userId"),
                new TextMessageContent("id", "I opened http://washingtonsblog.com yesterday"),
                Instant.now()
        );
        
        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);
        assertTrue(reply.getText().contains("http://washingtonsblog.com"));
        assertTrue(reply.getText().contains("satire news site"));
    }

    @Test
    void testAddFakeNews() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/add_filter http://lowell.com fake");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);
        try {
            BufferedReader reader = new BufferedReader(new FileReader("url.csv"));
            String eof = "";
            String readLine = reader.readLine();

            while (readLine != null) {
                eof = readLine;
                readLine = reader.readLine();
            }
            assertTrue(eof.contains("lowell.com,fake"));
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testBadInput() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/hmm hmmmhm hmmmm");

        TextMessage reply = fakeNewsController.handleTextMessageEvent(event);
        assertTrue(reply.getText().contains("Wrong input"));
        assertTrue(reply.getText().contains("Note: URL = news url using HTTP"));
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        fakeNewsController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
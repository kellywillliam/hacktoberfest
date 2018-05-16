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

import javax.xml.soap.Text;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)

public class TweetControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private TweetController tweetController;

    @Test
    void testContextLoads() {
        assertNotNull(tweetController);
    }


    @Test
    void testFoundUser() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/tweet recent melkibens");

        TextMessage reply = tweetController.handleTextMessageEvent(event);
        assertNotNull(reply.getText());
    }

    @Test
    void testHandleProtectedProfile() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/tweet recent vi_aviliani");

        TextMessage reply = tweetController.handleTextMessageEvent(event);
        assertTrue(reply.getText().contains("We can't"));
        assertTrue(reply.getText().contains("@vi_aviliani"));
    }

    @Test
    void testHandleErrorFormattMessage() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("mau kepoin twitter orang dong gan");
        TextMessage reply = tweetController.handleTextMessageEvent(event);
        assertTrue(reply.getText().contains("Oops, Sorry"));
        assertTrue(reply.getText().contains("Please use this command"));
    }

    @Test
    void testHandleUserNotFound()  {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/tweet recent hwhwhw123");
        TextMessage reply = tweetController.handleTextMessageEvent(event);
        assertTrue(reply.getText().contains("We can't retrieve tweets"));
        assertTrue(reply.getText().contains("@hwhwhw123"));
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        tweetController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

}

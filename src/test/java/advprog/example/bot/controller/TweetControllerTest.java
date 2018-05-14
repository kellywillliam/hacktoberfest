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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.xml.soap.Text;

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
    void testHandleProtectedProfile() {
        MessageEvent<TextMessage> event =
                EventTestUtil.createDummyTextMessage("/recent tweet debskiww");

        TextMessage reply = tweetController.handleTextMessageEvent(event);
        assertEquals("We can't retrieve tweets from @debskiww. Please make sure His/Her profile is not protected.",reply.getText());
    }

    @Test
    void testHandleErrorFormattMessage() {
        MessageEvent<TextMessage> event =
                EventTestUtil.createDummyTextMessage("mau kepoin twitter orang dong gan");
        TextMessage reply = tweetController.handleTextMessageEvent(event);
        assertEquals("Oops, Sorry. Please use this command format: /n /recent tweet [username]", reply.getText());
    }

    @Test
    void testHandleUserNotFound(){
        MessageEvent<TextMessage> event =
                EventTestUtil.createDummyTextMessage("/recent tweet hwhwhw123");
        TextMessage reply = tweetController.handleTextMessageEvent(event);
        assertEquals("Failed to retrieve tweets caused by Sorry, that page does not exist.", reply.getText());
    }
//    @Test
//    void testHandleDefaultMessage() {
//        TwitterAPI api = new TwitterAPI();
//        System.out.println(api.getUserTimeLine("melkibens"));
//    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        tweetController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

}

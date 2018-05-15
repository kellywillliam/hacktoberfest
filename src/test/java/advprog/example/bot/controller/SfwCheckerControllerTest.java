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
public class SfwCheckerControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "24202316968b16193c83be859056ccdf");
        System.setProperty("line.bot.channelToken", "+uFmWifpVZJBF1ZuxCaIeiFA7v4FF6D4"
                + "djyNitngehBdGNjpKc7ICYgFZHLFP7L/yuaH+Y"
                + "AIxi22WOgCGGVkwHhjWuJyEl38fBNOhb+A2G6gNJgwFBHQ2f"
                + "+B5ud6ofr7V7oH3ZNKD9scEl+FMTkwdB04t89/1O/w1cDnyilFU=");
    }

    @Autowired
    private SfwCheckerController sfwCheckerController;

    @Test
    void testContextLoads() {
        assertNotNull(sfwCheckerController);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Lorem Ipsum");

        TextMessage reply = sfwCheckerController.handleTextMessageEvent(event);

        assertEquals("Lorem Ipsum", reply.getText());

        MessageEvent<TextMessageContent> eventSfwCaseOne =
                EventTestUtil.createDummyTextMessage("/is_sfw https://image.freepik.com/free-photo"
                        + "/smiling-girl-on-street_23-2147654274.jpg");

        TextMessage replySfwCaseOne = sfwCheckerController.handleTextMessageEvent(eventSfwCaseOne);

        assertEquals("SFW", replySfwCaseOne.getText());

        MessageEvent<TextMessageContent> eventSfwCaseTwo =
                EventTestUtil.createDummyTextMessage("/is_sfw https://i.pinimg.com/736x/8f/89/61/8f8961ce472020ae790c803f86c90077--one-piece-x-one-piece-anime.jpg");

        TextMessage replySfwCaseTwo = sfwCheckerController.handleTextMessageEvent(eventSfwCaseTwo);

        assertEquals("NSFW", replySfwCaseTwo.getText());

        MessageEvent<TextMessageContent> eventSfwCaseThree =
                EventTestUtil.createDummyTextMessage("/is_sfw twitter.com");

        TextMessage replySfwCaseThree = sfwCheckerController.handleTextMessageEvent(
                eventSfwCaseThree);

        assertEquals("Error terjadi", replySfwCaseThree.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        sfwCheckerController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }




}

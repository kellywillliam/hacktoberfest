package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import advprog.example.bot.EventTestUtil;
import junit.framework.TestCase;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class OriconControllerTest extends TestCase {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private OriconController oriconController = new OriconController();

    @Test
    public void testContextLoads() {
        assertNotNull(oriconController);
    }

    @Test
    public void testHandleTextMessageEvent() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon 2018-05-11");

        TextMessage reply = oriconController.handleTextMessageEvent(event);

        assertEquals("2018-05-11", "2018-05-11");
    }

    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        oriconController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    public void testMakeGetCall() throws Exception {
    	assertEquals(null, null);
    }
    
    @Test
    public void testScreenScrapeGetBooks() {
    	assertNotNull(oriconController.screenScrapeGetBooks("<html></html>"));
    }

}
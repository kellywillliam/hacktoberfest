package advprog.example.bot.controller;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import junit.framework.TestCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class PrimbonControllerTest extends TestCase {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private PrimbonController primbonController = new PrimbonController();

    @Test
    public void testContextLoads() {
        assertNotNull(primbonController);
    }

    @Test
    public void testHandleTextMessageEvent() throws Exception {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo test");
        TextMessage reply = primbonController.handleTextMessageEvent(event);
        assertEquals("echo dari primbon", reply.getText());
        
        //Tests input without date
        event = EventTestUtil.createDummyTextMessage("/primbon 1999-02");
        reply = primbonController.handleTextMessageEvent(event);
        assertEquals("Wrong input, please try again!", reply.getText());
        
        //Tests input YYYY-MM-DD contains alphabetic chars
        event = EventTestUtil.createDummyTextMessage("/primbon 1999f-02f-04f");
        reply = primbonController.handleTextMessageEvent(event);
        assertEquals("Wrong input, please try again!", reply.getText());
        
        //Tests invalid month or date or year
        event = EventTestUtil.createDummyTextMessage("/primbon 999-22-00");
        reply = primbonController.handleTextMessageEvent(event);
        assertEquals("Wrong input, please try again!", reply.getText());
    }

    @Test
    public void testInputIsCorrect() throws Exception {
        //Tests correct input
        MessageEvent<TextMessageContent> event = 
                EventTestUtil.createDummyTextMessage("/primbon 1999-02-04");
        TextMessage reply = primbonController.handleTextMessageEvent(event);
        assertEquals("Kamis Kliwon", reply.getText());
    }

    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        primbonController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
    
    @Test
    public void testMakePostCall() throws Exception {
        assertEquals(primbonController.makePostCall("04", "02", "1999"), "Kamis Kliwon");
    }
    
    @Test
    public void testScreenScrapeGetNamaWeton() {
        assertEquals(primbonController.screenScrapeGetNamaWeton("<html>"
                + "<head></head><body id='body'>Test</body>"
                + "</html>"), "Wrong input, please retry");
        assertEquals(primbonController.screenScrapeGetNamaWeton("<html>"
                + "<head></head><body id='body'>Hari Lahir: Test, Tgl. "
                + "4 Februari 1999</body></html>"), "Test");
    }

}
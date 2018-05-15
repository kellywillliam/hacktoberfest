package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jsoup.select.Elements;

import advprog.example.bot.EventTestUtil;
import junit.framework.TestCase;

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
public class BillboardCountryControllerTest extends TestCase {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private BillboardCountryController billboardCountryController = new BillboardCountryController();;

    @Test
    public void testContextLoads() {
        assertNotNull(billboardCountryController);
    }

    @Test
    public void testHandleTextMessageEvent() throws Exception {
        //Tests echo from billboard hotcountry
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Test");
        TextMessage reply = billboardCountryController.handleTextMessageEvent(event);
        assertEquals("echo dari billboard", reply.getText());
        
        //Tests wrong input
        event = EventTestUtil.createDummyTextMessage("random text");
        reply = billboardCountryController.handleTextMessageEvent(event);
        assertEquals("Wrong input, use /billboard hotcountry "
                    + "<desired artist> to access the feature", reply.getText());
    }

    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        billboardCountryController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
    
    @Test
    public void testMakeGetCall() throws Exception {
        assertNotNull(billboardCountryController.makeGetCall());
    }
    
    @Test
    public void testArtistExists() throws Exception {
    	//Tests correct input exists
        Elements elements = billboardCountryController.screenScrapeGetArtists(billboardCountryController.makeGetCall());
        String topArtistOnTheList = elements.get(0).getElementsByClass("chart-row__artist").text();
        MessageEvent<TextMessageContent> event = EventTestUtil.createDummyTextMessage("/billboard hotcountry " + topArtistOnTheList);
        TextMessage reply = billboardCountryController.handleTextMessageEvent(event);
        assertTrue(reply.getText().contains(topArtistOnTheList));
    }
    
    @Test
    public void testArtistNotExists() throws Exception {
    	//Tests correct input not exists
        MessageEvent<TextMessageContent> event = EventTestUtil.createDummyTextMessage("/billboard hotcountry Dummy Artist123");
        TextMessage reply = billboardCountryController.handleTextMessageEvent(event);
        assertEquals(reply.getText(), "Your artist is not on the top 50 Hot Country Songs list");
    }
    
}
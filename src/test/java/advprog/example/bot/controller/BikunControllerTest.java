package advprog.example.bot.controller;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import java.io.IOException;
import java.time.Instant;
import java.util.List;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class BikunControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private BikunController bikunController;

    @Test
    void testContextLoads() {
        assertNotNull(bikunController);
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        bikunController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
    
    @Test
    public void testHandleTextMessageEvent() throws IOException, JSONException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Test");
        TextMessage reply = (TextMessage) 
                bikunController.handleTextMessageEvent(event);
        assertEquals("echo from bikun", reply.getText());
        
        event = EventTestUtil.createDummyTextMessage("/bikun");
        reply = (TextMessage) bikunController.handleTextMessageEvent(event);
        assertEquals("Please send your location", reply.getText());
        
        event = EventTestUtil.createDummyTextMessage("asdf");
        reply = (TextMessage) bikunController.handleTextMessageEvent(event);
        assertEquals("Wrong input, use '/bikun' or '/bikun_stop'", reply.getText());
        
        event = EventTestUtil.createDummyTextMessage("/bikun_stop");
        Message replyMessage = bikunController.handleTextMessageEvent(event);
        assertTrue(replyMessage instanceof TemplateMessage);
        CarouselTemplate template = (CarouselTemplate)((TemplateMessage)replyMessage).getTemplate();
        
        MessageEvent<TextMessageContent> group 
            = new MessageEvent<TextMessageContent>("replyToken", new GroupSource("1", "test"),
                new TextMessageContent("id", "Future Style"),
                Instant.parse("2018-01-01T00:00:00.000Z"));
        reply = (TextMessage) bikunController.handleTextMessageEvent(group);
        assertNull(reply);
        
    }
    
    @Test
    public void testHandleLocationMessageEvent() throws IOException, JSONException {
        MessageEvent<LocationMessageContent> event 
            = new MessageEvent<LocationMessageContent>("replyToken", new UserSource("1"),
                new LocationMessageContent("id", "title", "addr", 1.0, 1.0),
                Instant.parse("2018-01-01T00:00:00.000Z"));
        List<Message> reply = bikunController.handleLocationMessageEvent(event);
        System.out.println(reply);
        assertTrue(reply instanceof List);
        MessageEvent<LocationMessageContent> group 
            = new MessageEvent<LocationMessageContent>("replyToken", new GroupSource("1", "test"),
                new LocationMessageContent("id", "title", "addr", 1.0, 1.0),
                Instant.parse("2018-01-01T00:00:00.000Z"));
        reply = bikunController.handleLocationMessageEvent(group);
        System.out.println(reply);
        assertTrue(reply instanceof List);
    }
    
    @Test
    public void testGetNearestBusStop() throws IOException {
        double lat = Double.parseDouble("-6.364641921940596");
        double lon = Double.parseDouble("106.83225832879543");
        String[] result = bikunController.getNearestBusStop(lat, lon);
        assertEquals(result[0], "[1");
    }
    
    @Test
    public void testGetBusStop() throws IOException {
        String index = "0";
        String[] result = bikunController.getBusStop(index);
        assertEquals(result[0], "[1");
    }
    
    @Test
    public void testGetMinimumTime() {
        String time = "2018-05-23T13:00:00.000";
        int result = bikunController.getMinimumTime(time);
        assertEquals(15, result);
        time = "2018-05-23T13:15:00.000";
        result = bikunController.getMinimumTime(time);
        assertEquals(30, result);
        time = "2018-05-23T13:30:00.000";
        result = bikunController.getMinimumTime(time);
        assertEquals(45, result);
        time = "2018-05-23T13:45:00.000";
        result = bikunController.getMinimumTime(time);
        assertEquals(60, result);
    }
    
    @Test
    public void testGetMinutes() {
        String time = "2018-05-23T13:15:00.000";
        int result = bikunController.getMinutes(time);
        assertEquals(15, result);
    }
}
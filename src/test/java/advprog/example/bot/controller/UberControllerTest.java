package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.postback.PostbackContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;

import java.util.HashMap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class UberControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private UberController uberController;

    @Test
    void testContextLoads() {
        assertNotNull(uberController);
    }


    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        uberController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
    
    @Test
    void testRemoveDestination() throws Exception {
        Message reply;
        MessageEvent<TextMessageContent> event1 =
              EventTestUtil.createDummyTextMessage("/remove_destination");
        reply = uberController.handleTextMessageEvent(event1);
        assertTrue(reply instanceof TemplateMessage);
        
        CarouselTemplate template = (CarouselTemplate)((TemplateMessage)reply).getTemplate();
        for (CarouselColumn column: template.getColumns()) {
            for (Action action: column.getActions()) {
                PostbackAction pba = (PostbackAction)action;
                PostbackContent pbc = new PostbackContent(pba.getData(), new HashMap<>());
                PostbackEvent pbe = EventTestUtil.createDummyPostbackEvent(pbc);
                reply = uberController.handlePostbackEvent(pbe);
            }
        }
    }
    
    @Test
    void testAddDestination() throws Exception {
        Message reply;
        MessageEvent<TextMessageContent> event2 =
                EventTestUtil.createDummyTextMessage("/add_destination");
        reply = uberController.handleTextMessageEvent(event2);
        assertTrue(reply instanceof TemplateMessage);
        
        MessageEvent<LocationMessageContent> event3 =
                EventTestUtil.createDummyLocationMessage("Fasilkom", 
                        "Jalan Professor Doktor Nugroho Notosutanto Pondok Cina,"
                        + " Beji, Kota Depok, Jawa Barat 16424", 
                        -6.3645, 106.8288);
        reply = uberController.handleLocationMessageEvent(event3);
        assertTrue(reply instanceof TextMessage);
        
        MessageEvent<TextMessageContent> event4 =
                EventTestUtil.createDummyTextMessage("Fasilkom");
        reply = uberController.handleTextMessageEvent(event4);
        assertTrue(reply instanceof TextMessage);
    }
    
    @Test
    void testUberEstimate() throws Exception {
        Message reply;

        MessageEvent<TextMessageContent> event1 =
                EventTestUtil.createDummyTextMessage("/uber");
        reply = uberController.handleTextMessageEvent(event1);
        assertTrue(reply instanceof TemplateMessage);

        MessageEvent<LocationMessageContent> event3 = 
                EventTestUtil.createDummyLocationMessage("Fasilkom", 
                        "Jalan Professor Doktor Nugroho Notosutanto Pondok Cina,"
                        + " Beji, Kota Depok, Jawa Barat 16424", 
                        -6.3645, 106.8288);
        reply = uberController.handleLocationMessageEvent(event3);
        assertTrue(reply instanceof TemplateMessage);

        CarouselTemplate template = (CarouselTemplate)((TemplateMessage)reply).getTemplate();
        for (CarouselColumn column: template.getColumns()) {
            for (Action action: column.getActions()) {
                PostbackAction pba = (PostbackAction)action;
                PostbackContent pbc = new PostbackContent(pba.getData(), new HashMap<>());
                PostbackEvent pbe = EventTestUtil.createDummyPostbackEvent(pbc);
                reply = uberController.handlePostbackEvent(pbe);
            }
        }
    }
}
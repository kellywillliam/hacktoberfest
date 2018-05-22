package advprog.example.bot.controller;

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
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;

import advprog.example.bot.EventTestUtil;

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
    
    void testUberEstimate() {
    	
    }
    
    void testAddDestination() {
        
    }
    
    @Test
    void testRemoveDestination() throws Exception {
        MessageEvent<TextMessageContent> event1 =
              EventTestUtil.createDummyTextMessage("/remove_destination");
        uberController.handleTextMessageEvent(event1);
        MessageEvent<TextMessageContent> event2 =
                EventTestUtil.createDummyTextMessage("/add_destination");
        uberController.handleTextMessageEvent(event2);
        MessageEvent<LocationMessageContent> event3 =
                EventTestUtil.createDummyLocationMessage("Fasilkom", 
                		"Jalan Professor Doktor Nugroho Notosutanto Pondok Cina, Beji, Kota Depok, Jawa Barat 16424", 
                		-6.3645, 106.8288);
        uberController.handleLocationMessageEvent(event3);
        MessageEvent<TextMessageContent> event4 =
                EventTestUtil.createDummyTextMessage("Fasilkom");
        uberController.handleTextMessageEvent(event4);
    }
}
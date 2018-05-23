package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.postback.PostbackContent;
import com.linecorp.bot.model.message.TextMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;

public class HangoutControllerTest extends TestCase {
    @Autowired
    public HangoutController hangoutController = new HangoutController();

    @Test
    public void testContextLoads() {
        assertNotNull(hangoutController);
    }

    @Test
    public void testHandleTextMessageEvent1() {
        hangoutController = new HangoutController();
        MessageEvent<TextMessageContent> event = EventTestUtil
                .createDummyTextMessage("/hangout_kuy");
        assertEquals("Please send your location to me",
                hangoutController.handleTextMessageEvent(event).get(0).getText());
    }

    @Test
    public void testHandleTextMessageEvent2() {
        hangoutController = new HangoutController();
        MessageEvent<TextMessageContent> event = EventTestUtil
                .createDummyTextMessage("/random_hangout_kuy");
        assertEquals("Please send your location to me",
                hangoutController.handleTextMessageEvent(event).get(0).getText());
    }

    @Test
    public void testHandleTextMessageEvent3() {
        MessageEvent<TextMessageContent> event = EventTestUtil
                .createDummyTextMessage("/nearby_hangout_kuy 1500");
        assertEquals("Please send your location to me",
                hangoutController.handleTextMessageEvent(event).get(0).getText());
    }

    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);
        hangoutController.handleDefaultMessage(event);
        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    public void testHandleLocationMessageEvents() throws IOException {
        hangoutController = new HangoutController();
        MessageEvent<TextMessageContent> event = EventTestUtil
                .createDummyTextMessage("/hangout_kuy");
        LocationMessageContent message = new LocationMessageContent("1", "UI",
                "Kampus UI Depok, Pd. Cina, Beji, Kota Depok, Jawa Barat 16424", -6.3646009,
                106.8286886);
        MessageEvent<LocationMessageContent> event2 = EventTestUtil.locationMessage(message);
        hangoutController.handleLocationMessage(event2);

        event = EventTestUtil.createDummyTextMessage("/random_hangout_kuy");
        event2 = EventTestUtil.locationMessage(message);
        hangoutController.handleLocationMessage(event2);

        event = EventTestUtil.createDummyTextMessage("/nearby_hangout_kuy 130");
        event2 = EventTestUtil.locationMessage(message);
        hangoutController.handleLocationMessage(event2);
    }

    @Test
    public void testDistance() {
        hangoutController.getDistance(-6.3646009, -6.4646009, 106.8286884, 106.8296884);
    }

    @Test
    public void testnearestHangout() throws IOException {
        hangoutController.nearestHangout(-6.3646009, 106.8286884);
    }

    @Test
    public void testgetnearestPlace() throws IOException {
        hangoutController.getNearestPlace(-6.3646009, 106.8286884);
    }

    @Test
    public void testgetListCarouel() throws IOException {
        hangoutController.getListCarousel();
    }

    @Test
    public void testgetRandom() {
        hangoutController.getRandom();
    }

    @Test
    public void testHandlePostbackEvent() throws IOException {
        hangoutController.carousel();
        PostbackContent action = new PostbackContent("1", null);
        PostbackEvent event = EventTestUtil.postbackMessage(action);
        hangoutController.handlePostbackEvent(event);

        hangoutController.carousel();
        action = new PostbackContent("2", null);
        event = EventTestUtil.postbackMessage(action);
        hangoutController.handlePostbackEvent(event);

        hangoutController.carousel();
        action = new PostbackContent("3", null);
        event = EventTestUtil.postbackMessage(action);
        hangoutController.handlePostbackEvent(event);
    }
}

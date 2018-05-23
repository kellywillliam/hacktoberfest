package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.postback.PostbackContent;

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
        hangoutController.handleTextMessageEvent(event);
    }

    @Test
    public void testHandleTextMessageEvent2() {
        hangoutController = new HangoutController();
        MessageEvent<TextMessageContent> event = EventTestUtil
                .createDummyTextMessage("/random_hangout_kuy");
        hangoutController.handleTextMessageEvent(event);
    }

    @Test
    public void testHandleTextMessageEvent3() {
        MessageEvent<TextMessageContent> event = EventTestUtil
                .createDummyTextMessage("/nearby_hangout_kuy 1500");
        hangoutController.handleTextMessageEvent(event);
    }

    @Test
    public void testHandleDefaultMessage() {
        Event event = mock(Event.class);
        hangoutController.handleDefaultMessage(event);
        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    private static int flag = 1;

    @Test
    public void testHandleLocationMessageEvents() {
        LocationMessageContent message = new LocationMessageContent("1", "UI",
                "Kampus UI Depok, Pd. Cina, Beji, Kota Depok, Jawa Barat 16424", -6.3646009,
                106.8286886);
        MessageEvent<LocationMessageContent> event = EventTestUtil.locationMessage(message);
        // hangoutController.handleLocationMessage(event);
        flag = 2;
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
    }
}
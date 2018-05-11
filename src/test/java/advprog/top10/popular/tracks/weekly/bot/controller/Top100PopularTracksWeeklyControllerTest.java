package advprog.top10.popular.tracks.weekly.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.top10.popular.tracks.weekly.bot.Top100PopularTracksWeeklyAppTest;
import advprog.top10.popular.tracks.weekly.bot.controller.Top100PopularTracksWeeklyController;

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
public class Top100PopularTracksWeeklyControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private Top100PopularTracksWeeklyController top100PopularTracksWeeklyController;

    @Test
    void testContextLoads() {
        assertNotNull(top100PopularTracksWeeklyController);
    }

    @Test
    void testHandleTextMessageEventSuccess() {
        MessageEvent<TextMessageContent> event =
                Top100PopularTracksWeeklyAppTest.createDummyTextMessage("/billboard tropical");

        TextMessage reply = top100PopularTracksWeeklyController.handleTextMessageEvent(event);

        assertEquals("(1) Darude - Sandstorm\r\n"
                + "(2) Simon & Garfunkel - Scarborough Fair\r\n"
                + "(3) Lazy Town - We Are Number One\r\n" + "...\r\n"
                + "(10) Christopher Tin - Sogno di Volare\r\n"
                + "", reply.getText());
    }

    @Test
    void testHandleTextMessageEventError() {
        MessageEvent<TextMessageContent> event = Top100PopularTracksWeeklyAppTest
                .createDummyTextMessage("/billboard tropica");

        TextMessage reply = top100PopularTracksWeeklyController.handleTextMessageEvent(event);

        assertEquals("error", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        top100PopularTracksWeeklyController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
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
public class TopChartControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private TopChartController topChartController;

    @Test
    void testContextLoads() {
        assertNotNull(topChartController);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon Lorem Ipsum");

        TextMessage reply = topChartController.handleTextMessageEvent(event);

        assertEquals("Lorem Ipsum", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        topChartController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }


    @Test
    void topChartWeeklyTest() {
        assertEquals(topChartController.topChartWeekly("/oricon jpsingles weekly 2018-05-09"), null);
    }

    @Test
    void topChartMonthlyTest() {
        assertEquals(topChartController.topChartMonthly("/oricon jpsingles 2018-05"), null);
    }

    @Test
    void topChartDateTest() {
        assertEquals(topChartController.topChartDate("/oricon jpsingles daily 2018-05-09"),null);
    }

    @Test
    void topChartYearTest() {
        assertEquals(topChartController.topChartYear("/oricon jpsingles 2018"), null);
    }
}
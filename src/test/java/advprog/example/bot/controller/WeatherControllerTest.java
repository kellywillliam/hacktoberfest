package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import advprog.example.bot.EventTestUtil;
import advprog.example.bot.controller.WeatherController;


import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.MessageEvent;

import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;



import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class WeatherControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Test
    void testContextLoads() {
        assertNotNull(WeatherController.class);
    }

    @Test
    void TestHandleLocationMessageEvent() {
        EchoController echoController = new EchoController();

        MessageEvent<TextMessageContent> eventCall =
                EventTestUtil.createDummyTextMessage("/weather");
        echoController.handleTextMessageEvent(eventCall);
        MessageEvent<LocationMessageContent> eventLoc =
                EventTestUtil.createDummyLocation(106.832578,-6.369084);
        TextMessage reply = echoController.handleLocationMessageEvent(eventLoc);

        assertTrue(reply.getText().contains("Depok"));

    }

    @Test
    void TestHandleLocationMessageEventFail() {
        EchoController echoController = new EchoController();

        MessageEvent<LocationMessageContent> eventLoc =
                EventTestUtil.createDummyLocation(106.832578,-6.369084);
        TextMessage reply = echoController.handleLocationMessageEvent(eventLoc);

        assertTrue(reply.getText().contains("Info yang kamu masukkan salah"));

    }

    @Test
    void TestHandleGroupLocationMessageEvent() {
        EchoController echoController = new EchoController();

        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessageGroup("coi cuaca di Medan gimana?");

        TextMessage reply = echoController.handleTextMessageEvent(event);
        assertTrue(reply.getText().contains("Medan"));

        MessageEvent<TextMessageContent> event1 =
                EventTestUtil.createDummyTextMessageGroup("lae cuaca di Jakarta gimana?");

        TextMessage reply1 = echoController.handleTextMessageEvent(event1);
        assertTrue(reply1.getText().contains("Jakarta"));

        MessageEvent<TextMessageContent> event2 =
                EventTestUtil.createDummyTextMessageGroup("boboboiiii cuaca di Depok gimana?");

        TextMessage reply2 = echoController.handleTextMessageEvent(event2);
        assertTrue(reply2.getText().contains("Depok"));


    }

    @Test
    void TestHandleGroupLocationMessageEventFail() {
        EchoController echoController = new EchoController();

        MessageEvent<TextMessageContent> event3 =
                EventTestUtil.createDummyTextMessageGroup("boboboiiii cuaca di Konohagakure gimana?");

        TextMessage reply3 = echoController.handleTextMessageEvent(event3);
        assertTrue(reply3.getText().contains("Sana"));
    }


}

package advprog.TopLaughers.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import advprog.example.bot.EventTestUtil;

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
public class TopLaughersControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private TopLaughersControllerStub topLaughersControllerStub;

    @Test
    void testContextLoads() {
        assertNotNull(topLaughersControllerStub);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/toplaughers");

        TextMessage reply = topLaughersControllerStub.handleTextMessageEvent(event);

        assertEquals("1. FirstPerson \n2. SecondPerson \n3. ThirdPerson \n4. ForthPerson \n5. Fifth Person", reply.getText());
    }}

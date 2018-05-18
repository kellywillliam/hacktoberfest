package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import javax.xml.soap.Text;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)

public class MusicControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private MusicController musicController;

    @Test
    void testContextLoads() {
        assertNotNull(musicController);
    }

    @Test
    void testRetrieveSoundtrack() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/vgmdb OST this month");

        TextMessage reply = musicController.handleTextMessageEvent(event);

        assertTrue(reply.getText().contains("Original Soundtrack"));
        assertTrue(reply.getText().contains("IDR"));

    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        musicController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

}

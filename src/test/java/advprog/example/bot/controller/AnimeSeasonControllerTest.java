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
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class AnimeSeasonControllerTest {

    @Autowired
    private AnimeSeasonController animeSeasonController;

    @Test
    void testContextLoads() {
        assertNotNull(animeSeasonController);
    }

    @Test
    void testHandleTextMessageEvent_booleanIsLookupAnimeTrueLookup() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/lookup_anime");

        if (animeSeasonController.isLookUpAnime) {
            Message reply = animeSeasonController.handleTextMessageEvent(event);

            assertEquals(new TextMessage("Please input your preferred anime genre"), reply);
        }
    }

    @Test
    void testHandleTextMessageEvent_booleanIsLookupAnimeFalse() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("Drama");

        if (!animeSeasonController.isLookUpAnime) {
            Message reply = animeSeasonController.handleTextMessageEvent(event);

            assertEquals(new TextMessage(""), reply);
        }
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        animeSeasonController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
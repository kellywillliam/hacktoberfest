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
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class ImageTagControllerTest {

    static {
        System.setProperty(
                "line.bot.channelSecret",
                "46da48a24a9f5c1628dda6acc7ba02cf");
        System.setProperty(
                "line.bot.channelToken",
                "2EoGg84oz9jI0+4uv0q2iSemaTwry34G5O6XhOyv3JIWrz5AM11NCVzMJvT7PQ"
                        + "//VQQ4hzv3o1g1EJ4sEAhghU47bviQfwAD+0tWt3v51QNxTERu3SE8uvlJ"
                        + "+OjbHUR9weySIoM5YzuXsRrbjSyaYAdB04t89/1O/w1cDnyilFU=");
    }


    @Test
    void testHandleTextMessageEvent() throws IOException, JSONException {
        ImageTagController imageTagController = new ImageTagController();

        MessageEvent<TextMessageContent> eventText = EventTestUtil.createDummyTextMessage("/tags");
        TextMessage replyText = imageTagController.handleTextMessageEvent(eventText);
        MessageEvent<ImageMessageContent> event =
                EventTestUtil.createDummyImageMessage("7954184469074");
        assertEquals(replyText.getText(), "Now upload your image");
        assertTrue(imageTagController.isTags);

        TextMessage reply = imageTagController.handleImageMessageEvent(event);
        System.out.println(reply);
    }

    @Test
    void testHandleTextMessageEventNotTags() throws IOException, JSONException {
        ImageTagController imageTagController = new ImageTagController();
        MessageEvent<TextMessageContent> eventText = EventTestUtil.createDummyTextMessage("/ping");
        TextMessage replyText = imageTagController.handleTextMessageEvent(eventText);
        assertEquals(replyText.getText(), "not available");
        MessageEvent<ImageMessageContent> event =
                EventTestUtil.createDummyImageMessage("7954184469074");
        TextMessage reply = imageTagController.handleImageMessageEvent(event);
        assertEquals(reply.getText(), "succesfully uploaded your image");
    }

    @Test
    void testHandleImageDoesNotExist() throws IOException {
        ImageTagController imageTagController = new ImageTagController();
        String result = imageTagController.uploadContentToImagga();
        assertEquals(result, "file does not exists");
    }


    @Test
    void testHandleDefaultMessage() {

        ImageTagController imageTagController = new ImageTagController();
        Event event = mock(Event.class);

        imageTagController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}
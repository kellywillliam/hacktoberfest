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
public class ImageCropControllerTest {

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
        ImageCropController imageCropController = new ImageCropController();

        MessageEvent<TextMessageContent> eventText = EventTestUtil.createDummyTextMessage("/crop");
        TextMessage replyText = imageCropController.handleTextMessageEvent(eventText);
        assertEquals(replyText.getText(), "Now upload your image");
        MessageEvent<ImageMessageContent> event =
                EventTestUtil.createDummyImageMessage("7954184469074");
        assertTrue(imageCropController.isCrop);
        TextMessage reply = imageCropController.handleImageMessageEvent(event);
        System.out.println(reply);
    }

    @Test
    void testHandleTextMessageEventNotCrop() throws IOException, JSONException {
        ImageCropController imageTagController = new ImageCropController();
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
        File fileToBeDeleted = new File("src/main/resources/imagecrop.jpg");
        fileToBeDeleted.delete();
        ImageCropController imageCropController = new ImageCropController();
        String result = imageCropController.uploadContentToImgur();
        System.out.println(result);
        assertEquals(result, "file does not exist");
    }


    @Test
    void testHandleDefaultMessage() {

        ImageCropController imageCropController = new ImageCropController();
        Event event = mock(Event.class);

        imageCropController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }
}

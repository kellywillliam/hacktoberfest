package advprog.example.bot.xkcd.comic;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.linecorp.bot.model.message.ImageMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.junit.jupiter.api.Test;


class XkcdComicTest {
    @Test
    void testValidComicIdReturnImageMessage() {
        Message result = XkcdComic.getComic("20");

        assertTrue(result instanceof ImageMessage);
    }

    @Test
    void testValidComicIdReturnRightImage() {
        ImageMessage result = (ImageMessage) XkcdComic.getComic("20");

        assertNotNull(result);
        assertEquals("https://imgs.xkcd.com/comics/ferret.jpg",
                result.getOriginalContentUrl());
    }

    @Test
    void testInvalidComicIdReturnTextMessage() {
        Message result = XkcdComic.getComic("20");

        assertTrue(result instanceof TextMessage);
    }

    @Test
    void testInvalidComicIdReturnRightImage() {
        TextMessage result = (TextMessage) XkcdComic.getComic("20");

        assertNotNull(result);
        assertEquals("Comic ID is invalid :(",
                result.getText());
    }

}

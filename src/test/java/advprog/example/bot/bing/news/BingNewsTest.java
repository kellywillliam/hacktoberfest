package advprog.example.bot.bing.news;

import static org.junit.Assert.assertNotNull;

import com.linecorp.bot.model.message.TextMessage;
import org.junit.jupiter.api.Test;

class BingNewsTest {
    @Test
    void testGetNewsReturnTextMessage() {
        TextMessage textMessage = BingNews
                .getFiveNews("north korea");
        assertNotNull(textMessage);
        assertNotNull(textMessage.getText());
    }
}

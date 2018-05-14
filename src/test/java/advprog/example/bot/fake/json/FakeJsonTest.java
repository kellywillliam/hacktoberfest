package advprog.example.bot.fake.json;

import static org.junit.Assert.assertTrue;

import com.linecorp.bot.model.message.TextMessage;
import org.junit.jupiter.api.Test;

public class FakeJsonTest {
    @Test
    void testDataIsNull() {
        TextMessage textMessage = FakeJson.getFakeJson();
        assertTrue(textMessage.getText() != null);
    }

}

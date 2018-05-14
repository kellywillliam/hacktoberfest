package advprog.example.bot.sentiment.analysis;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.linecorp.bot.model.message.TextMessage;
import org.junit.jupiter.api.Test;


public class SentimentAnalysisTest {
    @Test
    public void testSentimentAnalysisReturnTextMessage() {
        TextMessage result = SentimentAnalysis.analyzeText("lorem ipsum dolor\n");
        assertTrue(result.getText() != null);
    }

    @Test
    public void testSentimentResultForBadText() {
        String result = SentimentAnalysis.analyzeText("Fuck You\n").getText();
        assertThat(result, containsString("bad"));
    }

    @Test
    public void testSentimentResultForGoodText() {
        String result = SentimentAnalysis.analyzeText("I Love You\n").getText();
        assertThat(result, containsString("good"));
    }
}

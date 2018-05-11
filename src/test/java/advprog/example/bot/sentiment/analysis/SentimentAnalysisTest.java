package advprog.example.bot.sentiment.analysis;

import static org.junit.Assert.assertTrue;

import com.linecorp.bot.model.message.TextMessage;
import org.junit.jupiter.api.Test;

public class SentimentAnalysisTest {
    @Test
    public void testSentimentAnalysisReturnTextMessage() {
        TextMessage result = SentimentAnalysis.analyzeText("lorem ipsum dolor\n");
        assertTrue(result != null);
        assertTrue(result.getText() != null);
    }

    @Test
    public void testSentimentResultIsDoubleString() {
        String result = SentimentAnalysis.analyzeText("lorem ipsum dolor\n").getText();
        Double score = Double.parseDouble(result);
        assertTrue(score <= 1.0 && score >= 0.0);
    }
}

package advprog.example.bot.confidence.percentage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class ConfidencePercentageTest {

    static {
        System.setProperty("line.bot.channelSecret", "24202316968b16193c83be859056ccdf");
        System.setProperty("line.bot.channelToken", "+uFmWifpVZJBF1ZuxCaIeiFA7v4FF6D4"
                + "djyNitngehBdGNjpKc7ICYgFZHLFP7L/yuaH+Y"
                + "AIxi22WOgCGGVkwHhjWuJyEl38fBNOhb+A2G6gNJgwFBHQ2f"
                + "+B5ud6ofr7V7oH3ZNKD9scEl+FMTkwdB04t89/1O/w1cDnyilFU=");
    }

    @Test
    void testGetConfidencePercentageIsSfw() throws java.io.IOException {
        assertEquals("SFW", ConfidencePercentage.getConfidencePercentage(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/36/Hopetoun_falls.jpg/300px-Hopetoun_falls.jpg"));
    }

    @Test
    void testGetConfidencePercentageIsNsfw() throws java.io.IOException {
        assertEquals("NSFW", ConfidencePercentage.getConfidencePercentage(
                "https://i.pinimg.com/736x/8f/89/61/8f8961ce472020ae790c803f86c90077--one-piece-x-one-piece-anime.jpg"));
    }

    @Test
    void testGetConfidencePercentageIsNotImage() throws java.io.IOException {
        assertEquals("Error terjadi", ConfidencePercentage.getConfidencePercentage(
                "twitter.com"));
    }



}

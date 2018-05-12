package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class DetectLanguangeTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Test
    void testContextLoads() {
        assertNotNull(DetectLanguage.class);
    }

    @Test
    void testDetectLangTextSuccess() {
        assertEquals("en 0.7857", DetectLanguage.detectLang("god is great"));
    }

    @Test
    void testDetectLangUrlSuccess() {
        assertEquals("en 1.0",
                DetectLanguage.detectLang("https://www.facebook.com"));
    }

    @Test
    void testDetectLangUrlFailed() {
        assertEquals("Some errors've occured, make sure your text is a valid text!",
                DetectLanguage.detectLang("https://wwww.google.com"));
    }
}

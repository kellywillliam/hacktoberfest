package advprog.example.bot.method;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class ScrapeMethodTest {



    @Test
    void scrapeGroupTest() {
        String scr = ScrapeMethod.scrapeGroup();
        assertEquals(null,scr);
    }

    @Test
    void scrapePrivateTest() {
        String scr = ScrapeMethod.scrapePrivate();
        assertEquals(null,scr);
    }



}

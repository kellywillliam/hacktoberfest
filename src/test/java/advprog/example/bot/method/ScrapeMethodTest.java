package advprog.example.bot.method;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.jsoup.HttpStatusException;
import org.junit.Rule;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.rules.ExpectedException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;



@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class ScrapeMethodTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    void testGetDoc() {
        thrown.expect(HttpStatusException.class);
        ScrapeMethod.getDoc("https://www.livechart.me/lalalalal");
    }

    @Test
    void testShowAnime() {
        thrown.expect(HttpStatusException.class);
        thrown.expect(NullPointerException.class);
        assertEquals("404 not found",ScrapeMethod.showAnime("https://www.livechart.me/lalalalal"));
    }



}

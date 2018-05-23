package advprog.example.bot.composer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.*;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class AnimeSeasonComposerTest {
    AnimeSeasonComposer animeSeasonComposer = null;

    @BeforeEach
    void setUp(){
        animeSeasonComposer = new AnimeSeasonComposer();
    }

    @Test
    void testGetGenre() {
        animeSeasonComposer.setGenre("Drama");
        assertEquals(animeSeasonComposer.getGenre(), "Drama");
    }

    @Test
    void testContextLoads() {
        assertNotNull(animeSeasonComposer);
    }

    @Test
    void testCarouselColumn() {
        assertNotNull(animeSeasonComposer.carouselColumn());
    }

    @Test
    void testCarouselTemplate() {
        List<CarouselColumn> carouselColumnList = new ArrayList<>();
        assertNotNull(animeSeasonComposer.carouselTemplate(carouselColumnList));
    }

    @Test
    void testGetWebsite_notFound() {
        assertEquals(animeSeasonComposer.getWebsite("dummydummy"), Collections.singletonList(new TextMessage("Cannot find resource")));
    }

    @Test
    void testGetWebsite_found() throws IOException {
        Document dummy = Jsoup.connect("https://myanimelist.net/anime/season/winter/2017").get();

        assertEquals(animeSeasonComposer.getWebsite("winter/2017"), animeSeasonComposer.getDataWebsite(dummy));
    }

    @Test
    void testGetDataWebsite_notFound() throws IOException {
        Document dummy = Jsoup.connect("https://myanimelist.net/anime/season/winter/2017").get();

        assertEquals(animeSeasonComposer.getDataWebsite(dummy), Collections.singletonList(new TextMessage("Cannot find anime(s) that suit your preferred genre")));
    }

    @Test
    void testGetReplyText(){
        Map<String, String> dummy = new TreeMap<>();
        assertNotNull(animeSeasonComposer.replyText(dummy));
    }
}


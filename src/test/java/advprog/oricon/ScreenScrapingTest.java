package advprog.oricon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScreenScrapingTest {
    private ScreenScraping ss;

    @BeforeEach
    void setUp() {
        ss = new ScreenScraping();
    }

    @Test
    void weekly() {
        String date = "2018-05-14";
        assertEquals(ss.getDate(), date);
        assertEquals(ss.getParam(), "w");
        assertEquals(ss.getUrl(), "https://www.oricon.co.jp/rank/bd/w/2018-05-14/");
    }

    @Test
    void daily() {
        String date = "2018-05-10";
        assertEquals(ss.getDate(), date);
        assertEquals(ss.getParam(), "dw");
        assertEquals(ss.getUrl(), "https://www.oricon.co.jp/rank/bd/w/2018-05-10/");
    }

    @Test
    void searchWeekly() {
    }
}
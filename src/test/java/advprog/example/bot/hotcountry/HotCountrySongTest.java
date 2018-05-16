package advprog.example.bot.hotcountry;

import static org.junit.Assert.assertEquals;

import advprog.example.bot.hotcountry.HotCountrySong;
import advprog.example.bot.hotcountry.SongInfo;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class HotCountrySongTest {
    ArrayList<SongInfo> topCountry;

    @Before
    public void setUp() {
        HotCountrySong top10Chart = new HotCountrySong();
        topCountry = top10Chart.getDataFromBillboard();
    }

    @Test
    public void testIfArraySizeIs100() {
        assertEquals(topCountry.size(), 100);
    }
}

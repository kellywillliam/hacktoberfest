package advprog.example.bot.HotCountry;

import static org.junit.Assert.assertEquals;

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

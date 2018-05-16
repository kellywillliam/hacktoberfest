package advprog.example.bot.countryhot;

import static org.junit.Assert.assertEquals;

import advprog.example.bot.countryhot.HotCountrySong;
import advprog.example.bot.countryhot.SongInfo;

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
    public void testIfArraySizeIs50() {
        assertEquals(50, topCountry.size());
    }
}

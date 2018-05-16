package advprog.example.bot.countryhot;

import static org.junit.Assert.assertEquals;

import advprog.example.bot.countryhot.SongInfo;
import advprog.example.bot.countryhot.TopSong;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class TopSongTest {
    ArrayList<SongInfo> hotSong;

    @Before
    public void setUp() {
        TopSong top10Chart = new TopSong();
        hotSong = top10Chart.getDataFromBillboard();
    }

    @Test
    public void testIfArraySizeIs200() {
        assertEquals(200, hotSong.size());
    }
}

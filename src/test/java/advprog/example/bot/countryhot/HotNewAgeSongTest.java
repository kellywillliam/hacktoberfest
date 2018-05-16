package advprog.example.bot.countryhot;

import static org.junit.Assert.assertEquals;

import advprog.example.bot.countryhot.HotNewAgeSong;
import advprog.example.bot.countryhot.SongInfo;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

public class HotNewAgeSongTest {
    ArrayList<SongInfo> hotSong;

    @Before
    public void setUp() {
        HotNewAgeSong top10Chart = new HotNewAgeSong();
        hotSong = top10Chart.getDataFromBillboard();
    }

    @Test
    public void testIfArraySizeIs10() {
        assertEquals(10, hotSong.size());
    }
}

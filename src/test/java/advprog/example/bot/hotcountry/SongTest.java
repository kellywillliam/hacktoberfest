package advprog.example.bot.hotcountry;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SongTest {

    @Test
    public void testGetRowInfo() {
        SongInfo song1 = new SongInfo("Carly Simon", "If I wasn't so small", 1);
        assertEquals(song1.getSongArtist(), "Carly Simon");
        assertEquals(song1.getSongTitle(), "If I wasn't so small");
        assertEquals(song1.getRank(), 1);
    }
}


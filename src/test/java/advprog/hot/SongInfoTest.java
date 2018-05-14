package advprog.hot;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SongInfoTest {

    @Test
    public void testGetRowInfo() {
        SongInfo song1 = new SongInfo("Dudu","Lalala", 1);
        assertEquals(song1.getSongArtist(),"Dudu");
        assertEquals(song1.getSongTitle(),"Lalala");
        assertEquals(song1.getRank(),1);
    }
}
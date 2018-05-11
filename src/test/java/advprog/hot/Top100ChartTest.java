package advprog.hot;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class Top100ChartTest {
    ArrayList<SongInfo> top100;

    @Before
    public void setUp() {
        Top100Chart top100Chart = new Top100Chart();
        top100 = new ArrayList<>();
    }

    @Test
    public void testIfArraySizeIs100() {
        assertEquals(top100.size(), 100);
    }
}

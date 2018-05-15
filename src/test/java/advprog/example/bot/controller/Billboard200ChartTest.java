package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class Billboard200ChartTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Test
    void testContextLoads() {
        assertNotNull(Billboard200Chart.class);
    }

    @Test
    void testTop10Tracks() {
        assertEquals("(1) Post Malone - beerbongs & bentleys \n"
                        + "(2) Keith Urban - Graffiti U \n"
                        + "(3) J. Cole - KOD \n"
                        + "(4) Cardi B - Invasion Of Privacy \n"
                        + "(5) Soundtrack - The Greatest Showman \n"
                        + "(6) Janelle Monae - Dirty Computer \n"
                        + "(7) YoungBoy Never Broke Again - Until Death Call My Name \n"
                        + "(8) Godsmack - When Legends Rise \n"
                        + "(9) Post Malone - Stoney \n"
                        + "(10) Jason Aldean - Rearview Town",
                Billboard200Chart
                        .getInstance()
                        .top10Tracks());
    }

}

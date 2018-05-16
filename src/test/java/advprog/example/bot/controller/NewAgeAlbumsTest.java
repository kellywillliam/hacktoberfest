package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import advprog.example.bot.EventTestUtil;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)

public class NewAgeAlbumsTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private NewAgeAlbums newAgeAlbums;

    @Test
    void testContextLoads() {
        assertNotNull(newAgeAlbums);
    }

    @Test
    void testHandleTextMessageEvent() throws IOException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/newage");

        TextMessage reply = newAgeAlbums.handleTextMessageEvent(event);

        assertEquals("(1) Armik - Pacifica\n"
                + "(2) Michael S. Tyrrell - Wholetones: Life, Love & Lullabies\n"
                + "(3) Armik - Enamor\n"
                + "(4) Kevin Wood - Eternal\n"
                + "(5) Janet Stone & Nat Kendeall - Path Of Devotion (EP)\n"
                + "(6) Sigur Ros - Route One (Soundtrack)\n"
                + "(7) Marconi Union - Weightless (10 Hour Version)\n"
                + "(8) Ryan Judd - The Rest & Relaxation 4 Album Set\n"
                + "(9) Relaxing Piano Crew - Disney Dreams: Classic "
                + "Disney Piano Ballads For Sleeping\n"
                + "(10) Enigma - The Fall Of A Rebel Angel\n"
                + "\n"
                + "Thank you for using our service", reply.getText());
    }

}
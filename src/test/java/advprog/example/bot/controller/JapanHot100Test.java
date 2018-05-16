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

public class JapanHot100Test {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private JapanHot100 japanHot100;

    @Test
    void testContextLoads() {
        assertNotNull(japanHot100);
    }

    @Test
    void testHandleTextMessageEvent() throws IOException {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/japan100");

        TextMessage reply = japanHot100.handleTextMessageEvent(event);

        assertEquals("(1) HKT48 - Hayaokuri Calendar\n"
                + "(2) Nogizaka46 - Syncronicity\n"
                + "(3) SingTuyo - KISS is my life.\n"
                + "(4) Kenshi Yonezu - Lemon\n"
                + "(5) UVERworld - Odd Future\n"
                + "(6) Aiko - Straw\n"
                + "(7) Kazumasa Oda - Kono Michi Wo\n"
                + "(8) Keyakizaka46 - Glass Wo Ware!\n"
                + "(9) TWICE - What Is Love?\n"
                + "(10) Hikaru Utada - Play A Love Song\n"
                + "\n"
                + "Thank you for using our service", reply.getText());
    }



}
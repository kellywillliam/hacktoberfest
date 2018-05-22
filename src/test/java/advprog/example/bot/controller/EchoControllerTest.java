package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

import advprog.example.bot.controller.EchoController;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class EchoControllerTest {

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Autowired
    private EchoController echoController;

    @Test
    void testContextLoads() {
        assertNotNull(echoController);
    }

    @Test
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Lorem Ipsum");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("lorem ipsum", reply.getText());
    }

    @Test
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        echoController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    void testHandleTextMessageUserInputFail() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon hehehehe");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("Format yang anda masukkan salah.\n"
                + "Untuk format yang benar adalah sbb :\n"
                + "(1) /oricon jpsingles YYYY (untuk info tahunan)\n"
                + "(2) /oricon jpsingles YYYY-MM (untuk info bulanan)\n"
                + "(3) /oricon jpsingles weekly YYYY-MM-DD (untuk info mingguan ,"
                + "ps: untuk info ini hanya ada untuk tanggal yang jatuh di hari senin)\n"
                + "(4) /oricon jpsingles daily YYYY-MM-DD\n"
                + "(5) /weather (untuk informasi cuaca)\n"
                + "(6) /configure_weather (untuk mengupdate satuan\n"
                + "informasi cuaca)", reply.getText());
    }

    @Test
    void testHandleTextMessageUserInputSuccess() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon jpsingles 2017");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("(1) 願いごとの持ち腐れ - AKB48 - 2017-05-31 - Unknown\n"
                + "(2) #好きなんだ - AKB48 - 2017-08-30 - Unknown\n"
                + "(3) 11月のアンクレット - AKB48 - 2017-11-22 - Unknown\n"
                + "(4) シュートサイン - AKB48 - 2017-03-15 - Unknown\n"
                + "(5) 逃げ水 - 乃木坂46 - 2017-08-09 - Unknown\n"
                + "(6) インフルエンサー - 乃木坂46 - 2017-03-22 - Unknown\n"
                + "(7) いつかできるから今日できる - 乃木坂46 - 2017-10-11 - Unknown\n"
                + "(8) 不協和音 - 欅坂46 - 2017-04-05 - Unknown\n"
                + "(9) 風に吹かれても - 欅坂46 - 2017-10-25 - Unknown\n"
                + "(10) Doors 〜勇気の軌跡〜 - 嵐 - 2017-11-08 - Unknown",
                reply.getText());
    }
}
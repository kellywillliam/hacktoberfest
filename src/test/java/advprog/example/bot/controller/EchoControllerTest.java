package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import advprog.example.bot.EventTestUtil;

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
    void testHandleDefaultMessage() {
        Event event = mock(Event.class);

        echoController.handleDefaultMessage(event);

        verify(event, atLeastOnce()).getSource();
        verify(event, atLeastOnce()).getTimestamp();
    }

    @Test
    void testHandleTextMessageEventInputWithoutFormat() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("Sample text message");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("Halo, terima kasih atas pesan yang dikirimkan. \n"
                + "Untuk menggunakan bot ini, silakkan kirimkan pesan dengan format "
                + "'/oricon bluray [weekly/daily] [YYYY-MM-DD]' \n"
                + "Contoh: /oricon bluray weekly 2018-05-14", reply.getText());
    }

    @Test
    void testHandleTextMessageEventInputFail() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon bluray weekly");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("Pesan yang kamu kirimkan belum sesuai format. "
                + "Pastikan format yang kamu kirimkan sudah lengkap.", reply.getText());
    }

    @Test
    void testHandleTextMessageEventInputWeeklyDaily() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon bluray weekl 2018-05-14");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("Pesan yang kamu kirimkan belum sesuai format. "
                + "Pastikan kamu menuliskan 'weekly' atau 'daily' dengan benar.", reply.getText());
    }

    @Test
    void testHandleTextMessageUserInputSuccess() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon bluray weekly 2018-05-14");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("(1) スター・ウォーズ/最後のジェダイ MovieNEX(初回版) - 推定売上枚数：29,846枚 - 2018-04-25 \n"
                + "(2) ヴァイオレット・エヴァーガーデン&#xfffd;A - 推定売上枚数：5,026枚 - 2018-05-02 \n"
                + "(3) オリエント急行殺人事件 2枚組ブルーレイ&amp;DVD - 推定売上枚数：4,162枚 - 2018-05-02 \n"
                + "(4) 斉木楠雄のΨ難 豪華版ブルーレイ&amp;DVDセット【初回生産限定】 - 推定売上枚数：3,817枚 - 2018-05-02 \n"
                + "(5) ラブライブ!サンシャイン!! 2nd Season 5【特装限定版】 - 推定売上枚数：3,292枚 - 2018-04-24 \n"
                + "(6) ミックス。 豪華版Blu-ray - 推定売上枚数：3,063枚 - 2018-05-02 \n"
                + "(7) GREEN MIND AT BUDOKAN - 推定売上枚数：2,523枚 - 2018-05-02 \n"
                + "(8) THE IDOLM@STER SideM GREETING TOUR 2017 〜BEYOND THE DREAM〜 LIVE Blu-ray - "
                + "推定売上枚数：2,292枚 - 2018-04-25 \n"
                + "(9) SHOGO HAMADA ON THE ROAD 2015-2016“Journey of a Songwriter” - 推定売上枚数：2,118枚 "
                + "- 2018-04-25 \n"
                + "(10) ラブライブ!サンシャイン!! Aqours 2nd LoveLive! HAPPY PARTY TRAIN TOUR Blu-ray "
                + "Memorial BOX - 推定売上枚数：2,033枚 - 2018-04-25 ", reply.getText());
    }

}
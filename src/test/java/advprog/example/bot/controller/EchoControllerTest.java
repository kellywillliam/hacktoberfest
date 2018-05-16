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
    void testHandleTextMessageEvent() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/echo Lorem Ipsum");

        TextMessage reply = echoController.handleTextMessageEvent(event);

        assertEquals("Lorem Ipsum", reply.getText());
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

        TextMessage reply = echoController.handleTextMessageEventNew(event);

        assertEquals("Halo, terima kasih atas pesan yang dikirimkan. \n"
                + "Untuk menggunakan bot ini, silakkan kirimkan pesan dengan format "
                + "'/oricon bluray [weekly/daily] [YYYY-MM-DD]' \n"
                + "Contoh: /oricon bluray weekly 2018-05-14", reply.getText());
    }

    @Test
    void testHandleTextMessageEventInputFail() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon bluray weekly");

        TextMessage reply = echoController.handleTextMessageEventNew(event);

        assertEquals("Pesan yang kamu kirimkan belum sesuai format. "
                + "Pastikan format yang kamu kirimkan sudah lengkap.", reply.getText());
    }

    @Test
    void testHandleTextMessageEventInputWeeklyDaily() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon bluray weekl 2018-05-14");

        TextMessage reply = echoController.handleTextMessageEventNew(event);

        assertEquals("Pesan yang kamu kirimkan belum sesuai format. "
                + "Pastikan kamu menuliskan 'weekly' atau 'daily' dengan benar.", reply.getText());
    }

    @Test
    void testHandleTextMessageUserInputSuccess() {
        MessageEvent<TextMessageContent> event =
                EventTestUtil.createDummyTextMessage("/oricon bluray weekly 2018-04-16");

        TextMessage reply = echoController.handleTextMessageEventNew(event);

        assertEquals("(1) 舞台『刀剣乱舞』ジョ伝 三つら星刀語り - マーベラス - 2018-04-04 \n"
                + "(2) がらくたライブ - ビクターエンタテインメント - 2018-04-04 \n"
                + "(3) 茅ヶ崎物語 〜MY LITTLE HOMETOWN〜 - アミューズソフト - 2018-04-04 \n"
                + "(4) キングスマン:ゴールデン・サークル 2枚組ブルーレイ&amp;DVD - "
                + "20世紀フォックス ホーム エンターテイメント - 2018-04-06 \n"
                + "(5) 関西ジャニーズJr.のお笑いスター誕生! 豪華版(初回限定生産) - 松竹ホームビデオ - 2018-04-04 \n"
                + "(6) Inori Minase 1st LIVE Ready Steady Go! - キングレコード - 2018-04-04 \n"
                + "(7) キングスマン:ゴールデン・サークル ブルーレイ プレミアム・エディション"
                + "(4K ULTRA HD付)〔数量限定生産〕 - 20世紀フォックス ホーム エンターテイメント - 2018-04-06 \n"
                + "(8) ヴァイオレット・エヴァーガーデン&#xfffd;@ - ポニーキャニオン - 2018-04-04 \n"
                + "(9) アトミック・ブロンド - ハピネット - 2018-04-03 \n"
                + "(10) 【BD】2.5次元ダンスライブ「ツキウタ。」ステージ 第4幕『Lunatic Party』通常版 "
                + "- ムービック - 2018-04-06 ", reply.getText());
    }

}
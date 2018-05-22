package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest(properties = "line.bot.handler.enabled=false")
@ExtendWith(SpringExtension.class)
public class TopChartControllerTest {

    TopChartController chart = new TopChartController();

    static {
        System.setProperty("line.bot.channelSecret", "SECRET");
        System.setProperty("line.bot.channelToken", "TOKEN");
    }

    @Test
    void testContextLoads() {
        assertNotNull(TopChartController.class);
    }

    @Test
    void topChartWeeklyTest() {
        assertEquals(chart.topChartWeekly(
                "2018-05-07"),
                "(1) シンクロニシティ - 乃木坂46 - 2018-04-25 - Unknown\n"
                + "(2) Fandango - THE RAMPAGE from EXILE TRIBE - 2018-04-25 - Unknown\n"
                + "(3) Fiction e.p - sumika - 2018-04-25 - Unknown\n"
                + "(4) Bumblebee - Lead - 2018-04-25 - Unknown\n"
                + "(5) 人間を被る - DIR EN GREY - 2018-04-25 - Unknown\n"
                + "(6) 泣きたいくらい - 大原櫻子 - 2018-04-25 - Unknown\n"
                + "(7) THE IDOLM@STER MILLION THE@TER GENERATION 07 トゥインクルリズム(ZETTAI × BREAK!!"
                + " トゥインクルリズム) - トゥインクルリズム[中谷育(原嶋あかり),"
                + "七尾百合子(伊藤美来),松田亜利沙(村川梨衣)] - 2018-04-25 - Unknown\n"
                + "(8) 春はどこから来るのか? - NGT48 - 2018-04-11 - Unknown\n"
                + "(9) Ask Yourself - KAT-TUN - 2018-04-18 - Unknown\n"
                + "(10) 鍵穴 - the Raid. - 2018-04-25 - Unknown");
    }

    @Test
    void topChartMonthlyTest() {
        assertEquals(chart.topChartMonthly("2018","04"),
                "(1) シンクロニシティ - 乃木坂46 - 2018-04-25 - 1,214,510\n"
                        + "(2) 早送りカレンダー - HKT48 - 2018-05-02 - 165,176\n"
                        + "(3) Ask Yourself - KAT-TUN - 2018-04-18 - 149,081\n"
                        + "(4) 春はどこから来るのか? - NGT48 - 2018-04-11 - 128,565\n"
                        + "(5) 君のAchoo! - ラストアイドル(シュークリームロケッツ)"
                        + " - 2018-04-18 - 58,198\n"
                        + "(6) SEXY SEXY/泣いていいよ/Vivid Midnight - Juice=Juice - "
                        + "2018-04-18 - 54,728\n"
                        + "(7) ガラスを割れ! - 欅坂46 - 2018-03-07 - 54,131\n"
                        + "(8) ONE TIMES ONE - コブクロ - 2018-04-11 - 39,395\n"
                        + "(9) ODD FUTURE - UVERworld - 2018-05-02 - 37,347\n"
                        + "(10) Shanana ここにおいで - B2takes! - 2018-04-11 - 36,455");
    }

    @Test
    void topChartDailyTest() {
        assertEquals(chart.topChartDaily("2018-05-16"),
                "(1) Wake Me Up - TWICE - 2018-05-16 - 56,097\n"
                        + "(2) BE IN SIGHT - 刀剣男士 formation of つはもの - 2018-05-16 - 6,650\n"
                        + "(3) 手遅れcaution - =LOVE - 2018-05-16 - 4,342\n"
                        + "(4) げんきいっぱい(鬼POP激キャッチー最強ハイパーウルトラミュージック)"
                        + " - ヤバイTシャツ屋さん - 2018-05-16 - Unknown\n"
                        + "(5) この道を/会いに行く/坂道を上って/小さな風景 - 小田和正 - 2018-05-02 - Unknown\n"
                        + "(6) シンクロニシティ - 乃木坂46 - 2018-04-25 - Unknown\n"
                        + "(7) divine criminal - fripSide - 2018-05-16 - Unknown\n"
                        + "(8) 恋はシュミシュミ - 郷ひろみ - 2018-05-16 - Unknown\n"
                        + "(9) A or A!? - petit milady - 2018-05-16 - Unknown\n"
                        + "(10) Lemon - 米津玄師 - 2018-03-14 - Unknown");
    }

    @Test
    void topChartYearTest() {
        assertEquals(chart.topChartYear("2017"),
                "(1) 願いごとの持ち腐れ - AKB48 - 2017-05-31 - Unknown\n"
                + "(2) #好きなんだ - AKB48 - 2017-08-30 - Unknown\n"
                + "(3) 11月のアンクレット - AKB48 - 2017-11-22 - Unknown\n"
                + "(4) シュートサイン - AKB48 - 2017-03-15 - Unknown\n"
                + "(5) 逃げ水 - 乃木坂46 - 2017-08-09 - Unknown\n"
                + "(6) インフルエンサー - 乃木坂46 - 2017-03-22 - Unknown\n"
                + "(7) いつかできるから今日できる - 乃木坂46 - 2017-10-11 - Unknown\n"
                + "(8) 不協和音 - 欅坂46 - 2017-04-05 - Unknown\n"
                + "(9) 風に吹かれても - 欅坂46 - 2017-10-25 - Unknown\n"
                + "(10) Doors 〜勇気の軌跡〜 - 嵐 - 2017-11-08 - Unknown");
    }
}
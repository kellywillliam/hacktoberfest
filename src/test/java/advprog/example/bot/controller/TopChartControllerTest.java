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
        assertEquals(chart.topChartDaily("2018-05-12"),
                "(1) 進化理論 - BOYS AND MEN - 2018-05-09 - 4,182\n"
                        + "(2) 泣けないぜ…共感詐欺/Uraha=Lover/君だけじゃないさ...friends"
                        + "(2018アコースティックVer.)"
                        + " - アンジュルム - 2018-05-09 - 3,246\n"
                        + "(3) この道を/会いに行く/坂道を上って/小さな風景 - 小田和正 - 2018-05-02 - 2,491\n"
                        + "(4) シンクロニシティ - 乃木坂46 - 2018-04-25 - Unknown\n"
                        + "(5) WE/GO - さとり少年団 - 2018-05-09 - Unknown\n"
                        + "(6) 誓い - 雨宮天 - 2018-05-09 - Unknown\n"
                        + "(7) Eclipse - 蒼井翔太 - 2018-05-09 - Unknown\n"
                        + "(8) 無敵のビーナス - ばってん少女隊 - 2018-05-09 - Unknown\n"
                        + "(9) Bumblebee - Lead - 2018-04-25 - Unknown\n"
                        + "(10) THE IDOLM@STER SideM WORLD TRE@SURE 01"
                        + "(永遠(とわ)なる四銃士)"
                        + " - 天道輝(仲村宗悟),葛之葉雨彦(笠間淳),握野英雄(熊谷健太郎),"
                        + "紅井朱雀(益山武明) - 2018-05-09 - Unknown");
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
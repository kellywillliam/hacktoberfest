package advprog.oricon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScreenScrapingTest {
    private ScreenScraping ss;

    @BeforeEach
    void setUp() {
        ss = new ScreenScraping();
    }

    @Test
    void weekly() {
        String date = "2018-05-14";
        ss.weekly(date);
        assertEquals(date, ss.getDate());
        assertEquals("w", ss.getParam());
        assertEquals("https://www.oricon.co.jp/rank/bd/w/2018-05-14/", ss.getUrl());
    }

    @Test
    void daily() {
        String date = "2018-05-10";
        ss.daily(date);
        assertEquals(date, ss.getDate());
        assertEquals("d", ss.getParam());
        assertEquals("https://www.oricon.co.jp/rank/bd/d/2018-05-10/", ss.getUrl());
    }

    @Test
    void search() {
        ss.setDate("2018-05-14");
        ss.setParam("w");
        ss.setUrl("https://www.oricon.co.jp/rank/bd/w/2018-05-14/");
        assertEquals("(1) スター・ウォーズ/最後のジェダイ MovieNEX(初回版) - 推定売上枚数：29,846枚 - 2018-04-25 \n" +
                "(2) ヴァイオレット・エヴァーガーデン&#xfffd;A - 推定売上枚数：5,026枚 - 2018-05-02 \n" +
                "(3) オリエント急行殺人事件 2枚組ブルーレイ&amp;DVD - 推定売上枚数：4,162枚 - 2018-05-02 \n" +
                "(4) 斉木楠雄のΨ難 豪華版ブルーレイ&amp;DVDセット【初回生産限定】 - 推定売上枚数：3,817枚 - 2018-05-02 \n" +
                "(5) ラブライブ!サンシャイン!! 2nd Season 5【特装限定版】 - 推定売上枚数：3,292枚 - 2018-04-24 \n" +
                "(6) ミックス。 豪華版Blu-ray - 推定売上枚数：3,063枚 - 2018-05-02 \n" +
                "(7) GREEN MIND AT BUDOKAN - 推定売上枚数：2,523枚 - 2018-05-02 \n" +
                "(8) THE IDOLM@STER SideM GREETING TOUR 2017 〜BEYOND THE DREAM〜 LIVE Blu-ray - 推定売上枚数：2,292枚 - 2018-04-25 \n" +
                "(9) SHOGO HAMADA ON THE ROAD 2015-2016“Journey of a Songwriter” - 推定売上枚数：2,118枚 - 2018-04-25 \n" +
                "(10) ラブライブ!サンシャイン!! Aqours 2nd LoveLive! HAPPY PARTY TRAIN TOUR Blu-ray Memorial BOX - 推定売上枚数：2,033枚 - 2018-04-25 ",
                ss.search());
    }
}
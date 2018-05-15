package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OriconRankTest {
    private OriconRank or;

    @BeforeEach
    void setUp() {
        or = new OriconRank();
    }

    @Test
    void weekly() {
        or.setDate("2018-05-14");
        or.weekly();
        assertEquals("2018-05-14", or.getDate());
        assertEquals("https://www.oricon.co.jp/rank/bd/w/2018-05-14/", or.getUrl());
    }

    @Test
    void daily() {
        or.setDate("2018-05-10");
        or.daily();
        assertEquals("2018-05-10", or.getDate());
        assertEquals("https://www.oricon.co.jp/rank/bd/d/2018-05-10/", or.getUrl());
    }

    @Test
    void search() {
        or.setDate("2018-05-14");
        or.setUrl("https://www.oricon.co.jp/rank/bd/w/2018-05-14/");
        assertEquals("(1) スター・ウォーズ/最後のジェダイ MovieNEX(初回版) - 推定売上枚数：29,846枚 - 2018-04-25 \n"
                        + "(2) ヴァイオレット・エヴァーガーデン&#xfffd;A - 推定売上枚数：5,026枚 - 2018-05-02 \n"
                        + "(3) オリエント急行殺人事件 2枚組ブルーレイ&amp;DVD - 推定売上枚数：4,162枚 - 2018-05-02 \n"
                        + "(4) 斉木楠雄のΨ難 豪華版ブルーレイ&amp;DVDセット【初回生産限定】 - 推定売上枚数：3,817枚 - 2018-05-02 \n"
                        + "(5) ラブライブ!サンシャイン!! 2nd Season 5【特装限定版】 - 推定売上枚数：3,292枚 - 2018-04-24 \n"
                        + "(6) ミックス。 豪華版Blu-ray - 推定売上枚数：3,063枚 - 2018-05-02 \n"
                        + "(7) GREEN MIND AT BUDOKAN - 推定売上枚数：2,523枚 - 2018-05-02 \n"
                        + "(8) THE IDOLM@STER SideM GREETING TOUR 2017 〜BEYOND THE DREAM〜 "
                        + "LIVE Blu-ray - 推定売上枚数：2,292枚 - 2018-04-25 \n"
                        + "(9) SHOGO HAMADA ON THE ROAD 2015-2016“Journey of a Songwriter” - "
                        + "推定売上枚数：2,118枚 - 2018-04-25 \n"
                        + "(10) ラブライブ!サンシャイン!! Aqours 2nd LoveLive! HAPPY PARTY TRAIN TOUR Blu-ray "
                        + "Memorial BOX - 推定売上枚数：2,033枚 - 2018-04-25 ",
                or.search());
    }

    @Test
    void searchNoResult() {
        or.setDate("2018-05-10");
        or.setUrl("https://www.oricon.co.jp/rank/bd/w/2018-05-10/");
        assertEquals("Tidak ada data yang ditemukan.", or.search());
    }

    @Test
    void execute() {
        assertEquals("Tanggal yang kamu masukkan tidak valid. Silakkan coba lagi.",
                or.execute("/oricon bluray daily 2018-02-31"));
        assertEquals("Halo, terima kasih atas pesan yang dikirimkan. \n"
                        + "Untuk menggunakan bot ini, silakkan kirimkan pesan dengan format "
                        + "'/oricon bluray [weekly/daily] [YYYY-MM-DD]' \n"
                        + "Contoh: /oricon bluray weekly 2018-05-14",
                or.execute("Sample text message"));
        assertEquals("(1) 劇場版「Fate/stay night[Heaven’s Feel]&#xfffd;T.presage flower」(完全生産限定版) "
                        + "- アニプレックス - 2018-05-09 \n"
                        + "(2) スター・ウォーズ/最後のジェダイ MovieNEX(初回版) - ウォルト・ディズニー・ジャパン - "
                        + "2018-04-25 \n"
                        + "(3) ナラタージュ Blu-ray 豪華版 - アスミック・エース - 2018-05-09 \n"
                        + "(4) 劇場版「Fate/stay night[Heaven’s Feel]&#xfffd;T.presage flower」(通常版) - "
                        + "アニプレックス - 2018-05-09 \n"
                        + "(5) 仮面ライダー平成ジェネレーションズFINAL ビルド&amp;エグゼイドwithレジェンドライダー "
                        + "コレクターズパック - 東映ビデオ - 2018-05-09 \n"
                        + "(6) Little Glee Monster Arena Tour 2018 -juice !!!!!- "
                        + "at YOKOHAMA ARENA - ソニー・ミュージックレコーズ - 2018-05-09 \n"
                        + "(7) カードキャプターさくら クリアカード編 Vol.1＜初回仕様版＞ - ワーナー・ブラザース "
                        + "ホームエンターテイメント - 2018-05-09 \n"
                        + "(8) 鈴村健一 10th Anniversary Live“lo-op”BD - ランティス - 2018-05-09 \n"
                        + "(9) ガーディアンズ・オブ・ギャラクシー MovieNEX - ウォルト・ディズニー・ジャパン - "
                        + "2015-01-21 \n"
                        + "(10) ワイルド・スピード ICE BREAK - NBCユニバーサル・エンターテイメントジャパン - 2018-05-09 ",
                or.execute("/oricon bluray daily 2018-05-10"));
    }

    @Test
    void isValidDate() {
        assertEquals(false, or.isValidDate("2018-02-31"));
    }
}
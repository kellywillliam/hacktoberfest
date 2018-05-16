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
        or.setUrl("https://www.oricon.co.jp/rank/bd/w/2018-04-16/");
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
                        + "- ムービック - 2018-04-06 ",
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
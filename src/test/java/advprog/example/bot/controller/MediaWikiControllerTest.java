package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MediaWikiControllerTest {
    private MediaWikiController mwc;

    @BeforeEach
    void setUp() {
        mwc = new MediaWikiController();
        mwc.setUrlList("./wikiUrlTest.csv");
    }

    @Test
    void execute() {
        assertEquals("Halo, terima kasih atas pesan yang dikirimkan. "
                        + "Untuk menggunakan bot ini, ada dua perintah yang bisa dilakukan:\n"
                        + "1. /add_wiki [URL-API-endpoint]\n"
                        + "Contoh: /add_wiki http://marvel.wikia.com/api.php\n"
                        + "2. /random_wiki_article",
                mwc.execute("Semoga kuis statprob saya lancar"));
        assertEquals("Pesan yang kamu kirimkan belum sesuai format. "
                + "Pastikan format yang kamu kirimkan sudah lengkap.",
                mwc.execute("/add_wiki"));
        assertEquals("URL yang kamu masukkan tidak valid. Silakan coba lagi.",
                mwc.execute("/add_wiki bla.com/api.php"));
        assertEquals("URL berhasil ditambahkan.",
                mwc.execute("/add_wiki marvel.wikia.com/api.php"));
        assertEquals("URL yang kamu masukkan sudah terdaftar.",
                mwc.execute("/add_wiki marvel.wikia.com/api.php"));

        try {
            PrintWriter writer = null;
            writer = new PrintWriter(mwc.getUrlList());
            writer.print("wikiUrl\n");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isValidUrl() {
        assertEquals(true, mwc.isValidUrl("marvel.wikia.com/api.php"));
    }
}
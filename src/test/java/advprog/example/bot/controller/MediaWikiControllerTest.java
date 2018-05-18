package advprog.example.bot.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MediaWikiControllerTest {
    private MediaWikiController mwc;

    @BeforeEach
    void setUp() {
        mwc = new MediaWikiController();
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
        assertEquals("URL berhasil ditambahkan.",
                mwc.execute("/add_wiki http://example.com/wiki/api.php"));
        assertEquals("URL berhasil ditambahkan.",
                mwc.execute("/add_wiki http://examplee.com/wiki/api.php"));
    }

    @Test
    void isValidUrl() {
        assertEquals(true, mwc.isValidUrl("http://marvel.wikia.com/api.php"));
    }
}
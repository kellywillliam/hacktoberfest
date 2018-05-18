package advprog.example.bot.controller;

public class MediaWikiController {
    String randomInput;
    String notEnoughInput;
    String addWikiSuccess;
    String getAddWikiFail;

    public MediaWikiController() {
        this.randomInput = "Halo, terima kasih atas pesan yang dikirimkan. "
                + "Untuk menggunakan bot ini, ada dua perintah yang bisa dilakukan:\n"
                + "1. /add_wiki [URL-API-endpoint]\n"
                + "Contoh: /add_wiki http://marvel.wikia.com/api.php\n"
                + "2. /random_wiki_article";
        this.notEnoughInput = "Pesan yang kamu kirimkan belum sesuai format. "
                + "Pastikan format yang kamu kirimkan sudah lengkap.";
        this.addWikiSuccess = "URL berhasil ditambahkan";
        this.getAddWikiFail = "URL yang kamu masukkan tidak valid. Silakan coba lagi.";
    }

    public String execute(String contentText) {
        String[] command = contentText.split(" ");
        String replyText = "";

        if (command[0].equals("/add_wiki")) {
            try {
                String urlGiven = command[1];
                if (isValidUrl(urlGiven)) {
                    saveUrl(urlGiven);
                    replyText = addWikiSuccess;
                } else {
                    replyText = getAddWikiFail;
                }

            } catch (ArrayIndexOutOfBoundsException e) {
                replyText = notEnoughInput;
            }

        } else if (command[0].equals("/random_wiki_article")) {
            replyText = "test";
        } else {
            replyText = randomInput;
        }

        return replyText;
    }

    public boolean isValidUrl(String urlGiven) {
        if (urlGiven.length() % 2 == 0) {
            return true;
        } else {
            return false;
        }
    }

    public void saveUrl(String url) {

    }
}

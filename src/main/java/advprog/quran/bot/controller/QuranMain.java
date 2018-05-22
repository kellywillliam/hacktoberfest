package advprog.quran.bot.controller;

import com.linecorp.bot.model.action.MessageAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;

import java.util.Arrays;
import java.util.StringTokenizer;

public class QuranMain {

    private JsonParser quranDatabase;

    private boolean isForGroupChat;
    private String savedSuratNo;
    private MessageEvent<TextMessageContent> currentEvent;

    public QuranMain() {
        quranDatabase = new JsonParser();
    }

    public Message displayCarousel() {
        String imageUrl = "https://i1.wp.com/khazanahalquran.com/wp-content/uploads/2015/12/baca-alquran.jpg";
        CarouselTemplate carouselTemplate = new CarouselTemplate(
                Arrays.asList(
                        new CarouselColumn(imageUrl, "Daftar surat tersedia",
                                "Pilih surat yang tersedia di database", Arrays.asList(
                                new MessageAction("An-Nas ayat 1-6",
                                        "An-Nas"),
                                new MessageAction("Al-Falaq ayat 1-5",
                                        "Al-Falaq")
                        )),
                        new CarouselColumn(imageUrl, "Daftar surat tersedia",
                                "Pilih surat yang tersedia di database", Arrays.asList(
                                new MessageAction("Al-Fil ayat 1-5",
                                        "Al-Fil"),
                                new MessageAction("Al-Lahab ayat 1-5",
                                        "Al-Lahab")
                        )),
                        new CarouselColumn(imageUrl, "Daftar surat tersedia",
                                "Pilih surat yang tersedia di database", Arrays.asList(
                                new MessageAction("Al-Kafirun ayat 1-6",
                                        "Al-Kafirun"),
                                new MessageAction("Al-Ma'un ayat 1-7",
                                        "Al-Ma'un")
                        ))
                ));
        TemplateMessage templateMessage = new TemplateMessage("Pilihan surat:",
                carouselTemplate);

        return templateMessage;
    }

    public Message reply(MessageEvent<TextMessageContent> event) {

        currentEvent = event;

        String message = event.getMessage().getText();
        StringTokenizer st = new StringTokenizer(message);
        Message reply = null;

        if (QuranController.isWaitingForReply) {
            reply = proceedWithPreviousRequest(message);
        } else if (event.getSource() instanceof UserSource && st.nextToken().equals("/qs")) {
            reply = pcReply(message);
        } else if (message.toLowerCase().contains("ngaji")) {
            reply = groupReply();
        }

        return reply;
    }

    private Message proceedWithPreviousRequest(String userReply) {
        String reply = null;

        if (isForGroupChat) {
            reply = checkAnswer(userReply);
        } else if (savedSuratNo == null) {
            reply = saveSurat(userReply);
        } else {
            reply = displayAyat(savedSuratNo, userReply);
            savedSuratNo = null;
            QuranController.isWaitingForReply = false;
        }

        return new TextMessage(reply);
    }

    private String saveSurat(String suratToBeSaved) {

        String reply = "";
        String suratNo = quranDatabase.getSuratNoByName(suratToBeSaved);

        if (suratNo.equals("")) {
            reply = "Surat '" + suratToBeSaved + "' tidak tersedia";
            QuranController.isWaitingForReply = false;
        } else {
            savedSuratNo = suratNo;
            reply = "Tolong pilih ayat dari surat " + suratToBeSaved;
        }

        return reply;
    }



    public Message pcReply(String inputCommand) {

        StringTokenizer st = new StringTokenizer(inputCommand);
        st.nextToken();
        String argument = null;
        if (st.hasMoreTokens()) {
            argument = st.nextToken(" ");
        }

        Message reply = null;

        if (argument == null) {
            reply = displayCarousel();
            isForGroupChat = false;
            QuranController.isWaitingForReply = true;
            QuranController.currentFitur = this;
        } else if (argument.matches("\\d+:\\d+")) {
            String[] chapterAndVerse = argument.split(":");
            reply = new TextMessage(displayAyat(chapterAndVerse[0], chapterAndVerse[1]));
            QuranController.isWaitingForReply = false;
            savedSuratNo = null;
        } else {
            reply = new TextMessage("To display Quran verse please use format "
                    + "'/qs ChapterNumber:VerseNumber' ex: '/qs 144:2'");
            QuranController.isWaitingForReply = false;
            savedSuratNo = null;
        }

        return reply;
    }

    public String displayAyat(String noSurat, String noAyat) {
        return quranDatabase.getAyatFromDatabase(noSurat, noAyat);
    }

    public Message groupReply() {

        isForGroupChat = true;

        savedSuratNo = quranDatabase.getRandomSuratNo();
        System.out.println("suratno " + savedSuratNo);
        String reply = quranDatabase.getRandomAyat(savedSuratNo);

        QuranController.isWaitingForReply = true;
        QuranController.currentFitur = this;

        return new TextMessage(reply);
    }

    private String checkAnswer(String answer) {
        String reply = "Jawaban salah!";

        if (savedSuratNo.toLowerCase().equals(
                quranDatabase.getSuratNoByName(answer).toLowerCase())) {
            reply = "Jawaban anda benar!";
        }

        QuranController.isWaitingForReply = false;
        savedSuratNo = null;
        return reply;
    }
}


package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;


@LineMessageHandler
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    private boolean flag = false;

    TopChartController eventHandler = new TopChartController();
    WeatherController eventController = new WeatherController();

    String errorMessage = "Format yang anda masukkan salah.\n"
            + "Untuk format yang benar adalah sbb :\n"
            + "(1) /oricon jpsingles YYYY (untuk info tahunan)\n"
            + "(2) /oricon jpsingles YYYY-MM (untuk info bulanan)\n"
            + "(3) /oricon jpsingles weekly YYYY-MM-DD (untuk info mingguan ,"
            + "ps: untuk info ini hanya ada untuk tanggal yang jatuh di hari senin)\n"
            + "(4) /oricon jpsingles daily YYYY-MM-DD"
            + "(5) /weather (untuk informasi cuaca)"
            + "(6) /configure_weather (untuk mengupdate satuan informasi cuaca)";

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText().toLowerCase();


        String[] temp = contentText.split(" ");
        String replyText;

        if (temp[0].equalsIgnoreCase("/oricon")
                && temp[1].equalsIgnoreCase("jpsingles")) {
            if (temp.length > 3) {
                if (temp[2].equalsIgnoreCase("weekly")) {
                    String tanggal = temp[3];
                    replyText = eventHandler.topChartWeekly(tanggal);
                } else if (temp[2].equalsIgnoreCase("daily")) {
                    String tanggal = temp[3];
                    replyText = eventHandler.topChartDaily(tanggal);
                } else {
                    replyText = errorMessage;
                }
            } else {
                String[] tempTanggal = temp[2].split("-");
                if (tempTanggal.length > 1) {
                    replyText = eventHandler.topChartMonthly(tempTanggal[0], tempTanggal[1]);
                } else if (tempTanggal.length == 1) {
                    replyText = eventHandler.topChartYear(tempTanggal[0]);
                } else {
                    replyText = errorMessage;
                }
            }

        } else if (temp[0].equalsIgnoreCase("/weather")) {
            replyText = "Silahkan kirim lokasi kamu agar Sana"
                    + " dapat memberitahu kamu kondisi cuaca ditempat kamu";
            flag = true;

        } else if (temp[0].equalsIgnoreCase("/configure_weather")) {
            replyText = "Kamu ingin ganti satuan suhu dan satuan kecepatan angin ?"
                        + ", Sana bisa membantu kamu dengan mengetik opsi berikut :\n"
                        + "(1) /configure STANDARD (untuk suhu Kelvin dan kecepatan angin Meter/sec\n"
                        + "(2) /configure METRIC (untuk suhu Celcius dan kecepatan angin Meter/sec\n"
                        + "(3) /configure IMPERIAL (untuk suhu Fahrenheit dan kecepatan Miles/hour"
                        + "contoh jika kamu ingin suhunya Celcius dan kecepatan angin Meter/sec "
                        + "maka kamu cukup mengetik : /configure METRIC";

            return new TextMessage(replyText);

        } else if (temp[0].equalsIgnoreCase("/configure")) {
            String userId = content.getId();
            String tipe = temp[1];
            if(tipe.equalsIgnoreCase("STANDARD")) {
                eventController.updateUserConfig(userId,tipe);
                replyText = "Konfigurasi data kamu sudah di-update YEAY !";
            } else if(tipe.equalsIgnoreCase("METRIC")) {
                eventController.updateUserConfig(userId,tipe);
                replyText = "Konfigurasi data kamu sudah di-update YEAY !";
            } else if(tipe.equalsIgnoreCase("IMPERIAL")) {
                eventController.updateUserConfig(userId,tipe);
                replyText = "Konfigurasi data kamu sudah di-update YEAY !";
            } else {
                replyText = "Opsi yang kamu pilih tidak tersedia :( ";
            }

            return new TextMessage(replyText);

        } else if (temp[0].equalsIgnoreCase("/echo")) {
            replyText = contentText.replace("/echo", "");
            return new TextMessage(replyText.substring(1));

        } else {
            replyText = errorMessage;
            return new TextMessage(replyText);
        }

        return new TextMessage(replyText);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    @EventMapping
    public TextMessage handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LOGGER.fine(String.format("LocationMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));


        if(flag) {
            String replyText;

            LocationMessageContent content = event.getMessage();
            String longitude = Double.toString(content.getLongitude());
            String latitude = Double.toString(content.getLatitude());
            String userId = content.getId();

            String dataInformation = longitude + ';' + latitude;

            replyText = eventController.getData(dataInformation, userId);

            flag = false;

            return new TextMessage(replyText + "\n longitude :" + longitude + "\n latitude :" + latitude
                    + "\n Id User: " + userId);
        }

        return new TextMessage("Info yang kamu masukkan salah");

    }
}

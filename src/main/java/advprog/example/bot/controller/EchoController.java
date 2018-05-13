package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.logging.Logger;


@LineMessageHandler
public class EchoController {

    private static final Logger LOGGER = Logger.getLogger(EchoController.class.getName());

    TopChartController eventHandler = new TopChartController();

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText().toLowerCase();


        String[] temp = contentText.split(" ");
        String replyText;

        if (temp[0].equalsIgnoreCase("/oricon") &&
                temp[1].equalsIgnoreCase("jpsingles")) {
            if  (temp.length > 3) {
                if(temp[2].equalsIgnoreCase("weekly")) {
                    String tanggal = temp[3];
                    replyText = eventHandler.topChartWeekly(tanggal);
                } else if (temp[2].equalsIgnoreCase("daily")) {
                    String tanggal = temp[3];
                    replyText = eventHandler.topChartDaily(tanggal);
                } else {
                    replyText = ("Format yang anda masukkan salah");
                }
            } else {
                String[] tempTanggal = temp[2].split("-");
                if (tempTanggal.length > 1) {
                    replyText = eventHandler.topChartMonthly(tempTanggal[0],tempTanggal[1]);
                } else if (tempTanggal.length == 1) {
                    replyText = eventHandler.topChartYear(tempTanggal[0]);
                } else {
                    replyText = ("Format yang anda masukkan salah");
                }
            }
        } else if (temp[0].equalsIgnoreCase("/echo")) {
            replyText = contentText.replace("/echo", "");
            return new TextMessage(replyText.substring(1));
        } else {
            replyText = ("Format yang anda masukkan salah");
            return new TextMessage(replyText);
        }

       return new TextMessage(replyText);
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }
}

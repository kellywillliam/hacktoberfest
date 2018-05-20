package advprog.example.bot.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import javax.xml.soap.Text;
import java.util.*;
import java.util.logging.Logger;

@LineMessageHandler
public class DiceController {
//    public static void main(String[] args) {
//        SortedSet<String> sample = new TreeSet<String>(Arrays.asList("head", "tail"));
//        System.out.println(sample.contains("head"));
//    }

    private static final Logger LOGGER = Logger.getLogger(DiceController.class.getName());

    @EventMapping
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        LOGGER.fine(String.format("TextMessageContent(timestamp='%s',content='%s')",
                event.getTimestamp(), event.getMessage()));
        TextMessageContent content = event.getMessage();
        String contentText = content.getText();
        String contentTextArr[] = contentText.split(" ");

        if (contentText.equalsIgnoreCase("/coin")) {
            String result = coinRolling();
            return new TextMessage(result);
        }

        if (contentTextArr[0].equalsIgnoreCase("/roll")) {
            String[] format = contentTextArr[1].split("d");
            List<Integer> diceRoll = diceRolling(Integer.parseInt(format[0]), Integer.parseInt(format[1]));
            String result = diceOutputBuilder(
                    diceRoll, Integer.parseInt(format[0]), Integer.parseInt(format[1]));
            return new TextMessage(result);
        }

        if (contentTextArr[0].equalsIgnoreCase("/multiroll")) {
            String attempt = contentTextArr[1];
            String[] format = contentTextArr[2].split("d");
            List<Integer> result = multiRolling(
                    Integer.parseInt(attempt), Integer.parseInt(format[0]), Integer.parseInt(format[1]));
            String output = multiRollOutputBuilder(
                    Integer.parseInt(attempt), Integer.parseInt(format[0]), Integer.parseInt(format[1]),
                    result);
            return new TextMessage(output);
        }

        if (contentTextArr[0].equalsIgnoreCase("/is_lucky")) {
            String luckyNum = contentTextArr[1];
            String[] format = contentTextArr[2].split("d");
            String result = isLuckyNumber(
                    Integer.parseInt(luckyNum), Integer.parseInt(format[0]), Integer.parseInt(format[1]));
            return new TextMessage(result);
        }

        String replyText = contentText.replace("/echo", "");
        return new TextMessage(replyText.substring(1));
    }

    @EventMapping
    public TextMessage handleFollowEvent(FollowEvent event) {
        String replyToken = event.getReplyToken();
        return new TextMessage("Hai! Terima Kasih udah add GimBot!\n"
                + "Jadi pengen kasih tau ni ada fitur apa aja di GimBot, "
                + "kamu bisa simulasi dadu, simulasi coin, simulasi multiple dadu, "
                + "bahkan simulasi lucky number. Fitur yang kami sediakan bisa digunakan "
                + "menggunakan command : \n"
                + "-) /coin : untuk memutar coin \n"
                + "-) /roll XdY : untuk memutar dadu, dengan X-kali lempar dan Y-sided dadu \n"
                + "-) /multiroll N XdY : untuk memutar dadu, dengan iterasi sebanyak N-kali \n"
                + "-) /is_lucky NUM XdY : untuk check apakah nilai NUM muncul pada dadu yang anda putar \n"
                + "Untuk bertanya lagi, silahkan menggunakan command /help");
    }

    @EventMapping
    public void handleDefaultMessage(Event event) {
        LOGGER.fine(String.format("Event(timestamp='%s',source='%s')",
                event.getTimestamp(), event.getSource()));
    }

    public static List<Integer> diceRolling(int x, int y) {
        List<Integer> result = new ArrayList<Integer>();
        for (int roll = 0; roll < x; roll++) {
            result.add(oneRoll(x, y));
        }
        return result;
    }

    public static Integer oneRoll(int xSided, int ySided) {
        Random rand = new Random();
        return rand.nextInt(ySided) + 1;
    }


//    public static String multiRolling(int attempt, int x, int y) {
//        List<Integer> result = new ArrayList<Integer>();
//        String output = "";
//        for (int j = 0; j < attempt; j++) {
//            output += multiRollOutputBuilder(x, y, diceRolling(x, y));
//        }
//        return output;
//    }

    public static List<Integer> multiRolling(int attempt, int x, int y) {
        List<Integer> result = new ArrayList<Integer>();
        String output = "";
        for (int j = 0; j < attempt; j++) {
            result.addAll(diceRolling(x, y));
        }
        return result;
    }


    public static String multiRollOutputBuilder(int attempt, int x, int y, List<Integer> subList) {
        String start = "";
        int counter = 1 ;

        for (int i = 0; i < attempt; i += 1) {
            start += x + "d" + y + " (";

            for (int j = i * x; j < x * counter; j++) {
                start += subList.get(j) + ", ";
            }
            counter++;
            start = start.substring(0, start.length()-2) + ")\n";
        }
        return start;

    }


    public static String diceOutputBuilder(List<Integer> list, int x, int y) {
        String base = "Result: " + x + "d" + y + " (";
        for (int i = 0; i < x; i++) {
            base += list.get(i) + ", ";
        }
        String output = base.substring(0, base.length()-2) + ")";
        return output;
    }

    public static String coinRolling() {
        Random rand = new Random();
        int n = rand.nextInt(2);
        if (n == 0) {
            return "head";
        } else {
            return "tail";
        }
    }

    public static String isLuckyNumber(int luckyNum, int x, int y) {
        List<Integer> possibilities = diceRolling(x, y);
        List<Integer> luck = new ArrayList<Integer>();
        luck.add(luckyNum);
        possibilities.retainAll(luck);

        if (possibilities.size() == 0) {
            return "I'm not lucky";
        } else {
            return luckyNum + " appears " + possibilities.size();
        }
    }
}

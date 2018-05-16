package advprog.toplaughers.controller;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;

import java.util.Map;
import java.util.SortedSet;


public class TopLaughersControllerStub extends TopLaughersController {

    @Override
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        return new TextMessage("1.        \n2.\n3.\n4.\n5.");
    }

    @Override
    public String getSenderName(MessageEvent<TextMessageContent> event) {
        return "Sender Name";
    }

    @Override
    public String getGroomUserId(MessageEvent<TextMessageContent> event) {
        return "Groom ID";
    }

    @Override
    public String printLaughers(SortedSet<Map.Entry<String,Integer>> groomUserId,
                                String id, Map<String, Integer> totalLaughter) {
        return "[C=15, B=10, A=5]";
    }

    @Override
    public int toPercentage(int laughterTimes, int total) {
        return 50;
    }
}

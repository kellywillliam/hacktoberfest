package advprog.TopLaughers.controller;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.Map;
import java.util.SortedSet;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;


public class TopLaughersControllerStub extends TopLaughersController {

    @Override
    public TextMessage handleTextMessageEvent(MessageEvent<TextMessageContent> event){
        return new TextMessage("1.        \n2.\n3.\n4.\n5.");
    }

    @Override
    public String getSenderName(MessageEvent<TextMessageContent> event){
        return "Sender Name";
    }

    @Override
    public String getGroomUserId(MessageEvent<TextMessageContent> event){
        return "Groom ID";
    }

    @Override
    public String printLaughers(SortedSet<Map.Entry<String,Integer>> groomUserId, String id, Map<String, Integer> totalLaughter){
        return "[C=15, B=10, A=5]";
    }

    @Override
    public int toPercentage(int laughterTimes, int total){
        return 50;
    }
}

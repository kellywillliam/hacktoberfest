package advprog.example.bot.composer;

import advprog.example.bot.composer.RandomTriviaComposer;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class RandomTriviaComposer {

    public RandomTriviaComposer() {}

    public CarouselTemplate addQuestion(String question, String answer) {
//        String[] answers = answer.split("\n");
//        List<Action> actionList = new LinkedList<>();
        List<CarouselColumn> carouselColumns = new LinkedList<>();
//
        CarouselTemplate carouselTemplate = new CarouselTemplate(carouselColumns);
//
//        for (String choice: answers) {
//            CarouselColumn card = new CarouselColumn("url", choice, choice, actionList);
//        }
//
        return carouselTemplate;
    }

    public void changeAnswer() {

    }


}

package advprog.example.bot.composer;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class RandomTriviaComposer {

    public RandomTriviaComposer() {}

    public void addQuestion(String question) {


    }

    public CarouselTemplate addAnswers(String answer) {
        String[] answers = answer.split("\n");

        List<Action> actionList = new LinkedList<>();
        List<CarouselColumn> carouselColumns = new LinkedList<>();

        CarouselTemplate carouselTemplate = new CarouselTemplate(carouselColumns);

        for (String choice: answers) {
            CarouselColumn card = new CarouselColumn("url", choice, choice, actionList);
        }
        return carouselTemplate;
    }


    public void changeAnswer() {

    }


}

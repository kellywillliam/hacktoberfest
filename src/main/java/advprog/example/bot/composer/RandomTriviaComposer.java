package advprog.example.bot.composer;

import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

public class RandomTriviaComposer {

    public RandomTriviaComposer() {}

    public TemplateMessage questionTemplate(String creatorId){
        List<CarouselColumn> carouselColumns = new ArrayList<>();
        CarouselTemplate carouselTemplate = new CarouselTemplate(carouselColumns);

        int noChoice = 1;
        // Get questions from database with user's id
        // Only showing this user's questions

        return new TemplateMessage("Choose between these questions", carouselTemplate);
    }

    public TemplateMessage answerTemplate(Question question){
        List<CarouselColumn> carouselColumns = new ArrayList<>();
        CarouselTemplate carouselTemplate = new CarouselTemplate(carouselColumns);

        int noChoice = 1;
        for(Answer answer: question.getAnswers()) {
            String choice = answer.getAnswer();
            String title = "Choice " + noChoice;
            CarouselColumn card = new CarouselColumn(null, title, choice, Collections.singletonList(new PostbackAction("Choose this", question.creatorId)));
            carouselColumns.add(card);
            noChoice++;
        }
        return new TemplateMessage("Choose the right answer", carouselTemplate);
    }

}

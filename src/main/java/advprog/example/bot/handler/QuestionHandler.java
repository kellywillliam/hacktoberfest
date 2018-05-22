package advprog.example.bot.handler;

import advprog.example.bot.composer.Answer;
import advprog.example.bot.composer.Question;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;

public class QuestionHandler {

    Question question;

    @EventMapping
    public TextMessage handleCommandAddQuestion(MessageEvent<TextMessageContent> event){
        return new TextMessage("Please enter your question: ");
    }

    @EventMapping
    public TextMessage handleAddQuestion(MessageEvent<TextMessageContent> event, String senderId){
        question = new Question(senderId);

        question.addQuestion(event.getMessage().getText());

        return new TextMessage("Please provide 4 choices for the answer");
    }

    @EventMapping
    public void handleAddAnswers(MessageEvent<TextMessageContent> event){
        Answer answer = new Answer(event.getMessage().getText());
        question.addAnswers(answer);
    }
}

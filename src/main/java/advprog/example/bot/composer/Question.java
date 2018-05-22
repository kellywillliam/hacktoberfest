package advprog.example.bot.composer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "question")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotNull
    String question;

    @NotNull
    String creatorId;

    @NotNull
    int correctAnswerIndex;

    @OneToMany(mappedBy = "question")
    List<Answer> answers;


    public Question(String senderId) {
        this.creatorId = senderId;
        this.correctAnswerIndex = -1;
        this.answers =  new ArrayList<>();
    }

    public String getQuestion() {
        return question;
    }

    public void addQuestion(String question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void addAnswers(Answer answer) {
        this.answers.add(answer);
    }

    public String getCorrectAnswer() {
        return answers.get(correctAnswerIndex).getAnswer();
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }

    @Override
    public String toString(){
        String reply = "";

        reply = reply + ("Question:\n" + getQuestion());
        reply = reply + ("\nChoices:\n" + getAnswers());
        reply = reply + ("\nCorrect answer:\n" + getCorrectAnswer());

        return reply;
    }

}

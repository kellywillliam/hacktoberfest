package advprog.example.bot.database;


import advprog.example.bot.composer.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionDb extends JpaRepository<Question, Integer> {
    List<Question> findByCreatorId(String creatorId);

}

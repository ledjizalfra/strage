package it.buniva.strage.repository;

import it.buniva.strage.entity.Answer;
import it.buniva.strage.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    boolean existsByAnswerContentAndQuestion(String aContent, Question question);

    boolean existsByQuestionAndCorrectTrue(Question question);

    boolean existsByAnswerCode(String code);

    List<Answer> findAllByQuestionQuestionCodeAndEnabledTrueAndDeletedFalse(String questionCode);

    Answer findByQuestionAndCorrectTrueAndDeletedFalse(Question question);

    Answer findByAnswerCodeAndEnabledTrueAndDeletedFalse(String answerCode);

    Answer findByAnswerCodeAndDeletedFalse(String answerCode);

    Answer findByAnswerContentAndQuestion(String answerContent, Question question);
}

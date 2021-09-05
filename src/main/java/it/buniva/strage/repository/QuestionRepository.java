package it.buniva.strage.repository;

import it.buniva.strage.entity.Argument;
import it.buniva.strage.entity.Question;
import it.buniva.strage.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    boolean existsByQuestionCode(String questionCode);

    boolean existsByQuestionContentAndArgumentSubject(String questionContent, Subject subject);

    Question findByQuestionCodeAndEnabledTrueAndDeletedFalse(String questionCode);

    Question findByQuestionCodeAndDeletedFalse(String questionCode);

    List<Question> findAllByEnabledTrueAndDeletedFalse();

    List<Question> findAllByArgumentAndEnabledTrueAndDeletedFalse(Argument argument);

    List<Question> findAllByArgumentSubjectAndEnabledTrueAndDeletedFalse(Subject subject);

    Question findByQuestionContentAndArgument(String questionContent, Argument argument);
}

package it.buniva.strage.repository;

import it.buniva.strage.entity.Argument;
import it.buniva.strage.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    boolean existsByQuestionCode(String questionCode);

    boolean existsByQuestionContentAndArgument(String questionContent, Argument argument);

    Question findByQuestionCodeAndEnabledTrueAndDeletedFalse(String questionCode);
}

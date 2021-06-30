package it.buniva.strage.payload.response;

import it.buniva.strage.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionResponse {

    private String questionCode;

    private String questionContent;

    private String argumentCode;

    private Double correctAnswerMark;

    private Double incorrectAnswerMark;

    private boolean enabled;


    public static QuestionResponse createFromQuestion(Question question) {

        return new QuestionResponse(
                question.getQuestionCode(),
                question.getQuestionContent(),
                question.getArgument().getArgumentCode(),
                question.getCorrectAnswerMark(),
                question.getIncorrectAnswerMark(),
                question.isEnabled()
        );
    }
}

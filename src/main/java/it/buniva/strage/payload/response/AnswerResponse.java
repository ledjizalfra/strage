package it.buniva.strage.payload.response;

import it.buniva.strage.constant.AnswerConstant;
import it.buniva.strage.entity.Answer;
import it.buniva.strage.entity.Argument;
import it.buniva.strage.entity.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerResponse {

    private String questionCode;

    private String answerCode;

    private String answerContent;

    private boolean correct;

    private boolean defaultAnswer;

    private boolean noWantAnswerItem;

    private boolean enabled;


    public static AnswerResponse createFromAnswer(Answer answer) {

        String questionCode = "";
        if(answer.getQuestion()!=null && !answer.isDefaultAnswer() && !answer.isNoWantAnswerItem()) {
            questionCode = answer.getQuestion().getQuestionCode();
        }


        return new AnswerResponse(
                questionCode,
                answer.getAnswerCode(),
                answer.getAnswerContent(),
                answer.isCorrect(),
                answer.isDefaultAnswer(),
                answer.isNoWantAnswerItem(),
                answer.isEnabled()
        );
    }

}

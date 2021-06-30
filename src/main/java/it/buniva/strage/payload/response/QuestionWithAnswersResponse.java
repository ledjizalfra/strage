package it.buniva.strage.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionWithAnswersResponse {

    private QuestionResponse questionResponse;

    private List<AnswerResponse> answerResponseList;
}

package it.buniva.strage.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRequest {

    // Select the answer type ( only one)
    private boolean normalAnswer;

    private boolean defaultAnswer;

    private boolean noWantToAnswer;


    private String questionCode;

    @NotBlank
    private String answerContent;

    @NotNull
    private Boolean correct;

}

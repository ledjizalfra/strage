package it.buniva.strage.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerUpdateRequest {

    @NotBlank
    private String answerContent;

    private boolean correct;

}

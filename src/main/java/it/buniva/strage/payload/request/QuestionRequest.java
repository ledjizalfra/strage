package it.buniva.strage.payload.request;

import it.buniva.strage.entity.Argument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {

    @NotBlank
    private String argumentCode;

    @NotBlank
    private String questionContent;

}
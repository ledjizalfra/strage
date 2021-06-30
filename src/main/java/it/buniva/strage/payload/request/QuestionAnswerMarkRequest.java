package it.buniva.strage.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionAnswerMarkRequest {

    @NotNull
    @DecimalMin(value = "0.00", message = "must be equal or greater than 0.00")
    private Double correctAnswerMark;

    @NotNull
    @DecimalMax(value = "0.00", message = "must be equal or least than 0.00")
    private Double incorrectAnswerMark;
}

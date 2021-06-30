package it.buniva.strage.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateConfigRequest {

    @NotBlank
    private String configurationName; // Title of the configuration

    @NotBlank
    private String subjectCode;

    @NotNull
    @Min(value = 1, message = "must be equal or greater than 1")
    private Integer numberQuestion;

    @NotNull
    @Min(value = 1, message = "must be equal or greater than 1")
    private Integer numberAnswerByQuestion; // number of Answer item per question except other answer

    @NotNull
    @DecimalMin(value = "0.01", message = "must be equal or greater than 0.01")
    private Double minScore;

    @DecimalMin(value = "0.01", message = "must be equal or greater than 0.01")
    private Double minScoreToPass; // min score to pass the quiz exam

    @NotNull
    @DecimalMin(value = "0.01", message = "must be equal or greater than 0.01")
    private Double maxScore; // We get this value from FE

    @NotNull
    @Min(value=1, message = "must be equal or greater than 1")
    private Integer duration; // in millisecond

    @NotNull
    @Min(value=0, message="must be equal or greater than 1")
    private Integer durationSced; // in millisecond

    // flag to check if quiz mark option (correctAnswerMark, incorrectAnswerMark) are global or specific for any question
    private boolean sameMarkForAllQuestions;

    @DecimalMin(value = "0.01", message = "must be equal or greater than 0.01")
    private Double correctAnswerMark; // correct answer mark

    @DecimalMax(value = "0.00", message = "must be equal or least than 0.00")
    private Double incorrectAnswerMark; // incorrect answer mark

    private boolean roundMark;

    // One Quiz for all student
    private boolean oneQuizForAllStud;

    // Any student have random answer
    private boolean randomAnswer;

    private boolean enabledAutoCommit;

    // Enabled or disabled no want to answer item
    private boolean enabledNoWantAnswer;

    // Enabled or disabled default answer
    private boolean enabledDefaultAns;
}

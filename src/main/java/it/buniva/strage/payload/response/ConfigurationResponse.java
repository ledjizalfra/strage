package it.buniva.strage.payload.response;

import it.buniva.strage.entity.Configuration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationResponse {

    private String configurationCode;

    private String configurationName; // Title of the configuration

    private String subjectCode;

    private Integer numberQuestion;

    private Integer numberAnswerByQuestion; // number of Answer item per question except other answer

    private Double minScore;

    private Double minScoreToPass; // min score to pass the quiz exam

    private Double maxScore; // We get this value from FE

    private Integer duration; // in millisecond

    private Integer durationSced; // in millisecond

    private boolean sameMarkForAllQuestions;

    private Double correctAnswerMark; // correct answer mark

    private Double incorrectAnswerMark; // incorrect answer mark

    private boolean roundMark;

    private boolean oneQuizForAllStud;

    private boolean randomAnswer;

    private boolean enabledAutoCommit;

    private boolean enabledNoWantAnswer;

    private boolean enabledDefaultAns;

    public static ConfigurationResponse createFromConfiguration(Configuration configuration) {
        return new ConfigurationResponse(
                configuration.getConfigurationCode(),
                configuration.getConfigurationName(),
                configuration.getSubject().getSubjectCode(),
                configuration.getNumberQuestion(),
                configuration.getNumberAnswerByQuestion(),
                configuration.getMinScore(),
                configuration.getMinScoreToPass(),
                configuration.getMaxScore(),
                configuration.getDuration(),
                configuration.getDurationSced(),
                configuration.isSameMarkForAllQuestions(),
                configuration.getCorrectAnswerMark(),
                configuration.getIncorrectAnswerMark(),
                configuration.isRoundMark(),
                configuration.isOneQuizForAllStud(),
                configuration.isRandomAnswer(),
                configuration.isEnabledAutoCommit(),
                configuration.isEnabledNoWantAnswer(),
                configuration.isEnabledDefaultAns()
        );
    }
}

package it.buniva.strage.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="CONFIGURATION")
public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="CONFIGURATION_ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NaturalId
    @Column(nullable=false, unique = true)
    private String configurationCode;// C-xxx //000-999

    @NotBlank
    @Column(nullable=false, unique = true)
    private String configurationName; // Title of the configuration

    @NotNull
    @Column(nullable=false)
    @Min(value = 1, message = "must be equal or greater than 1")
    private Integer numberQuestion;

    @NotNull
    @Column(nullable=false)
    @Min(value = 1, message = "must be equal or greater than 1")
    private Integer numberAnswerByQuestion; // number of Answer item per question except other answer

    @NotNull
    @DecimalMin(value = "0.01", message = "must be equal or greater than 0.01")
    @Column(nullable=false, columnDefinition="DECIMAL(4,2)")
    private Double minScore;

    @NotNull
    @DecimalMin(value = "0.01", message = "must be equal or greater than 0.01")
    @Column(nullable=false, columnDefinition="DECIMAL(4,2)")
    private Double minScoreToPass; // min score to pass the quiz exam

    @NotNull
    @DecimalMin(value = "0.01", message = "must be equal or greater than 0.01")
    @Column(nullable=false, columnDefinition="DECIMAL(4,2)")
    private Double maxScore; // We get this value from FE

    @NotNull
    @Column(nullable=false)
    @Min(value=1, message = "must be equal or greater than 1")
    private Integer duration; // in millisecond

    @NotNull
    @Column(nullable=false)
    @Min(value=0, message="must be equal or greater than 1")
    private Integer durationSced; // in millisecond

    // flag to check if quiz mark option (correctAnswerMark, incorrectAnswerMark) are global or specific for any question
    @Column(name="IS_SAME_MARK_FOR_ALL_QUESTIONS", columnDefinition="TINYINT(1)")
    private boolean sameMarkForAllQuestions;

    @DecimalMin(value = "0.01", message = "must be equal or greater than 0.01")
    @Column(nullable=false, columnDefinition="DECIMAL(3,2)")
    private Double correctAnswerMark; // correct answer mark

    @DecimalMax(value = "0.00", message = "must be equal or least than 0.00")
    @Column(nullable=false, columnDefinition="DECIMAL(4,3)")
    private Double incorrectAnswerMark; // incorrect answer mark

    @Column(name="IS_ROUND_MARK", columnDefinition="TINYINT(1)")
    private boolean roundMark;

    // One Quiz for all student
    @Column(name="IS_ONE_QUIZ_FOR_ALL_STUD", columnDefinition="TINYINT(1)")
    private boolean oneQuizForAllStud;

    // Any student have random answer
    @Column(name="IS_RANDOM_ANSWER", columnDefinition="TINYINT(1)")
    private boolean randomAnswer;

    @Column(name="IS_ENABLED_AUTO_COMMIT", columnDefinition="TINYINT(1)")
    private boolean enabledAutoCommit;

    // Enabled or disabled no want to answer item
    @Column(name="IS_ENABLED_NO_WANT_ANS", columnDefinition="TINYINT(1)")
    private boolean enabledNoWantAnswer;

    // Enabled or disabled default answer
    @Column(name="IS_ENABLED_DEFAULT_ANS", columnDefinition="TINYINT(1)")
    private boolean enabledDefaultAns;

    @JsonIgnore
    @Column(name="IS_DELETED", columnDefinition="TINYINT(1)")
    private boolean deleted;

    @Column(name="IS_ENABLED", columnDefinition="TINYINT(1)")
    private boolean enabled;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="CREATED_AT",
            updatable=false, insertable=false,
            columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Rome")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="UPDATED_AT",
            insertable=false,
            columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Rome")
    private Date updatedAt;



    // ============= MAPPING WITH OTHER TABLES =================
    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="COURSE_ID", nullable = false)
    private Subject subject;

}

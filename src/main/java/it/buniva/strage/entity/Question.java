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
@Table(name="QUESTION")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="QUESTION_ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NaturalId
    @Column(nullable=false, unique = true)
    private String questionCode; // Q-

    @NotBlank
    @Column(nullable=false, columnDefinition="TEXT")
    private String questionContent;

    @DecimalMin(value = "0.00", message = "must be equal or greater than 0.00")
    @DecimalMax(value = "5.00", message = "must be equal or least than 5.00")
    @Column(columnDefinition="DECIMAL(3,2)")
    private Double difficultyLevel;

    @Min(value = 0, message = "must be equal or greater than 0")
    private Long occurrenceInQuiz; // number of time this question appear in quiz

    @Min(value = 0, message = "must be equal or greater than 0")
    private Long occurrenceTrueInQuiz; // number of time this question appear in quiz and has good answer

    // flag to check if quiz mark option (correctAnswerMark, incorrectAnswerMark) are global or specific for any question
    @DecimalMin(value = "0.00", message = "must be equal or greater than 0.00")
    @Column(columnDefinition="DECIMAL(3,2)")
    private Double correctAnswerMark;

    @DecimalMax(value = "0.00", message = "must be equal or least than 0.00")
    @Column(columnDefinition="DECIMAL(3,2)")
    private Double incorrectAnswerMark;

    @Column(name="IS_ENABLED", columnDefinition="TINYINT(1)")
    private boolean enabled;

    @JsonIgnore
    @Column(name="IS_DELETED", columnDefinition="TINYINT(1)")
    private boolean deleted;

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
    @JoinColumn(name="ARGUMENT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_ARGUMENT_IN_QUESTION"))
    private Argument argument;
}

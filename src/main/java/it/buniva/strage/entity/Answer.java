package it.buniva.strage.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ANSWER")
public class Answer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="ANSWER_ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NaturalId
    @Column(nullable=false, unique = true)
    private String answerCode;

    @NotBlank
    @Column(nullable=false, columnDefinition="TEXT")
    private String answerContent;

    @Column(name="IS_ENABLED", columnDefinition="TINYINT(1)")
    private boolean enabled;

    @JsonIgnore
    @Column(name="IS_DELETED", columnDefinition="TINYINT(1)")
    private boolean deleted;

    @Column(name="IS_CORRECT", columnDefinition="TINYINT(1)")
    private boolean correct;

    @Column(name="IS_DEFAULT_ANS", columnDefinition="TINYINT(1)")
    private boolean defaultAnswer;

    @Column(name="IS_NO_ANSWER_ITEM", columnDefinition="TINYINT(1)")
    private boolean noWantAnswerItem;


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
    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="QUESTION_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_QUESTION_IN_ANSWER"))
    private Question question;
}

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
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="CLASSROOM")
public class Classroom implements Serializable {

    @Id
    @Column(name="CLASS_ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NaturalId
    @Column(nullable=false, unique = true)
    private String classroomName;

    @NotNull
    @Column(nullable=false)
    @Pattern(regexp = "^[1-5]{1}$")
    private String annuity;

    @NotBlank
    @Column(nullable=false)
    private String discipline;

    @NotBlank
    @Column(nullable=false)
    @Pattern(regexp = "^[a-zA-Z]{1}$")
    private String section; //A,B,C,D..

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
    @JsonIgnore
    @ManyToMany
    @JoinTable(name="CLASSROOM_SUBJECT",
            joinColumns = {@JoinColumn(name="CLASS_ID", referencedColumnName="CLASS_ID", foreignKey = @ForeignKey(name = "FK_CLASSROOM_IN_CLASSROOM_SUBJECT"))},
            inverseJoinColumns = {@JoinColumn(name="SUBJECT_ID", referencedColumnName="SUBJECT_ID", foreignKey = @ForeignKey(name = "FK_SUBJECT_IN_CLASSROOM_SUBJECT"))})
    private Set<Subject> subjectSet;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name="CLASSROOM_PROFESSOR",
            joinColumns = {@JoinColumn(name="CLASS_ID", referencedColumnName="CLASS_ID", foreignKey = @ForeignKey(name = "FK_CLASSROOM_IN_CLASSROOM_PROFESSOR"))},
            inverseJoinColumns = {@JoinColumn(name="PROFESSOR_ID", referencedColumnName="PROFESSOR_ID", foreignKey = @ForeignKey(name = "FK_PROFESSOR_IN_CLASSROOM_PROFESSOR"))})
    private Set<Professor> professorSet;
}

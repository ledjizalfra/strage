package it.buniva.strage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="PROFESSOR")
public class Professor implements Serializable {
    @Id
    @Column(name = "PROFESSOR_ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Embedded
    private PersonalData personalData = new PersonalData();

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
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="USER_ID", foreignKey = @ForeignKey(name = "FK_USER_IN_PROFESSOR"))
    private User user;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name="PROFESSOR_SUBJECT",
            joinColumns = {@JoinColumn(name="PROFESSOR_ID", referencedColumnName="PROFESSOR_ID", foreignKey = @ForeignKey(name = "FK_PROFESSOR_IN_PROFESSOR_COURSES"))},
            inverseJoinColumns = {@JoinColumn(name="SUBJECT_ID", referencedColumnName="SUBJECT_ID", foreignKey = @ForeignKey(name = "FK_SUBJECT_IN_PROFESSOR_COURSES"))})
    private Set<Subject> subjectSet;
}

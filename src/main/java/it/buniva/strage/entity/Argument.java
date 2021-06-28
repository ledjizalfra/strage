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
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="ARGUMENT")
public class Argument {

    @Id
    @Column(name="ARGUMENT_ID")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NaturalId
    @Column(nullable=false, unique = true)
    private String argumentCode;// FUNZIONI-1

    @NotBlank
    @Column(nullable=false)
    private String argumentName;

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
    @JoinColumn(name="SUBJECT_ID", foreignKey = @ForeignKey(name = "FK_SUBJECT_IN_ARGUMENT"))
    private Subject subject;
}

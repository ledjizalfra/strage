package it.buniva.strage.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Email
    @NotBlank
    @Size(min = 1, max = 100)
    @NaturalId(mutable = true)
    @Column(length = 100, nullable = false)
    private String username; // Email

    @NotBlank
    @JsonIgnore
    @Size(min = 8, max = 100)
    @Column(length = 100, nullable = false)
    private String password;

    @Column(name = "IS_ENABLED", columnDefinition = "TINYINT(1)") //Da fare
    private boolean enabled;

    @JsonIgnore
    @Column(name = "IS_DELETED", columnDefinition = "TINYINT(1)") //Questo
    private boolean deleted;

//    @Column(name = "IS_NOT_LOCKED", columnDefinition = "TINYINT(1)")
//    private boolean isNotLocked;

    @Column(name = "IS_MAIL_SENT", columnDefinition = "TINYINT(1)")
    private boolean mailSent;

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ROLE_ID", foreignKey = @ForeignKey(name = "FK_ROLE_IN_USER"))
    private Role role;
}

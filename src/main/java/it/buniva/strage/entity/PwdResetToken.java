package it.buniva.strage.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import it.buniva.strage.constant.PwdResetTokenConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="PASSWORD_RESET_TOKEN")
public class PwdResetToken implements Serializable {

    @Id
    @Column(name = "TOKEN_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;

    private String token;

    @Column(name = "IS_ACTIVATED", columnDefinition = "TINYINT(1)")
    private boolean activated;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "USER_ID", foreignKey = @ForeignKey(name = "FK_PWD_RESET_TOKEN_USER"))
    private User user;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Rome")
    private Date expiryDate;


    // CONSTRUCTOR
    public PwdResetToken(String token, User user) {
        super();

        this.token = token;
        this.activated = true;
        this.user = user;
        this.expiryDate = calculateExpiryDate(PwdResetTokenConstant.EXPIRATION);
    }


    private Date calculateExpiryDate(int expiryTimeInMinutes) {

        final Calendar rightNow = Calendar.getInstance();

        rightNow.setTimeInMillis(new Date().getTime());
        rightNow.add(Calendar.MINUTE, expiryTimeInMinutes);

        return new Date(rightNow.getTime().getTime());
    }

    public void updatePwdResetToken(String token) {
        this.token = token;
        this.activated = true;
        this.expiryDate = calculateExpiryDate(PwdResetTokenConstant.EXPIRATION);
    }

    public boolean isExpired() {
        return new Date().after(this.expiryDate);
    }
}

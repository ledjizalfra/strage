package it.buniva.strage.entity.compositeatributte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PersonalData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Email
    @NotBlank
    @Size(min=1, max=100)
    @Column(length=100, nullable=false)
    private String email;

    @Size(min=1, max=100)
    @Column(length=100)
    private String name;

    @Size(min=1, max=100)
    @Column(length=100)
    private String surname;
}

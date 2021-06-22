package it.buniva.strage.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.buniva.strage.entity.Admin;
import it.buniva.strage.entity.Professor;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorResponse {

    private Long professorId;

    private PersonalData personalData;

    private boolean enabled;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Rome")
    private Date createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Rome")
    private Date updatedAt;

    public static ProfessorResponse createFromProfessor(Professor professor) {
        return new ProfessorResponse(
                professor.getId(),
                professor.getPersonalData(),
                professor.isEnabled(),
                professor.getCreatedAt(),
                professor.getUpdatedAt()
        );
    }
}

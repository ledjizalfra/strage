package it.buniva.strage.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.buniva.strage.entity.Professor;
import it.buniva.strage.entity.Student;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {

    private Long studentId;

    private PersonalData personalData;

    private boolean enabled;

    private boolean scedEnabled;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Rome")
    private Date createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Rome")
    private Date updatedAt;

    public static StudentResponse createFromStudent(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getPersonalData(),
                student.isEnabled(),
                student.isScedEnabled(),
                student.getCreatedAt(),
                student.getUpdatedAt()
        );
    }
}

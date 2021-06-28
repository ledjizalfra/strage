package it.buniva.strage.payload.response;

import it.buniva.strage.entity.Classroom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomResponse {

    private String classroomName;

    private String annuity;

    private String discipline;

    private String section;

    private boolean enabled;


    public static ClassroomResponse createFrom(Classroom classroom) {

        return new ClassroomResponse(
                classroom.getClassroomName(),
                classroom.getAnnuity(),
                classroom.getDiscipline(),
                classroom.getSection(),
                classroom.isEnabled()
        );
    }
}

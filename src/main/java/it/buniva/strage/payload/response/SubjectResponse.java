package it.buniva.strage.payload.response;

import it.buniva.strage.entity.Argument;
import it.buniva.strage.entity.Subject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectResponse {

    private Long subjectId;

    private String subjectCode;

    private String subjectName;

    private boolean enabled;

    public static SubjectResponse createFromSubject(Subject subject) {

        return new SubjectResponse(
                subject.getId(),
                subject.getSubjectCode(),
                subject.getSubjectName(),
                subject.isEnabled()
        );
    }
}

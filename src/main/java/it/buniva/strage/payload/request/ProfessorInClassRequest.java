package it.buniva.strage.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfessorInClassRequest {

    @Email
    @NotBlank
    @Size(min=1, max=100)
    private String professorEmail;

    @NotBlank
    private String classroomName;
}

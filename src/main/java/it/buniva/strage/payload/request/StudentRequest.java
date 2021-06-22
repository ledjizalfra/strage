package it.buniva.strage.payload.request;

import it.buniva.strage.utility.csvfile.StudentCSV;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {

    @Email
    @NotBlank
    @Size(min=1, max=100)
    private String email;

    /*@NotBlank
    @Size(min=8, max=100)*/
    private String password = StringUtils.EMPTY;

    @NotBlank
    @Size(max=100)
    private String name;

    @NotBlank
    @Size(max=100)
    private String surname;



    public static StudentRequest createFrom(StudentCSV userCSV) {

        return new StudentRequest(
                userCSV.getEmail(),
                StringUtils.EMPTY,
                userCSV.getName(),
                userCSV.getSurname()
        );
    }
}

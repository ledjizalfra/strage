package it.buniva.strage.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassroomRequest {

    @NotNull
    @Pattern(regexp = "^[1-5]{1}$",
            message = "Invalid value. It must be one digit in range: [1-5]")
    private String annuity;

    @NotBlank
    private String discipline;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z]{1}$",
            message = "Invalid value. It must be one letter in range: [a-zA-Z]")
    private String section; //A,B,C,D..

}

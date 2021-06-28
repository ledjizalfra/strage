package it.buniva.strage.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArgumentRequest {
    @NotBlank
    private String argumentName;

    @NotBlank
    @Pattern(regexp = "^[1-5]{1}$",
            message = "Invalid value. It must be one digit in range: [1-5]")
    private String annuity;

    @NotBlank
    private String subjectCode;
}
package it.buniva.strage.payload.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequest {

    @NotBlank
    @Size(min=8, max=100)
    private String oldPassword;

    @NotBlank
    @Size(min=8, max=100)
    private String newPassword;
}

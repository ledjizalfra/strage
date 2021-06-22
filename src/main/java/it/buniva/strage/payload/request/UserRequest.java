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
public class UserRequest {

    @Email
    @NotBlank
    private String username; // Email

    @NotBlank
    @Size(min=8, max=100)
    private String password;

    @NotBlank
    private String role;
}

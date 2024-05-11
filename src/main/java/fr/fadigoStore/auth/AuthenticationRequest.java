package fr.fadigoStore.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    @NotEmpty(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    @Email(message = "Email address is not formated")
    private String email;

    @NotEmpty(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    @Size(min = 8, message = "The size is 8 Characters for password")
    private String password;
}

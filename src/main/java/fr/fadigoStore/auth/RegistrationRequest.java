package fr.fadigoStore.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    @NotEmpty(message = "This field is necessary")
    @NotBlank(message = "This field is necessary")
    private String firstname;

    @NotEmpty(message = "This field is necessary")
    @NotBlank(message = "This field is necessary")
    private String lastname;

    @NotEmpty(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    @Email(message = "Email address is formatted")
    private String email;

    @NotEmpty(message = "This field is mandatory")
    @NotBlank(message = "This field is mandatory")
    @Size(min = 8, message = "The size is 8 Characters for password")
    private String password;


}

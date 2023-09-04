package ru.kpfu.itis.shkalin.spring_site_politics.dto.user;

import lombok.*;
import org.springframework.security.core.CredentialsContainer;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFormRegisterDto implements CredentialsContainer {

    @Size(min = 2, max = 255, message = "Length of username should be from 2 to 30 characters")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Email(message = "Email is not correct")
    @NotBlank(message = "Email cannot be empty")
    @Size(max = 255, message = "Length of email address should Not be more 255 characters")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, max = 255, message = "Length of password should be more 6 characters")
    private String password;

    @NotBlank(message = "Second password cannot be empty")
    @Size(min = 6, max = 255, message = "Length of password should be more 6 characters")
    private String password2;

    @Override
    public void eraseCredentials() {
        this.password = null;
        this.password2 = null;
    }

}

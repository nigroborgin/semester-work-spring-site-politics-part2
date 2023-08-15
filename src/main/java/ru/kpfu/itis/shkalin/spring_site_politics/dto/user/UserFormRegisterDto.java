package ru.kpfu.itis.shkalin.spring_site_politics.dto.user;

import lombok.*;
import org.springframework.security.core.CredentialsContainer;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserFormRegisterDto implements CredentialsContainer {
    private String username;
    private String email;
    private String password;
    private String password2;

    @Override
    public void eraseCredentials() {
        this.password = null;
        this.password2 = null;
    }
}

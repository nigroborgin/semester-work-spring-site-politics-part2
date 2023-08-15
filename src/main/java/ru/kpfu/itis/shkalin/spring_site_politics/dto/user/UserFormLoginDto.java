package ru.kpfu.itis.shkalin.spring_site_politics.dto.user;

import lombok.*;
import org.springframework.security.core.CredentialsContainer;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserFormLoginDto implements CredentialsContainer {
    private String username;
    private String password;
    private Boolean saveuser;

    @Override
    public void eraseCredentials() {
        this.password = null;
    }
}

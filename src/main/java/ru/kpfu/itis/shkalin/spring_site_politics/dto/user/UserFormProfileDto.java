package ru.kpfu.itis.shkalin.spring_site_politics.dto.user;

import lombok.*;
import org.springframework.security.core.CredentialsContainer;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserFormProfileDto implements CredentialsContainer {
    private String username;
    private String password;
    private String email;
    private String roleName;

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

}

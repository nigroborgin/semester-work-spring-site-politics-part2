package ru.kpfu.itis.shkalin.spring_site_politics.dto.user;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserViewDto {
    private Integer id;
    private String roleName;
    private String username;
    private String email;
    private String pictureUrl;
}

package ru.kpfu.itis.shkalin.spring_site_politics.dto.post;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostFormDto {
    private String title;
    private String text;
}

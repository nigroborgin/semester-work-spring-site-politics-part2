package ru.kpfu.itis.shkalin.spring_site_politics.dto.post;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostViewDto {
    private Integer id;
    private String title;
    private String date;
    private String authorOfPost;
    private String text;
}

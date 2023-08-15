package ru.kpfu.itis.shkalin.spring_site_politics.dto.book;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookFormDto {
    private String title;
    private String description;
    private String author;
}

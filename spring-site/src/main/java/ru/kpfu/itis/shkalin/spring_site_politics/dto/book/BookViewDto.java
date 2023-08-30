package ru.kpfu.itis.shkalin.spring_site_politics.dto.book;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookViewDto {
    private Integer id;
    private String title;
    private String description;
    private String fileUrl;
    private String author;
}

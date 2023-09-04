package ru.kpfu.itis.shkalin.spring_site_politics.dto.book;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookFormDto {

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Length of book-title should Not be more 255 characters")
    private String title;

    @Size(max = 10000, message = "Length of description should Not be more 10000 characters")
    private String description;

    @NotBlank(message = "Author cannot be empty")
    @Size(max = 255, message = "Length of author should Not be more 255 characters")
    private String author;

}

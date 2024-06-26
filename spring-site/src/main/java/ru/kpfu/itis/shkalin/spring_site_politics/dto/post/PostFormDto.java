package ru.kpfu.itis.shkalin.spring_site_politics.dto.post;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostFormDto {

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Length of title should Not be more 255 characters")
    private String title;

    @NotBlank(message = "Text of post cannot be empty")
    @Size(max = 10000, message = "Length of post-text should Not be more 10000 characters")
    private String text;

}

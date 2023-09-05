package ru.kpfu.itis.shkalin.spring_site_politics.dto.selections_book;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SelectionBookFormDto {

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Length of book-selection-title should Not be more 255 characters")
    private String title;
    private List<Integer> bookIdList;

}

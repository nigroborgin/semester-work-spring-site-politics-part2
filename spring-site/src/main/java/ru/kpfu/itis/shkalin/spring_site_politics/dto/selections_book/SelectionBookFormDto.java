package ru.kpfu.itis.shkalin.spring_site_politics.dto.selections_book;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SelectionBookFormDto {

    @NotBlank(message = "Title cannot be empty")
    private String title;
    private List<Integer> bookIdList;

}

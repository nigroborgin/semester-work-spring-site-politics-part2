package ru.kpfu.itis.shkalin.spring_site_politics.dto.selections_book;

import lombok.*;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookViewDto;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SelectionBookFormDto {

    private Integer id;
    private String title;
    private List<Integer> bookIdList;

}

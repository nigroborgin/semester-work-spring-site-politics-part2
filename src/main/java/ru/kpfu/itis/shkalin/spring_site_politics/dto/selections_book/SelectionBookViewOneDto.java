package ru.kpfu.itis.shkalin.spring_site_politics.dto.selections_book;

import lombok.*;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookViewDto;
import ru.kpfu.itis.shkalin.spring_site_politics.dto.book.BookViewForSelectionDto;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SelectionBookViewOneDto {

    private Integer id;
    private String title;
    private List<BookViewForSelectionDto> bookList;

}

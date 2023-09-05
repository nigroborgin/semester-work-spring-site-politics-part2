package ru.kpfu.itis.shkalin.spring_site_politics.dto.book;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookViewForSelectionDto {

    private Integer id;
    private String title;
    private String description;
    private List<FormatBookDto> formatsOfBook;
    private String author;
    private Boolean isSelected = false;

}

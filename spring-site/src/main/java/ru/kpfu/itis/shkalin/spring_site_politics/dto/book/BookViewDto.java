package ru.kpfu.itis.shkalin.spring_site_politics.dto.book;

import lombok.*;
import ru.kpfu.itis.shkalin.spring_site_politics.model.BookToFormatBook;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookViewDto {

    private Integer id;
    private String title;
    private String description;
    private List<FormatBookDto> formatsOfBook;
    private String author;

    @Override
    public String toString() {
        return "BookViewDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", formats=" + formatsOfBook +
                ", author='" + author + '\'' +
                '}';
    }
}

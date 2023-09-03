package ru.kpfu.itis.shkalin.spring_site_politics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "book_to_format")
@IdClass(BookToFormatBookId.class)
public class BookToFormatBook {
    @Id
    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "id")
    private Book book;

    @Id
    @ManyToOne
    @JoinColumn(name = "format_id", referencedColumnName = "id")
    private FormatBook format;

    @Column(name = "file_url")
    private String url;

    @Override
    public String toString() {
        return "BookToFormatBook{" +
                "book=" + "Book{" + book.getId() + ", " + book.getTitle() + ", " + book.getAuthor() + "} " +
                ", format=" + format +
                ", url='" + url + '\'' +
                '}';
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class BookToFormatBookId implements Serializable {
    private int book;
    private int format;
}

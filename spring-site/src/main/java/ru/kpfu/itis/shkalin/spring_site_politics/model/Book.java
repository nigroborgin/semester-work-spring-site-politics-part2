package ru.kpfu.itis.shkalin.spring_site_politics.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "author")
    private String author;

    @OneToMany(
            fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            mappedBy = "book")
    private List<BookToFormatBook> formats;

    public Optional<BookToFormatBook> getFormatByName(String nameOfFormat) {
        for (BookToFormatBook format : formats) {
            if (format.getFormat().getName().equals(nameOfFormat)) {
                return Optional.of(format);
            }
        }
        return Optional.empty();
    }

}

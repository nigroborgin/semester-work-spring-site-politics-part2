package ru.kpfu.itis.shkalin.spring_site_politics.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "file_url")
    private String fileUrl;

//    @ManyToMany(mappedBy = "books")
//    private List<SelectionBook> selectionsOfBook;
//
//    public void addSelection(SelectionBook selection) {
//        if (selectionsOfBook == null) {
//            selectionsOfBook = new ArrayList<>();
//        }
//        if (!selectionsOfBook.contains(selection)) {
//            selectionsOfBook.add(selection);
//            selection.addBook(this);
//        }
//    }

}

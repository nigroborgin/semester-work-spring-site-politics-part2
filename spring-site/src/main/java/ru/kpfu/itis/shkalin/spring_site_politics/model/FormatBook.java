package ru.kpfu.itis.shkalin.spring_site_politics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "book_format")
public class FormatBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    public FormatBook(Integer id) {
        this.id = id;
    }

    public FormatBook(String name) {
        this.name = name;
    }
}

package ru.kpfu.itis.shkalin.spring_site_politics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.shkalin.spring_site_politics.model.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
}

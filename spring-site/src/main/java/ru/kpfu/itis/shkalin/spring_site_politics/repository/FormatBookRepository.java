package ru.kpfu.itis.shkalin.spring_site_politics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.shkalin.spring_site_politics.model.FormatBook;

import java.util.List;

@Repository
public interface FormatBookRepository extends JpaRepository<FormatBook, Integer> {
    @Query("select fb from FormatBook fb where fb.name = :name")
    List<FormatBook> findByName(@Param("name") String name);
}

package ru.kpfu.itis.shkalin.spring_site_politics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.shkalin.spring_site_politics.model.SelectionBook;

import javax.persistence.NamedNativeQuery;
import java.util.List;

@Repository
public interface SelectionBookRepository extends JpaRepository<SelectionBook, Integer> {

    @Query("select sb from SelectionBook sb where sb.user.id = :idSB")
    List<SelectionBook> findAllByUser(@Param("idSB") Integer id);

    @Query("select sb from SelectionBook sb where sb.user.id is null")
    List<SelectionBook> findAllWithoutUser();

}

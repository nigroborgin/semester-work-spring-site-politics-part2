package ru.kpfu.itis.shkalin.spring_site_politics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.shkalin.spring_site_politics.model.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("select p from Post p where p.user.id = :id")
    List<Post> findAllByUser(Integer id);

}

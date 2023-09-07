package ru.kpfu.itis.shkalin.spring_site_politics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kpfu.itis.shkalin.spring_site_politics.model.AuthName;

import java.util.Optional;

@Repository
public interface AuthNameRepository extends JpaRepository<AuthName, Integer> {
    Optional<AuthName> findByName(String name);
}

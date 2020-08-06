package com.example.demo.repository;

import com.example.demo.entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction,Long> {
    List<Interaction> findAllByUsernameAndFilmId(String username,Long filmId);

    List<Interaction> findAllByUsername(String username);

    @Query(value = "select * from interaction where username=?1 and interested=1",nativeQuery = true)
    List<Interaction> findAllLikedByUsername(String username);

}

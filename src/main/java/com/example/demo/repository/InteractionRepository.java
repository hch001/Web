package com.example.demo.repository;

import com.example.demo.entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction,Long> {
    List<Interaction> findAllByUsernameAndFilmId(String username,Long filmId);
}

package com.example.demo.repository;


import com.example.demo.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film,Long> {
    List<Film> findAllByRatingBetween(float rating, float rating2);

}

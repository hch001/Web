package com.example.demo.repository;


import com.example.demo.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film,Long> {
    List<Film> findAllByTitleContains(String title);
    List<Film> findAllByTitleIsContaining(String title);
    List<Film> findByFilmId(Long filmId);

    @Query(value="select * from film where rating > ?1 and `type`='movie' order by release_date DESC limit ?2",nativeQuery = true)
    List<Film> findAllMoviesByRatingGreaterThanAndOrderByDateWithLimit(int rating,int limit);

    @Query(value="select * from film where rating > ?1 and `type`='tv' order by release_date DESC limit ?2",nativeQuery = true)
    List<Film> findAllTvsByRatingGreaterThanAndOrderByDateWithLimit(int rating,int limit);


}

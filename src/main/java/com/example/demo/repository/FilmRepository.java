package com.example.demo.repository;


import com.example.demo.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.table.TableRowSorter;
import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film,Long> {
    List<Film> findAllByTitleContains(String title);

    List<Film> findByFilmId(Long filmId);

    @Query(value="select * from film where rating > ?1 and `type`='movie' order by release_date DESC limit ?2",nativeQuery = true)
    List<Film> findAllMoviesByRatingGreaterThanAndOrderByDateWithLimit(int rating,int limit);

    @Query(value="select * from film where rating > ?1 and `type`='tv' order by release_date DESC limit ?2",nativeQuery = true)
    List<Film> findAllTvsByRatingGreaterThanAndOrderByDateWithLimit(int rating,int limit);

    @Query(value="select * from film where LOCATE(?1,genres) order by rating desc,release_date desc",nativeQuery = true)
    List<Film> findAllByGenresContainsAndOrderByRatingAndDate(String genre);

    @Query(value="select * from film where locate(?1,film.type) order by rating desc,release_date desc",nativeQuery = true)
    List<Film> findAllByTypeContainsAndOrderByRatingAndDate(String type);

    @Query(value="select * from film where locate(?1,film.area)=1 order by rating desc,release_date desc",nativeQuery = true)
    List<Film> findAllByAreaStartsWithAndOrderByRatingAndDate(String area);

    @Query(value="select * from film where locate(?1,film.genres) and locate(?2,film.language)=1  order by rating desc,release_date desc limit ?3 ",nativeQuery = true)
    List<Film> findAllByOneGenreAndLanguageWithLimit(String genre,String language,int limit);

    @Query(value="select * from film where locate(?1,film.genres) and locate(?2,film.genres) and locate(?3,film.language)=1  order by rating desc,release_date desc limit ?4 ",nativeQuery = true)
    List<Film> findAllByTwoGenreAndLanguageWithLimit(String genre1,String genre2,String language,int limit);
}

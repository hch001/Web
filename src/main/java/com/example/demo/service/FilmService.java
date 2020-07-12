package com.example.demo.service;

import com.example.demo.entity.Film;
import com.example.demo.repository.FilmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class FilmService {
    @Resource
    private FilmRepository filmRepository;

    public List<Film> findAllByTitleContains(String title){
        return filmRepository.findAllByTitleContains(title);
    }

    public List<Film> findByFilmId(Long filmId){
        return filmRepository.findByFilmId(filmId);
    }

    public List<Film> findAllMoviesByRatingGreaterThanAndOrderByDateWithLimit(int rating,int limit){
        return filmRepository.findAllMoviesByRatingGreaterThanAndOrderByDateWithLimit(rating,limit);
    }

    public List<Film> findAllTvsByRatingGreaterThanAndOrderByDateWithLimit(int rating,int limit) {
        return filmRepository.findAllTvsByRatingGreaterThanAndOrderByDateWithLimit(rating,limit);
    }

}

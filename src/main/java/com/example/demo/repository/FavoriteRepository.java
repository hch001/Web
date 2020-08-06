package com.example.demo.repository;

import com.example.demo.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
    List<Favorite> findAllByUsername(String username);
    List<Favorite> findAllByUsernameAndFilmId(String username, Long filmId);
}

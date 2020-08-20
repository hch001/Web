package com.example.demo.repository;

import com.example.demo.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActorRepository extends JpaRepository<Actor,Long> {
    // 返回电影所有的演员
    @Query(value = "select * from actor where actor_id in (select actor_id from perform where perform.film_id = ?1);",nativeQuery = true)
    List<Actor> findAllByFilmId(Long filmId);

}

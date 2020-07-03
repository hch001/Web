package com.example.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filmId;
    private String title;
    private String releaseDate;
    private String area;
    private String language;
    private String genres;
    private float rating;
    private String description;
    private String type;

    public void setFilmId(Long filmId) {
        this.filmId = filmId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setDescription(String description) {
        this.description=description;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getFilmId() {
        return filmId;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getArea() {
        return area;
    }

    public String getLanguage() {
        return language;
    }

    public String getGenres() {
        return genres;
    }

    public float getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

}

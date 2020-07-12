package com.example.demo.controller;

import com.example.demo.entity.Film;
import com.example.demo.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value="/film")
public class FilmController  {
    
    private FilmService filmService;

    @Autowired
    FilmController(FilmService filmService){
        this.filmService=filmService;
    }

    @RequestMapping(value = "/{filmId}",method = RequestMethod.GET)
    public String loadFilmPage(@PathVariable String filmId, Model model, HttpSession session){
        model.addAttribute("user",session.getAttribute("user"));
        List<Film> films = filmService.findByFilmId(Long.parseLong(filmId));
        model.addAttribute("films",null);
        model.addAttribute("films",films);
        return "film";
    }

    @RequestMapping(value = "/{filmId}",method = RequestMethod.POST)
    public String collectInterest(@PathVariable String filmId){
//        System.out.println("Film POST");
//        System.out.println(session.getAttribute("user"));
//        System.out.println(like);
//        System.out.println(notLike);

        return "redirect:/film/{filmId}";
    }

}

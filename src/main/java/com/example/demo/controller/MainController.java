package com.example.demo.controller;


import com.example.demo.entity.Film;
import com.example.demo.repository.FilmRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {

    private FilmRepository filmRepository;
    MainController(FilmRepository filmRepository){
        this.filmRepository=filmRepository;
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String toIndex(HttpSession session, Model model){
        Object object = session.getAttribute("user");
        List<Film> movies = filmRepository.findAllMoviesByRatingGreaterThanAndOrderByDateWithLimit(7,10);
        model.addAttribute("movies",movies);
        List<Film> tvs = filmRepository.findAllTvsByRatingGreaterThanAndOrderByDateWithLimit(7,10);
        model.addAttribute("tvs",tvs);
        // 还没写完推荐的部分
        return "main";
    }

    @RequestMapping(value="/main",method = RequestMethod.GET)
    public String toIndex1(){
        return "redirect:/";
    }

    @RequestMapping(value="/main/",method = RequestMethod.GET)
    public String toIndex2(){
        return "redirect:/";
    }

    @RequestMapping(value="/",method = RequestMethod.POST)
    public String search(@RequestParam("search") String search,Model model){
        List<Film> result = filmRepository.findAllByTitleIsContaining(search);
        model.addAttribute("result",null);
        model.addAttribute("result",result);
        return "main";
    }

}

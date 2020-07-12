package com.example.demo.controller;


import com.example.demo.entity.Film;
import com.example.demo.repository.FilmRepository;
import com.example.demo.service.FilmService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {

    private FilmService filmService;
    MainController(FilmService filmService){
        this.filmService=filmService;
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String toIndex(HttpSession session, Model model){
        Object object = session.getAttribute("user");
        List<Film> movies = filmService.findAllMoviesByRatingGreaterThanAndOrderByDateWithLimit(7,10);
        model.addAttribute("movies",movies);
        List<Film> tvs = filmService.findAllTvsByRatingGreaterThanAndOrderByDateWithLimit(7,10);
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
        List<Film> result = filmService.findAllByTitleContains(search);
        model.addAttribute("result",null);
        model.addAttribute("result",result);
        return "main";
    }

}

package com.example.demo.controller;


import com.example.demo.entity.Film;
import com.example.demo.service.FilmService;
import com.example.demo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {
    @Resource
    private FilmService filmService;
    @Resource
    private UserService userService;


    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String toIndex(HttpSession session, Model model){
        List<Film> movies = filmService.findAllMoviesByRatingGreaterThanAndOrderByDateWithLimit(7,10);
        model.addAttribute("movies",movies);
        List<Film> tvs = filmService.findAllTvsByRatingGreaterThanAndOrderByDateWithLimit(7,10);
        model.addAttribute("tvs",tvs);
        // 还没写完推荐的部分
        String username = (String)session.getAttribute("user");
        if(username!=null)
        {
            List<Film> films =  userService.getUserBasedRecommendation(username,"localhost",8891,400,9);
            model.addAttribute("films",films);
        }

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

    @RequestMapping(value = "/main/exit",method = RequestMethod.GET)
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login/login_page";
    }

}

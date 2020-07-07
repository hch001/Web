package com.example.demo.controller;


import com.example.demo.entity.Film;
import com.example.demo.repository.FilmRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {

    private FilmRepository filmRepository;
    IndexController(FilmRepository filmRepository){
        this.filmRepository=filmRepository;
    }

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String toIndex(HttpSession session, Model model){
        Object object = session.getAttribute("user");
        if(object==null){ // 未登录
            List<Film> films = filmRepository.findAllByRatingGreaterThanAndOrderByDateWithLimit(7,15);
            model.addAttribute("films",films);
            return "main";
        }
        else{
            // 还没写基于用户的部分
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

}
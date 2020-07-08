package com.example.demo.controller;

import com.example.demo.entity.Film;
import com.example.demo.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(value = "/main")
public class UserController {

    private FilmRepository filmRepository;

    @Autowired
    UserController(FilmRepository filmRepository){
        this.filmRepository=filmRepository;
    }

    // 特定用户界面
    @RequestMapping(value = "/{user}",method = RequestMethod.GET)
    public String loadUserPage(HttpSession session, @PathVariable String user,Model model){
        Object object = session.getAttribute("user");
        // session的user属性在LoginController中已经被赋值(如果已经登录的话,)，否则为空
        if(object==null) {
            model.addAttribute("errMsg","您还没有登录，可以尝试登录。"); // 加入错误信息
            return "error"; // 跳转到error进行处理
        }
        else if(!user.equals(object)){
            model.addAttribute("errMsg","您不能访问他人的主页");
        }
        model.addAttribute("errMsg",null); // 清除错误信息
        List<Film> movies = filmRepository.findAllMoviesByRatingGreaterThanAndOrderByDateWithLimit(7,10);
        model.addAttribute("movies",movies);
        List<Film> tvs = filmRepository.findAllTvsByRatingGreaterThanAndOrderByDateWithLimit(7,10);
        model.addAttribute("tvs",tvs);
        return "main";
    }

    // 特定用户界面，退出当前帐号，无效当前session
    @RequestMapping(value = "/exit",method = RequestMethod.GET)
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/login/login_page";
    }

    @RequestMapping(value="/{user}",method = RequestMethod.POST)
    public String search(@RequestParam("search") String search, Model model)  {
        List<Film> result = filmRepository.findAllByTitleContains(search);
        model.addAttribute("result",null);
        model.addAttribute("result",result);
        return "main";
    }

}

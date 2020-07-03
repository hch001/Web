package com.example.demo.controller;

import com.example.demo.repository.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/users")
public class FilmController {

    private FilmRepository filmRepository;

    @Autowired
    FilmController(FilmRepository filmRepository){
        this.filmRepository=filmRepository;
    }

    // 特定用户界面
    @RequestMapping(value = "/{user}",method = RequestMethod.GET)
    public String loadUserPage(HttpSession session, @PathVariable String user,Model model){
        Object object = session.getAttribute("user");
        // session的user属性在LoginController中已经被赋值(如果已经登录的话,)，否则为空
        if(object==null) {
            model.addAttribute("errMsg","您还没有登录，可以尝试登陆。"); // 加入错误信息
            return "error"; // 跳转到error进行处理
        }
        model.addAttribute("errMsg",null); // 清除错误信息
        return "user_page";
    }

    // 特定用户界面，退出当前帐号，无效当前session
    @RequestMapping(value = "/{user}",method = RequestMethod.POST)
    public String logout(HttpSession session, @PathVariable String user){
        session.invalidate();
        return "user_page";
    }
}

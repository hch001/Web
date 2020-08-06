package com.example.demo.controller;

import com.example.demo.entity.Film;
import com.example.demo.service.FavoriteService;
import com.example.demo.service.InteractionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {
    @Resource
    private InteractionService interactionService;
    @Resource
    private FavoriteService favoriteService;


    // 特定用户界面
    @RequestMapping(value = "/users/{user}",method = RequestMethod.GET)
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

        // 传递数据
        // 最近喜欢的影片
        List<Film> recentLiked = interactionService.findAllLikedFilmsByUsernameWithLimit(user,6);
        // 最近添加到收藏夹的影片
        List<Film> recentAdded = favoriteService.findAllFilmsAddedByUsername(user,6);
        model.addAttribute("recentLiked",recentLiked);
        model.addAttribute("recentAdded",recentAdded);

        return "user";
    }

}

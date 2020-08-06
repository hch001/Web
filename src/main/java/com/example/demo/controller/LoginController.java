package com.example.demo.controller;

import com.example.demo.entity.User;
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
@RequestMapping(value = "/login") // 路由
public class LoginController {
    @Resource
    private UserService userService;

    @RequestMapping(value="/login_page",method = RequestMethod.GET) // 跳转到login初始页面
    public String loadLoginPage(Model model){
        model.addAttribute("users",null);
        return "login";
    }

    @RequestMapping(value="/login_page" ,method = RequestMethod.POST)
    public String loginConn(@RequestParam("username") String username, @RequestParam("password") String password, // 获取前端request中的返回的登录用户名和密码
                            HttpSession session, Model model){
        model.addAttribute("users",null); // 先清空
        List<User> users = userService.findUsersByUsernameAndPassword(username,password); // 查询是否有效
        if(users.size()>0) {
            session.setAttribute("user",username); // 正确，在session中存储user属性，并赋值
            session.setMaxInactiveInterval(36000); // 设置session失效的时间，10h
        }
        model.addAttribute("users",users); // model中存储查询的结果，前端判断该数组的大小，(=0 账号不存在),(>0 合法)
        return "login";
    }

    // 跳转到register初始页面
    @RequestMapping(value="/register_page",method = RequestMethod.GET)
    public String loadRegisterPage(Model model){
        model.addAttribute("valid",4); // 说明还没有经过注册查询
        return "register";
    }

    // 用于处理register页面的POST请求，即注册信息
    @RequestMapping(value = "/register_page",method = RequestMethod.POST)
    public String registerConn(@RequestParam("username") String username,@RequestParam("password") String password,Model model,User newUser){
        newUser.setUsername(username);
        newUser.setPassword(password);
        int valid = userService.save(newUser);
        model.addAttribute("valid",valid); // 判断是否有效的变量
        return "register";
    }

}

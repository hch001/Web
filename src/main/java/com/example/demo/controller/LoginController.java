package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.ValidationImgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/login") // 路由
public class LoginController {

    private UserRepository userRepository;

    @Autowired
    public LoginController(UserRepository userRepository,ValidationImgRepository validationImgRepository){
        this.userRepository=userRepository;
    }

    @RequestMapping(value="/login_page",method = RequestMethod.GET) // 跳转到login初始页面
    public String loadLoginPage(){
        return "login";
    }

    @RequestMapping(value="/login_page" ,method = RequestMethod.POST)
    public String loginConn(@RequestParam("username") String username, @RequestParam("password") String password, // 获取前端request中的返回的登录用户名和密码
                            HttpSession session, Model model){
        model.addAttribute("users",null); // 先清空
        List<User> users = userRepository.findUsersByUsernameAndPassword(username,password); // 查询是否有效
        if(users.size()>0) {
            session.setAttribute("user",username); // 正确，在session中存储user属性，并赋值
            session.setMaxInactiveInterval(36000); // 设置session失效的时间，10h
        }
        model.addAttribute("users",users); // model中存储查询的结果，前端判断该数组的大小，(=0 账号不存在),(>0 合法)
        return "login";
    }

    // 跳转到register初始页面
    @RequestMapping(value="/register_page",method = RequestMethod.GET)
    public String loadRegisterPage(){
        return "register";
    }

    // 用于处理register页面的POST请求，即注册信息
    @RequestMapping(value = "/register_page",method = RequestMethod.POST)
    public String registerConn(@RequestParam("username") String username,@RequestParam("password") String password,Model model,User newUser){
        model.addAttribute("users",null); // 清空
        List<User> users = userRepository.findUsersByUsername(username); // 已存在的用户
        if(users.isEmpty()){
            newUser.setPassword(password);
            newUser.setUsername(username);
            userRepository.save(newUser); // 将新创建的用户存储到数据库
            model.addAttribute("user",newUser); // 返回新创建的用户
        }
        model.addAttribute("users",users); // 返回查询结果，users的长度 (=0 说明数据库中没有，可以创建) (>0 用户名已经存在，不能创建) 由前端进行判断
        return "register";
    }

}

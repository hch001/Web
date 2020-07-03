package com.example.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class IndexController {

    @RequestMapping(value = "/",method = RequestMethod.GET)
    public String toIndex(){
        return "index";
    }

    @RequestMapping(value="/index",method = RequestMethod.GET)
    public String toIndex1(){
        return "index";
    }
}

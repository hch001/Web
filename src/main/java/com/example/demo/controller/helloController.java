package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class helloController {
    @RequestMapping(value="/index",method = RequestMethod.GET)
    public String hello()
    {
        return "test";
    }
}

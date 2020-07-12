package com.example.demo.controller;

import com.example.demo.entity.Film;
import com.example.demo.entity.Interaction;
import com.example.demo.service.FilmService;
import com.example.demo.service.InteractionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value="/film")
public class FilmController  {
    @Resource
    private FilmService filmService;
    @Resource
    private InteractionService interactionService;


    @RequestMapping(value = "/{filmId}",method = RequestMethod.GET)
    public String loadFilmPage(@PathVariable String filmId, Model model, HttpSession session){
        model.addAttribute("user",session.getAttribute("user"));
        List<Film> films = filmService.findByFilmId(Long.parseLong(filmId));
        model.addAttribute("films",films);
        return "film";
    }

    @ResponseBody // 作为response，不跳转
    @RequestMapping(value = "/{filmId}",method = RequestMethod.POST)
    public HashMap<String, String> collectInterest(@RequestBody Interaction interaction, @PathVariable String filmId, Model model, HttpSession session){
        boolean valid = interactionService.save(interaction);
        HashMap<String,String> map = new HashMap<>();
        if(valid) {
            map.put("valid","true");
            map.put("msg","评价成功，谢谢反馈");
        }
        else {
            map.put("valid","false");
            map.put("msg","您已经评价过该影片了");
        }
        return map;
    }

}

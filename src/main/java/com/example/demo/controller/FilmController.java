package com.example.demo.controller;

import com.example.demo.entity.Favorite;
import com.example.demo.entity.Film;
import com.example.demo.entity.Interaction;
import com.example.demo.service.FavoriteService;
import com.example.demo.service.FilmService;
import com.example.demo.service.InteractionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value="/film")
public class FilmController  {
    @Resource
    private FilmService filmService;
    @Resource
    private InteractionService interactionService;
    @Resource
    private FavoriteService favoriteService;


    @RequestMapping(value = "/{filmId}",method = RequestMethod.GET)
    public String loadFilmPage(@PathVariable String filmId, Model model, HttpSession session) throws IOException {
        model.addAttribute("user",session.getAttribute("user"));
        List<Film> films = filmService.findByFilmId(Long.parseLong(filmId));
        model.addAttribute("films",films);
        List<Film> similarFilms = filmService.findSimilarFilms(filmId,"localhost",8848);
        model.addAttribute("similarFilms",similarFilms);
        return "film";
    }

    @ResponseBody // 作为response，不跳转
    @RequestMapping(value = "/{filmId}",method = RequestMethod.POST)
    public HashMap<String, String> collectInterest(@RequestBody Interaction interaction, @PathVariable String filmId, Model model, HttpSession session){
        HashMap<String,String> map = new HashMap<>();
        boolean valid;
        if(interaction.getInterested()!=2)
        {
            valid = interactionService.save(interaction);
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
        Favorite newFavorite = new Favorite();
        newFavorite.setFilmId(interaction.getFilmId());
        newFavorite.setUsername(interaction.getUsername());
        valid = favoriteService.save(newFavorite);
        if(valid){
            map.put("valid","true");
            map.put("msg","已经添加进您的收藏夹，可以单击右上角的用户名进入主页查看");
        }else{
            map.put("valid","false");
            map.put("msg","已经在您的收藏夹里了");
        }
        return map;
    }


    @RequestMapping(value = "/tags/{tag}",method = RequestMethod.GET)
    public String loadTags(@PathVariable("tag") String tag,Model model) {
        List<Film> films = filmService.findAllByGenresContains(tag);
        model.addAttribute("key",tag);
        if(films.size()==0){
            model.addAttribute("found",false);
            return "tag";
        }
        model.addAttribute("found",true);
        model.addAttribute("films",films);
        return "tag";
    }

    @RequestMapping(value = "/types/{type}",method = RequestMethod.GET)
    public String loadTypes(@PathVariable("type") String type,Model model){
        List<Film> films = filmService.findAllByTypeContains(type);
        model.addAttribute("key",type);
        if(films.size()==0){
            model.addAttribute("found",false);
            return "tag";
        }
        model.addAttribute("found",true);
        model.addAttribute("films",films);
        return "tag";
    }

    @RequestMapping(value = "/areas/{area}",method = RequestMethod.GET)
    public String loadAreas(@PathVariable("area") String area,Model model) {
        List<Film> films = filmService.findAllByAreaStartsWith(area);
        model.addAttribute("key",area);
        if(films.size()==0){
            model.addAttribute("found",false);
            return "tag";
        }
        model.addAttribute("found",true);
        model.addAttribute("films",films);
        return "tag";
    }

    @RequestMapping(value="/area/*",method = RequestMethod.POST)
    public String search1(@RequestParam("search") String search,Model model){
        List<Film> result = filmService.findAllByTitleContains(search);
        model.addAttribute("result",null);
        model.addAttribute("result",result);
        return "main";
    }
    @RequestMapping(value="/tags/*",method = RequestMethod.POST)
    public String search2(@RequestParam("search") String search,Model model){
        return search1(search,model);
    }
    @RequestMapping(value="/types/*",method = RequestMethod.POST)
    public String search3(@RequestParam("search") String search,Model model){
        return search1(search,model);
    }

}

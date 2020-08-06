package com.example.demo.service;

import com.example.demo.entity.Favorite;
import com.example.demo.entity.Film;
import com.example.demo.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FavoriteService {
    @Resource
    private FavoriteRepository favoriteRepository;
    @Resource
    private FilmService filmService;

    // 返回用户的收藏列表,至多limit条
    public List<Film> findAllFilmsAddedByUsername(String username,int limit){
        List<Favorite> list = favoriteRepository.findAllByUsername(username);
        List<Film> result=new ArrayList<>();
        for(Favorite item:list){
            result.add(filmService.findOneByFilmId(item.getFilmId()));
        }
        if(result.size()>limit) return result.subList(0,limit);
        return result;
    }

    // 加入收藏夹
    public boolean save(Favorite favoriteList){
        if(favoriteRepository.findAllByUsernameAndFilmId(favoriteList.getUsername(),favoriteList.getFilmId()).size()>0) // 记录已经存在
            return false;
        favoriteRepository.save(favoriteList);
        return true;
    }
}

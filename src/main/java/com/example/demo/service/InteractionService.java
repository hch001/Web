package com.example.demo.service;

import com.example.demo.entity.Film;
import com.example.demo.entity.Interaction;
import com.example.demo.repository.InteractionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class InteractionService {
    @Resource
    private InteractionRepository interactionRepository;
    @Resource
    private FilmService filmService;

    static private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 保存
    public boolean save(Interaction interaction) {
        if(interactionRepository.findAllByUsernameAndFilmId(interaction.getUsername(),interaction.getFilmId()).size()>0) // 已经存在
            return false;
        interaction.setRatingTime(df.format(new Date()));
        interactionRepository.save(interaction);
        return true;
    }

    // 返回至多limit条用户喜欢的影片
    public List<Film> findAllLikedFilmsByUsernameWithLimit(String username,int limit){
        List<Interaction> histories =interactionRepository.findAllLikedByUsername(username);
        List<Film> result = new ArrayList<>();
        for (Interaction record : histories) {
            result.add(filmService.findOneByFilmId(record.getFilmId()));
        }
        if(result.size()>limit) return result.subList(0,limit);
        return result;
    }

    
}

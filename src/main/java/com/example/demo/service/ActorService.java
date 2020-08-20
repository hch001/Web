package com.example.demo.service;

import com.example.demo.entity.Actor;
import com.example.demo.repository.ActorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class ActorService {
    @Resource
    private ActorRepository actorRepository;

    public String getActorsNameActedInFilm(Long filmId){
        List<Actor> actors= actorRepository.findAllByFilmId(filmId);
        if(actors.size()==0) return null;
        // 合并演员的名字
        StringBuilder s = new StringBuilder();
        String tmp;
        for(int i=0;i<actors.size();i++)
        {
            tmp=actors.get(i).getName();
            if(i!=0&&tmp!=null)
                s.append("/");
            if(tmp!=null) s.append(tmp);
        }

        // 空值则返回null
        return (s.indexOf("\0")==0)?null:s.toString();

    }
}

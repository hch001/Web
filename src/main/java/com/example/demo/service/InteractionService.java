package com.example.demo.service;

import com.example.demo.entity.Interaction;
import com.example.demo.repository.InteractionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Transactional
public class InteractionService {
    @Resource
    private InteractionRepository interactionRepository;
    static private SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public boolean save(Interaction interaction) {
        if(interactionRepository.findAllByUsernameAndFilmId(interaction.getUsername(),interaction.getFilmId()).size()>0) // 已经存在
            return false;
        interaction.setRatingTime(df.format(new Date()));
        interactionRepository.save(interaction);
        return true;
    }
}

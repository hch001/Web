package com.example.demo.service;

import com.example.demo.entity.Film;
import com.example.demo.repository.FilmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class FilmService {
    @Resource
    private FilmRepository filmRepository;

    // 根据标题查询所有影片
    public List<Film> findAllByTitleContains(String title){
        return filmRepository.findAllByTitleContains(title);
    }

    // 根据id查询所有影片
    public List<Film> findByFilmId(Long filmId){
        return filmRepository.findByFilmId(filmId);
    }

    // 返回一部,根据id查询影片
    public Film findOneByFilmId(Long filmId){
        List<Film> films = filmRepository.findByFilmId(filmId);
        return films.size()>0?films.get(0):null;
    }

    // 返回limit条评分高于rating的movie，按日期排序
    public List<Film> findAllMoviesByRatingGreaterThanAndOrderByDateWithLimit(int rating,int limit){
        return filmRepository.findAllMoviesByRatingGreaterThanAndOrderByDateWithLimit(rating,limit);
    }

    // tv同上
    public List<Film> findAllTvsByRatingGreaterThanAndOrderByDateWithLimit(int rating,int limit) {
        return filmRepository.findAllTvsByRatingGreaterThanAndOrderByDateWithLimit(rating,limit);
    }

    // 包含genre的影片，按评分和日期排序
    public List<Film> findAllByGenresContains(String genre) {
        return filmRepository.findAllByGenresContainsAndOrderByRatingAndDate(genre);
    }

    // 某种类型，按评分和日期排序
    public List<Film> findAllByTypeContains(String type){
        return filmRepository.findAllByTypeContainsAndOrderByRatingAndDate(type);
    }

    // 查询某地区的影片，按评分和日期排序
    public List<Film> findAllByAreaStartsWith(String area){

        if(area.equals("香港")){
            return merge("香港","中国香港");
        }
        return filmRepository.findAllByAreaStartsWithAndOrderByRatingAndDate(area);
    }

    // 地区有多个名称，合并这些
    public List<Film> merge(String ...areas){
        Set<Film> s=new HashSet<>();
        for(String area:areas){
            s.addAll(filmRepository.findAllByAreaStartsWithAndOrderByRatingAndDate(area));
        }
        return new ArrayList<>(s);
    }

    // 向python脚本发送请求返回离线计算出的相似的电影
    public List<Film> findSimilarFilms(String filmId,String host,int port) throws IOException {
        Socket socket = new Socket(host,port);
        socket.getOutputStream().write(filmId.getBytes());

        byte[] bytes = new byte[200];
        socket.getInputStream().read(bytes);

        String s = new String(bytes);
        String result = s.substring(0,s.indexOf("\0"));

        if(result.equals("未找到")) {
            Film f = filmRepository.findByFilmId(Long.parseLong(filmId)).get(0);
            String g = f.getGenres(), l=f.getLanguage();
            if(g.length()<=1) return null; // 过于小众且无标签，无法推荐
            if(g.length()>5) g=g.substring(0,5);
            return filmRepository.findAllByGenresContainingAndOrderByRatingAndDateWithLimit(g,l,20,5);
        }

        Pattern pattern = Pattern.compile("\\d+");
        Matcher m = pattern.matcher(result);

        // 查询
        List<Film> films = new ArrayList<>();
        while(m.find()){
            films.add(findOneByFilmId(Long.valueOf(m.group())));
        }
        return films;
    }


}

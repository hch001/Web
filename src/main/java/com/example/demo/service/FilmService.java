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

    // 标签的流行程度 从小到大 越小优先级越高
    static private List<String> rank = new ArrayList<>();
    static{
        rank.add("鬼怪");
        rank.add("灾难");
        rank.add("短片");
        rank.add("运动");
        rank.add("武侠");
        rank.add("历史");
        rank.add("古装");
        rank.add("战争");
        rank.add("家庭");
        rank.add("奇幻");
        rank.add("科幻");
        rank.add("冒险");
        rank.add("恐怖");
        rank.add("悬疑");
        rank.add("犯罪");
        rank.add("动作");
        rank.add("爱情");
        rank.add("喜剧");
        rank.add("剧情");
    }

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
    public List<Film> findSimilarFilms(String filmId,String host,int port,int buffSize,int limit) throws IOException {
        Socket socket = new Socket(host,port);
        socket.getOutputStream().write(filmId.getBytes());

        byte[] bytes = new byte[buffSize];
        socket.getInputStream().read(bytes);

        // 获取filmId
        String s = new String(bytes);
        String result = s.substring(0,s.indexOf("\0"));

        // 无法通过计算得出相似的影片
        if(result.equals("未找到")) {
            Film f = filmRepository.findByFilmId(Long.parseLong(filmId)).get(0);
            return getDefaultSimilarFilm(f,limit);
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

    // 计算不出相似度结果的影片采用该方法推荐
    public List<Film> getDefaultSimilarFilm(Film f,int limit) {
        System.out.println("调用默认方法");
        String g = f.getGenres(), l=f.getLanguage();
        if(g.length()<=1||l.length()<=1) return null; // 过于小众且无标签，无法推荐

        // genres 选出最多2个最小众的标签
        List<String> genres = new ArrayList<>();
        for(int i=0,count=0;count<2&i<rank.size();i++){
            if(g.contains(rank.get(i))){
                genres.add(rank.get(i));
                count+=1;
            }
        }
        if(genres.size()==0) return null;

        // 根据获取的标签数量选择方法
        List<Film> films = (genres.size()==1)?(filmRepository.findAllByOneGenreAndLanguageWithLimit(genres.get(0),l,6)):(filmRepository.findAllByTwoGenreAndLanguageWithLimit(genres.get(0),genres.get(1),l,limit+1));

        System.out.println("films:"+films.get(0).getFilmId());
        // 过滤本身
        return filter(films,f.getFilmId(),limit);
    }

    // 过滤本身
    public List<Film> filter(List<Film> films,Long filmId,int maxSize) {
        films.removeIf(f -> f.getFilmId().equals(filmId));

        return (films.size()==0)?null:((films.size()>maxSize)?films.subList(0,maxSize):films);
    }

}

package com.example.demo.service;

import com.example.demo.entity.Film;
import com.example.demo.entity.User;
import com.example.demo.repository.FavoriteRepository;
import com.example.demo.repository.InteractionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class UserService {
    @Resource
    private UserRepository userRepository;
    @Resource
    private FavoriteRepository favoriteRepository;
    @Resource
    private InteractionRepository interactionRepository;
    @Resource
    private FilmService filmService;

    // 根据用户名和密码查找，用于登录判断
    public List<User> findUsersByUsernameAndPassword(String username, String password) {
        return userRepository.findUsersByUsernameAndPassword(username,password);
    }

    // 查找用户名是否存在，用于注册判断
    public List<User> findUsersByUsername(String username) {
        return userRepository.findUsersByUsername(username);
    }

    // 保存新用户
    public int save(User user){
        String username=user.getUsername();
        String password=user.getPassword();
        List<User> users = findUsersByUsername(username);
        boolean check = checkRegister(username,password);

        if(!checkSecurity(username,password)) return 3; // 错误3 存在非法字符
        else if(!check) return 2; // 错误2 格式不正确
        else if(users.size()!=0)  return 1; //错误1 已经存在

        userRepository.save(user);
        return 0; // 正确
    }

    // 用户名和密码格式检查
    public boolean checkRegister(String username,String password){
        int complexity = 0;
        // 存在大写字母
        for(int i=0;i<password.length();i++){
            if(password.charAt(i)>='A'&&password.charAt(i)<='Z'){
                complexity++;break;
            }
        }
        // 存在数字
        for(int i=0;i<password.length();i++){
            if(password.charAt(i)>='0'&&password.charAt(i)<='9'){
                complexity++;break;
            }
        }

        return username.length()<=33 && username.length() >= 2 && password.length() >= 8 && password.length()<=33 && complexity >= 2 ;
    }

    // 检测非法字符(目前只支持数字和大小写字母)
    public boolean checkSecurity(String username,String password){
        return isValidStr(username) && isValidStr(password);
    }

    // 判断一个字符串是否合法
    public boolean isValidStr(String s){
        char c;
        for(int i=0;i<s.length();i++){
            c=s.charAt(i);
            if((c>='0'&&c<='9')||(c>='a'&&c<='z')||(c>='A'&&c<='Z')) // 合法继续循环 非法直接返回false
                continue;
            return false;
        }
        return true;
    }

    // 返回用户有关的所有电影
    public List<String> getFilmIdsUserLike(String username) {
        HashSet<String> filmIds = new HashSet<>();

        favoriteRepository.findAllByUsername(username).forEach((f)->{
            filmIds.add(f.getFilmId().toString());
        });
        interactionRepository.findAllByUsername(username).forEach((i)->{
            filmIds.add(i.getFilmId().toString());
        });

        return filmIds.size()==0?null:new ArrayList<>(filmIds);
    }

    // 根据用户的历史记录来推荐
    public List<Film> getUserBasedRecommendation(String username,String host,int port,int buffSize,int limit) {
        List<String> filmIds = getFilmIdsUserLike(username);
        if(filmIds==null) return null;

        // filmRank 相似的电影出现的频率
        HashMap<String,Integer> filmRank = new HashMap<>();
        filmIds.forEach((filmId)->{
            try {
                List<Film > tmp = filmService.findSimilarFilms(filmId,host,port,buffSize,limit);
                if(tmp!=null){
                    tmp.forEach((f)->{
                        if(filmRank.containsKey(f.getFilmId().toString())) {
                            filmRank.put(f.getFilmId().toString(),filmRank.get(f.getFilmId().toString())+1);
                        }
                        else {
                            filmRank.put(f.getFilmId().toString(),1);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        if(filmRank.size()==0) return null;


        List<Film> result = new ArrayList<>();
        // l 是排序后的电影列表
        List<Map.Entry<String,Integer>> l = new ArrayList<>(filmRank.entrySet());
        l.sort((a,b)->b.getValue()-a.getValue());


        l.forEach((f)->{
            result.add(filmService.findOneByFilmId(Long.parseLong(f.getKey())));
//            System.out.println(f.getKey()+","+f.getValue());
        });

        return (result.size()>limit)?result.subList(0,limit):result;

    }


}

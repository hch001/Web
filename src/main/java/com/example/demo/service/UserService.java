package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class UserService {
    @Resource
    private UserRepository userRepository;

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

}

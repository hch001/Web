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

    public List<User> findUsersByUsernameAndPassword(String username, String password) {
        return userRepository.findUsersByUsernameAndPassword(username,password);
    }

    public List<User> findUsersByUsername(String username) {
        return userRepository.findUsersByUsername(username);
    }

    public int save(User user){
        String username=user.getUsername();
        String password=user.getPassword();
        List<User> users = findUsersByUsername(username);
        boolean check = checkRegister(username,password);
        if(!check){
            return 2; // 错误2
        }
        else if(users.size()!=0){
            return 1; //错误1
        }
        userRepository.save(user);
        return 0; // 正确
    }

    public boolean checkRegister(String username,String password){
        int complex = 0;
        for(int i=0;i<password.length();i++){
            if(password.charAt(i)>='A'&&password.charAt(i)<='Z'){
                complex++;break;
            }
        }
        for(int i=0;i<password.length();i++){
            if(password.charAt(i)>='0'&&password.charAt(i)<='9'){
                complex++;break;
            }
        }

        return username.length() >= 2 && password.length() >= 8 && complex >= 2;
    }
}

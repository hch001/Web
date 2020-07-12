package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    private UserRepository userRepository;

    public List<User> findUsersByUsernameAndPassword(String username, String password) {
        return userRepository.findUsersByUsernameAndPassword(username,password);
    }

    public List<User> findUsersByUsername(String username) {
        return userRepository.findUsersByUsername(username);
    }

    public User save(User user){
        return userRepository.save(user);
    }
}

package com.example.savemoney.service;

import com.example.savemoney.entity.User;
import com.example.savemoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean checkId(String username) {
        boolean exists = userRepository.existsByUsername(username);
        return exists;
    }

    public User getUserInfo(String username) {
        try{
            return userRepository.findByUsername(username);
        }catch(Exception e){
            return null;
        }
    }

    public boolean deleteUser(String username) {
        User user = userRepository.findByUsername(username);
        if(user != null) {
            userRepository.delete(user);
            return true;
        }
        return false;
    }


}

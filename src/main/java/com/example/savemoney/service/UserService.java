package com.example.savemoney.service;

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
}

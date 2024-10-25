package com.example.savemoney.service;

import com.example.savemoney.dto.SigninDTO;
import com.example.savemoney.dto.SignupDTO;
import com.example.savemoney.entity.User;
import com.example.savemoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean signup(SignupDTO user) {
        boolean isExists = userRepository.existsByUsername(user.getUsername());

        if (!isExists) {
            User newUser = User.builder()
                    .username(user.getUsername())
                    .password(bCryptPasswordEncoder.encode(user.getPassword()))
                    .name(user.getName())
                    .age(user.getAge())
                    .email(user.getEmail())
                    .phone(user.getPhone())
                    .gender(user.getGender())
                    .gameLife(5)
                    .build();

            userRepository.save(newUser);
            return true;
        }
        return false;
    }

//    public boolean signin(SigninDTO user) {
//
//    }
}

package com.example.savemoney.service;

import com.example.savemoney.dto.SignupDTO;
import com.example.savemoney.entity.Milage;
import com.example.savemoney.entity.User;
import com.example.savemoney.repository.MilageRepository;
import com.example.savemoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final MilageRepository milageRepository;

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
                    .monthlySpendLimit(500000)
                    .build();

            userRepository.save(newUser);

            // 유저 마일리지 지갑 생성
            Milage milage= Milage.builder()
                    .balance(0)
                    .user(newUser)
                    .build();

            milageRepository.save(milage);

            return true;
        }
        return false;
    }


}

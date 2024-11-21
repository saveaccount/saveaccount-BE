package com.example.savemoney.service;

import com.example.savemoney.dto.UserUpdateDTO;
import com.example.savemoney.entity.User;
import com.example.savemoney.repository.StatementRepository;
import com.example.savemoney.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder; //BCryptPasswordEncoder를 사용하여 비밀번호 암호화

    private final StatementRepository statementRepository;

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

    @Transactional
    public User updateUser(String username, UserUpdateDTO userUpdateDTO){
        // 사용자 조회
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("해당 사용자가 존재하지 않습니다.");
        }
        // 비밀번호와 비밀번호 확인이 일치하는지 검증
        if(userUpdateDTO.getPassword() != null && !userUpdateDTO.getPassword().equals(userUpdateDTO.getPasswordConfirm())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        String encryptedPassword = null;
        if(userUpdateDTO.getPassword() != null){
            encryptedPassword = bCryptPasswordEncoder.encode(userUpdateDTO.getPassword());
        }

        // 전용 메서드를 통해 필요한 필드 업데이트
        user.updateUserInfo(
                userUpdateDTO.getEmail(),
                encryptedPassword,
                userUpdateDTO.getName(),
                userUpdateDTO.getGender(),
                userUpdateDTO.getAge(),
                userUpdateDTO.getPhone()
        );
        return user; //트랜잭션이 끝나면 변경사항이 자동으로 커밋
    }

    @Transactional
    public Integer getMonthlySpendLimit() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        // Adjust the monthly spending limit before returning it
        int newLimit=0;
        // Step 1: Calculate the total amount spent in the past month
        LocalDateTime oneMonthAgo = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);

        int monthlySpend = statementRepository.calculateMonthlySpend(user.getId(), oneMonthAgo);

        // Step 2: Determine adjustment rate and target limit based on spend amount
        double adjustmentRate = 0.0;
        int targetLimit = user.getMonthlySpendLimit();  // Current limit as a starting target
        if (monthlySpend > 1000000) {
            adjustmentRate = 0.05;
            targetLimit = 700000;
        } else if (monthlySpend > 700000) {
            adjustmentRate = 0.04;
            targetLimit = 500000;
        } else if (monthlySpend > 500000) {
            adjustmentRate = 0.03;
            targetLimit = 400000;
        }
        else {
            adjustmentRate = 0;
            newLimit = 400000;
        }
        // Step 3: Adjust the monthly spending limit until reaching the target
        if (adjustmentRate > 0) {
            newLimit = (int) (user.getMonthlySpendLimit() * (1 - adjustmentRate));
        }
        log.info("Monthly spend: {}, target limit: {}, new limit: {}", monthlySpend, targetLimit, newLimit);
        user.updateMonthlySpendLimit(Math.max(newLimit, targetLimit));

        return user.getMonthlySpendLimit();
    }
}

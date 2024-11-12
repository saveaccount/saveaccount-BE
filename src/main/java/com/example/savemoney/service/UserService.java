package com.example.savemoney.service;

import com.example.savemoney.dto.UserUpdateDTO;
import com.example.savemoney.entity.User;
import com.example.savemoney.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final SpendingAdjustmentService spendingAdjustmentService;


    private final BCryptPasswordEncoder bCryptPasswordEncoder; //BCryptPasswordEncoder를 사용하여 비밀번호 암호화

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
                userUpdateDTO.getPhone(),
                userUpdateDTO.getMonthlySpendLimit()
        );
        return user; //트랜잭션이 끝나면 변경사항이 자동으로 커밋
    }

    public Integer getMonthlySpendLimit(String username) {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        // Adjust the monthly spending limit before returning it
        spendingAdjustmentService.adjustMonthlySpendLimit(user);

        // Save the updated user to persist the adjusted limit
        userRepository.save(user);

        return user.getMonthlySpendLimit();
    }
}

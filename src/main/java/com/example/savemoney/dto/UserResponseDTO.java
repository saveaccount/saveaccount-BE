package com.example.savemoney.dto;

import com.example.savemoney.entity.User;
import com.example.savemoney.enumeration.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private String username;
    private String email;
    private String name;
    private String phone;
    private int age;
    private Gender gender;

    // User 객체를 인자로 받아서 필드를 초기화하는 생성자
    public UserResponseDTO(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.age = user.getAge();
        this.gender = user.getGender();
    }
}
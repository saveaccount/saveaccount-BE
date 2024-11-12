package com.example.savemoney.dto;

import com.example.savemoney.enumeration.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDTO {

    private String email;
    private String password;
    private String passwordConfirm;
    private String name;
    private Gender gender;
    private int age;
    private String phone;
    private int monthlySpendLimit;
}

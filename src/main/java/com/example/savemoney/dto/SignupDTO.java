package com.example.savemoney.dto;

import com.example.savemoney.enumeration.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {

    private String username;
    private String password;
    private String email;
    private String name;
    private String phone;
    private int age;
    private Gender gender;

}

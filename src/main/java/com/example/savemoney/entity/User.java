package com.example.savemoney.entity;

import com.example.savemoney.enumeration.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private Gender gender;

    @Column
    private int gameLife;

    @Builder
    public User(Long id, String username, String password, String name,
                String email, String phone, int age, Gender gender, int gameLife) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
        this.gameLife = gameLife;
    }


}

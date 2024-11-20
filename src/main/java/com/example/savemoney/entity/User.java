package com.example.savemoney.entity;

import com.example.savemoney.enumeration.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
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

    @Column
    private int monthlySpendLimit;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Gifticon_relation> gifticons = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Mileage mileage;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Statement> statements = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Account> accounts = new ArrayList<>();

    @Builder
    public User(String username, String password, String name,
                String email, String phone, int age, Gender gender, int gameLife, int monthlySpendLimit) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.gender = gender;
        this.gameLife = gameLife;
        this.monthlySpendLimit = monthlySpendLimit;
    }

    // 특정 필드를 한 번에 업데이트하는 전용 메서드
    public void updateUserInfo(String email, String password, String name, Gender gender, Integer age, String phone) {
        if (email != null) this.email = email;
        if (password != null) this.password = password;
        if (name != null) this.name = name;
        if (gender != null) this.gender = gender;
        if (age != null) this.age = age;
        if (phone != null) this.phone = phone;
    }
}

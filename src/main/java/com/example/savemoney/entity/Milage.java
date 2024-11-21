package com.example.savemoney.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Milage extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "username")
    private User user;

    @Column(nullable = false)
    private int balance;

    @Builder
    public Milage(User user, int balance) {
        this.user = user;
        this.balance = balance;
    }

    public void updateBalance(int balance) {
        this.balance += balance;
    }
}

package com.example.savemoney.entity;

import com.example.savemoney.enumeration.AccountType;
import com.example.savemoney.enumeration.BankType;
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
public class Account extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @Column(nullable = false, unique = true)
    private String account_num;

    @Column(nullable = false)
    private AccountType type;

    @Column(nullable = false)
    private String pw;

    @Column(nullable = false)
    private BankType bank;

    @Column(nullable = false)
    private int balance;

    @OneToMany(mappedBy = "senderAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Statement> statements=new ArrayList<>();

    @Builder
    public Account(User user, String account_num, AccountType type, String pw, BankType bank, int balance) {
        this.user = user;
        this.account_num = account_num;
        this.type = type;
        this.pw = pw;
        this.bank = bank;
        this.balance = balance;
    }
}

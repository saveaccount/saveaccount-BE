package com.example.savemoney.entity;

import com.example.savemoney.enumeration.AccountType;
import com.example.savemoney.enumeration.BankType;
import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private String account_num;

    @Column(nullable = false)
    private AccountType type;

    @Column(nullable = false)
    private String pw;

    @Column(nullable = false)
    private BankType bank;

    // balance setter 추가
    @Setter
    @Column(nullable = false)
    private int balance;

    @OneToMany(mappedBy = "senderAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Statement> statements=new ArrayList<>();

    // 생성자 추가
    @Builder
    public Account(String accountNum, BankType bank, User user, String pw, AccountType type, int balance) {
        this.account_num = accountNum;
        this.bank = bank;
        this.user = user;
        this.pw = pw;
        this.type = type;
        this.balance = balance;
    }

}

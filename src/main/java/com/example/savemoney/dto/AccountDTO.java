package com.example.savemoney.dto;

import com.example.savemoney.enumeration.AccountType;
import com.example.savemoney.enumeration.BankType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDTO {
    private Long id;
    private String accountNum; // 계좌 번호
    private AccountType type;  // 계좌 타입
    private BankType bank;     // 은행 타입
    private int balance;       // 잔액
    private String userName;   // 유저 이름
}

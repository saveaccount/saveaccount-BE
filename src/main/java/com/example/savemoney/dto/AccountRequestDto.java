package com.example.savemoney.dto;

import com.example.savemoney.enumeration.AccountType;
import com.example.savemoney.enumeration.BankType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequestDto {
    private String accountNum;
    private BankType bank;
    private String user;
    private String pw;
    private AccountType type;
}

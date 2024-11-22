package com.example.savemoney.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountEnrollDTO {
    private String username;
    private String accountNum;
    private int type; //0: 개인 계좌, 1 : 모임 계좌
    private String pw;
    private String bank;
    private int balance;


}

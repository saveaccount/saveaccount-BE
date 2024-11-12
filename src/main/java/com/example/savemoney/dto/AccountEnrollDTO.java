package com.example.savemoney.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountEnrollDTO {
    private String accountNum;
    private String bank;
    private String username;
    private String pw;
    private int type; //0: 개인 계좌, 1 : 모임 계좌
}

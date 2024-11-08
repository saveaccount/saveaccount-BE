package com.example.savemoney.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferRequestDto {
    private Long senderAccountId;
    private Long receiverAccountId;
    private String pw;
    private int amount;
    private String memo;
}

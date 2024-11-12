package com.example.savemoney.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDTO {

    private int amount;
    private String senderAccountNum;
    private String receiverAccountNum;;
    private String accountPassword;
}

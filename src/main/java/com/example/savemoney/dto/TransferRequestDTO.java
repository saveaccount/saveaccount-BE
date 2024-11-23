package com.example.savemoney.dto;

import com.example.savemoney.enumeration.ExpenseType;
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
    private ExpenseType category;
    private String memo;
}

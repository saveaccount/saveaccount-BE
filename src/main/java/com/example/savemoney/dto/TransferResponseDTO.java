package com.example.savemoney.dto;

import com.example.savemoney.enumeration.ExpenseType;
import com.example.savemoney.enumeration.StatementType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TransferResponseDTO {
    private Long statementId;
    private String memo;
    private StatementType statementType;;
    private ExpenseType expenseType;
    private int amount;
    private String senderAccountNum;
    private int senderBalance;
    private String receiverAccountNum;
    private int receiverBalance;
}

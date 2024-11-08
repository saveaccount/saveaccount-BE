package com.example.savemoney.dto;

import com.example.savemoney.enumeration.StatementType;
import com.example.savemoney.enumeration.ExpenseType;

public class StatementResponseDto {

    private String memo;
    private StatementType statementType;
    private ExpenseType expenseType;
    private int amount;
    private int senderBalance;
    private int receiverBalance;

    public StatementResponseDto(String memo, StatementType statementType, ExpenseType expenseType, int amount, int senderBalance, int receiverBalance) {
        this.memo = memo;
        this.statementType = statementType;
        this.expenseType = expenseType;
        this.amount = amount;
        this.senderBalance = senderBalance;
        this.receiverBalance = receiverBalance;
    }

    // getters and setters (optional, depending on how you want to expose the data)
}

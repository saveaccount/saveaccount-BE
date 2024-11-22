package com.example.savemoney.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class StatementResponseDTO {
    private Long id;
    private String memo;
    private String statementType;
    private String expenseType;
    private int amount;
    private LocalDateTime createdAt;
}
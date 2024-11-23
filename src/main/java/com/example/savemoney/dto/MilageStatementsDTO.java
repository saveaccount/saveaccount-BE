package com.example.savemoney.dto;

import com.example.savemoney.enumeration.StatementType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MilageStatementsDTO {


    private int amount;
    private String memoji;
    private LocalDateTime createdAt;

}

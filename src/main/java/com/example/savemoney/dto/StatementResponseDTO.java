package com.example.savemoney.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class StatementResponseDTO {
    private String sender_num;
    private String receiver_num;
    private int amount;
    private LocalDateTime createdAt;
}
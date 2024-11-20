package com.example.savemoney.dto;


import com.example.savemoney.entity.Mileage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MileageRequestDTO {

    private String username;
    private int balance;
    private int ticketCount;
}

package com.example.savemoney.dto;


import com.example.savemoney.entity.Mileage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MileageResponseDTO {

    private long id;

    private String username;
    private int balance;
    private int ticketCount;

    //User entity의 username과 mileage balance에 접근
    public MileageResponseDTO(Mileage mileage){
        this.username = mileage.getUser().getUsername();
        this.balance = getBalance();
        this.ticketCount = getTicketCount();
    }
}
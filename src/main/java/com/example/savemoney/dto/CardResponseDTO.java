package com.example.savemoney.dto;

import com.example.savemoney.entity.Card;
import com.example.savemoney.enumeration.ExpenseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDTO {
    //카드를 클라이언트에 반환하기 위한 DTO
    private String name;
    private String bank;
    private ExpenseType category;
    private String privilege;

    public CardResponseDTO(Card card){
        this.name=card.getName();
        this.bank=card.getBank().toString();
        this.category=card.getCategory();
        this.privilege=card.getPrivilege();
    }

}

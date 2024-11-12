package com.example.savemoney.entity;

import com.example.savemoney.enumeration.BankType;
import com.example.savemoney.enumeration.ExpenseType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private BankType bank;

    @Column(nullable = false)
    private ExpenseType category;

    @Column(nullable = false)
    private String privilege;

    @Builder
    public Card(String name, BankType bank, ExpenseType category, String privilege) {
        this.name = name;
        this.bank = bank;
        this.category = category;
        this.privilege = privilege;
    }
}

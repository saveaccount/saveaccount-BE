package com.example.savemoney.repository;

import com.example.savemoney.entity.Card;
import com.example.savemoney.enumeration.BankType;
import com.example.savemoney.enumeration.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    //카테고리와 은행을 기준으로 일치하는 카드 조회하는 메서드
    List<Card> findByCategoryAndBank(ExpenseType category, BankType bank);
}

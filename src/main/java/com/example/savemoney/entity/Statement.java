package com.example.savemoney.entity;

import com.example.savemoney.enumeration.ExpenseType;
import com.example.savemoney.enumeration.StatementType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Statement extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memo;

    @Column(nullable = false)
    private StatementType statementType;

    @Column(nullable = false)
    private ExpenseType expenseType;

    @Column(nullable = false)
    private int amount;

    // 송금 내역 발생 날짜
    @Column(nullable = false)
    private LocalDate date;

//    // 주 시작일과 종료일 추가 (주간 송금 내역을 조회할 때 사용)
//    @Column(nullable = false)
//    private LocalDate weekStartDate;
//
//    @Column(nullable = false)
//    private LocalDate weekEndDate;
//    @Column(nullable = false)
//    private Integer week;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @ManyToOne
    @JoinColumn(name = "senderAccount")
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "receiverAccount")
    private Account receiverAccount;

    @Builder
    public Statement(String memo, StatementType statementType, ExpenseType expenseType, int amount, Account senderAccount, Account receiverAccount) {
        this.memo = memo;
        this.statementType = statementType;
        this.expenseType = expenseType;
        this.amount = amount;
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
    }


}

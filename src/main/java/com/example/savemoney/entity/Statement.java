package com.example.savemoney.entity;

import com.example.savemoney.enumeration.ExpenseType;
import com.example.savemoney.enumeration.StatementType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = true)
    private ExpenseType expenseType;

    @Column(nullable = false)
    private int amount;

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
    public Statement(String memo, StatementType statementType, ExpenseType expenseType, int amount, Account senderAccount, Account receiverAccount, User user) {
        this.memo = memo;
        this.statementType = statementType;
        this.expenseType = expenseType;
        this.amount = amount;
        this.senderAccount = senderAccount;
        this.receiverAccount = receiverAccount;
        this.user = user;
    }


}

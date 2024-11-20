package com.example.savemoney.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Mileage extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "username")
    private User user;

    @Column(nullable = false)
    private int balance = 0;

    @Column(nullable = false)
    private int ticketCount = 0;

    @Builder
    public Mileage(User user, int balance, int ticketCount) {
        this.user = user;
        this.balance = balance;
        this.ticketCount = ticketCount;
    }
    public void setBalance(int balance) {
        this.balance = balance;
    }

    public void setTicketCount(int ticketCount) {
        this.ticketCount = ticketCount;
    }
}

package com.example.savemoney.service;

import com.example.savemoney.entity.User;
import com.example.savemoney.repository.StatementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import com.example.savemoney.dto.CustomUserDetails;
import com.example.savemoney.entity.User;
import com.example.savemoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class SpendingAdjustmentService {

    private final StatementRepository statementRepository;

    public SpendingAdjustmentService(StatementRepository statementRepository) {
        this.statementRepository = statementRepository;
    }

    @Transactional
    public void adjustMonthlySpendLimit(User user) {
        // Step 1: Calculate the total amount spent in the past month
        LocalDateTime oneMonthAgo = LocalDateTime.now().minus(1, ChronoUnit.MONTHS);
        int monthlySpend = statementRepository.calculateMonthlySpend(user.getId(), oneMonthAgo);

        // Step 2: Determine adjustment rate and target limit based on spend amount
        double adjustmentRate = 0.0;
        int targetLimit = user.getMonthlySpendLimit();  // Current limit as a starting target

        if (monthlySpend > 100) {
            adjustmentRate = 0.05;
            targetLimit = 70;
        } else if (monthlySpend > 70) {
            adjustmentRate = 0.04;
            targetLimit = 50;
        } else if (monthlySpend > 50) {
            adjustmentRate = 0.03;
            targetLimit = 40;
        }

        // Step 3: Adjust the monthly spending limit until reaching the target
        if (adjustmentRate > 0) {
            int newLimit = (int) (user.getMonthlySpendLimit() * (1 - adjustmentRate));
            user.setMonthlySpendLimit(Math.max(newLimit, targetLimit));
        }
    }
}

package com.example.savemoney.service;

import com.example.savemoney.entity.Account;
import com.example.savemoney.entity.Milage;
import com.example.savemoney.entity.Statement;
import com.example.savemoney.entity.User;
import com.example.savemoney.enumeration.ExpenseType;
import com.example.savemoney.enumeration.StatementType;
import com.example.savemoney.repository.AccountRepository;
import com.example.savemoney.repository.MilageRepository;
import com.example.savemoney.repository.StatementRepository;
import com.example.savemoney.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MilageService {

    private final MilageRepository milageRepository;

    private final StatementRepository statementRepository;

    private final UserRepository userRepository;

    private final AccountRepository accountRepository;

    /**
     *
     * @param amount
     * @return 입금 후 잔액
     */
    @Transactional
    public int putMilageIn(int amount) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // 사용자 조회
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다");
        }

        // 마일리지 적립 로직
        Milage milage = milageRepository.findByUser(user);
        milage.updateBalance(amount);

        // statement 생성
        Statement statement = Statement.builder()
                .user(user)
                .memo("목표 지출 금액 달성으로 인한 마일리지 적립")
                .statementType(StatementType.MILEAGE)
                .amount(amount)
                .build();

        // statement 저장
        statementRepository.save(statement);
        log.info("Statement 저장 완료");
        // 입금 후 잔액 반환
        int afterBalance = milageRepository.getMilage(user);
        log.info("마일리지 잔액 : {}", afterBalance);
        return afterBalance;
    }
}

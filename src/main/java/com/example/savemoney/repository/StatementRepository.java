package com.example.savemoney.repository;

import com.example.savemoney.entity.Statement;
import com.example.savemoney.entity.Account;
import com.example.savemoney.enumeration.ExpenseType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StatementRepository extends JpaRepository<Statement, Long> {

    // 1. 특정 사용자에 대해, 특정 계좌에서 송금된 내역을 조회
    List<Statement> findBySenderAccount(Account senderAccount);

    // 2. 특정 사용자에 대해, 특정 기간 내에 발생한 송금 내역 조회 (날짜 범위)
    List<Statement> findBySenderAccountInAndDateBetween(List<Account> accounts, LocalDate startDate, LocalDate endDate);

//    // 3. 특정 사용자에 대해, 특정 날짜에 발생한 송금 내역 조회 (하루 단위)
//    List<Statement> findByUser_UsernameAndDate(String username, LocalDate date);
//
//    // 4. 특정 사용자에 대해, 날짜 범위 내에서 송금된 내역 조회 (일, 주, 월)
//    List<Statement> findByUser_UsernameAndDateBetween(String username, LocalDate startDate, LocalDate endDate);
//
//    // 5. 특정 사용자에 대해, 특정 카테고리(지출 유형)에 따른 송금 내역 조회
//    List<Statement> findByUser_UsernameAndExpenseType(String username, ExpenseType expenseType);
//
//    // 6. 특정 날짜를 기준으로, 주간별 이체 내역 조회 (이 주의 시작일 ~ 오늘)
//    List<Statement> findByUser_UsernameAndWeek(String username, LocalDate weekStartDate);

}

package com.example.savemoney.service;

import com.example.savemoney.dto.StatementResponseDto;
import com.example.savemoney.dto.TransferRequestDto;
import com.example.savemoney.entity.Account;
import com.example.savemoney.entity.Statement;
import com.example.savemoney.repository.AccountRepository;
import com.example.savemoney.repository.StatementRepository;
import com.example.savemoney.repository.UserRepository;
import com.example.savemoney.enumeration.StatementType;
import com.example.savemoney.enumeration.ExpenseType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatementService {

    private final AccountRepository accountRepository;
    private final StatementRepository statementRepository;
    private final UserRepository userRepository;

    @Autowired
    public StatementService(AccountRepository accountRepository, StatementRepository statementRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.statementRepository = statementRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createStatement(TransferRequestDto transferRequestDto, Account senderAccount, Account receiverAccount) {
        // 이체 내역 생성
        Statement statement = Statement.builder()
                .memo(transferRequestDto.getMemo())
                .statementType(StatementType.MONEY)
                .expenseType(ExpenseType.TRANSPORTATION)
                .amount(transferRequestDto.getAmount())
                .senderAccount(senderAccount)
                .receiverAccount(receiverAccount)
                .build();

        // 이체 내역 저장
        statementRepository.save(statement);
    }

    // 1. 이번 달 전체 이체 내역 조회
    @Transactional
    public List<StatementResponseDto> getMainStatements(String userId) {
        // 현재 월의 시작 날짜와 끝 날짜 계산
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);

        // 사용자 계좌 목록 조회
        List<Account> accounts = accountRepository.findByUser_Username(userId);

        // 이번 달 이체 내역 조회
        List<Statement> statements = statementRepository.findBySenderAccountInAndDateBetween(accounts, startOfMonth, endOfMonth);

        // DTO로 변환
        return statements.stream()
                .map(statement -> new StatementResponseDto(
                        statement.getMemo(),
                        statement.getStatementType(),
                        statement.getExpenseType(),
                        statement.getAmount(),
                        statement.getSenderAccount().getBalance(),
                        statement.getReceiverAccount().getBalance()
                ))
                .collect(Collectors.toList());
    }

    // 2. 계좌별 이체 내역 조회
    @Transactional
    public List<StatementResponseDto> getAccountStatements(String userId, Long accountId) {
        // 사용자 계좌 조회
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        // 해당 계좌의 이체 내역 조회
        List<Statement> statements = statementRepository.findBySenderAccount(account);

        // DTO로 변환
        return statements.stream()
                .map(statement -> new StatementResponseDto(
                        statement.getMemo(),
                        statement.getStatementType(),
                        statement.getExpenseType(),
                        statement.getAmount(),
                        statement.getSenderAccount().getBalance(),
                        statement.getReceiverAccount().getBalance()
                ))
                .collect(Collectors.toList());
    }

    // 3. 이체 내역 분석 (일별/주간별/월별/분류별)
    @Transactional
    public List<StatementResponseDto> getAnalysisStatements(String userId, String period, String category, String date) {
        // 날짜 필터링
        LocalDate filterDate = (date != null) ? LocalDate.parse(date, DateTimeFormatter.ISO_DATE) : null;
        List<Statement> statements;

        // 조건에 맞는 이체 내역 조회
        if ("daily".equals(period)) {
            statements = getDailyStatements(userId, filterDate);
        } else if ("weekly".equals(period)) {
            statements = getWeeklyStatements(userId, filterDate);
        } else if ("monthly".equals(period)) {
            statements = getMonthlyStatements(userId);
        } else if ("category".equals(period)) {
            statements = getCategoryStatements(userId, category);
        } else {
            throw new IllegalArgumentException("Invalid period specified");
        }

        // DTO로 변환
        return statements.stream()
                .map(statement -> new StatementResponseDto(
                        statement.getMemo(),
                        statement.getStatementType(),
                        statement.getExpenseType(),
                        statement.getAmount(),
                        statement.getSenderAccount().getBalance(),
                        statement.getReceiverAccount().getBalance()
                ))
                .collect(Collectors.toList());
    }

    private List<Statement> getDailyStatements(String userId, LocalDate filterDate) {
        // 필터링된 날짜로 일별 이체 내역 조회
        if (filterDate != null) {
            return statementRepository.findByUser_UsernameAndDate(userId, filterDate);
        }
        return statementRepository.findByUser_UsernameAndDateBetween(userId, filterDate.minusDays(30), filterDate);
    }

    private List<Statement> getWeeklyStatements(String userId, LocalDate filterDate) {
        // 주간별로 이체 내역 조회
        if (filterDate != null) {
            return statementRepository.findByUser_UsernameAndWeek(userId, filterDate);
        }
        return statementRepository.findByUser_UsernameAndDateBetween(userId, LocalDate.now().minusWeeks(1), LocalDate.now());
    }

    // 월별 이체 내역을 가져오는 메서드
    public List<Statement> getMonthlyStatements(String userId) {
        // 현재 날짜를 기준으로 월별 시작과 종료 날짜를 계산
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);  // 이번 달 첫 날
        LocalDate endOfMonth = now.withDayOfMonth(now.lengthOfMonth());  // 이번 달 마지막 날

        // userId에 맞는 사용자 계좌의 이체 내역을 조회하여 반환
        return statementRepository.findByUser_UsernameAndDateBetween(userId, startOfMonth, endOfMonth);
    }
    private List<Statement> getCategoryStatements(String userId, String category) {
        // 분류별 이체 내역 조회
        ExpenseType expenseType = ExpenseType.valueOf(category.toUpperCase());
        return statementRepository.findByUser_UsernameAndExpenseType(userId, expenseType);
    }

}

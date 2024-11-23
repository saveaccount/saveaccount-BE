package com.example.savemoney.service;

import com.example.savemoney.dto.MilageStatementsDTO;
import com.example.savemoney.dto.StatementResponseDTO;
import com.example.savemoney.entity.Statement;
import com.example.savemoney.entity.User;
import com.example.savemoney.enumeration.StatementType;
import com.example.savemoney.repository.StatementRepository;
import com.example.savemoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository statementRepository;
    private final UserRepository userRepository;

    public int getCurrentMonthExpense(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        return statementRepository.calculateCurrentMonthExpense(user.getId(), startOfMonth, now);
    }
    public List<StatementResponseDTO> getCurrentMonthStatementsByAccount(String accountNum) {
        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        List<Statement> statements = statementRepository.findByAccountAndDateBetween(accountNum, startOfMonth, now);

        // Statement -> DTO 변환
        return statements.stream()
                .map(statement -> new StatementResponseDTO(
                        statement.getSenderAccount().getAccount_num(),
                        statement.getReceiverAccount().getAccount_num(),
                        statement.getAmount(),
                        statement.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public List<StatementResponseDTO> getCurrentMonthStatements(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        LocalDateTime startOfMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime now = LocalDateTime.now();

        List<Statement> statements = statementRepository.findByUserAndDateBetween(user.getId(), startOfMonth, now);

        // Statement -> DTO 변환
        return statements.stream()
                .map(statement -> new StatementResponseDTO(
                        statement.getSenderAccount().getAccount_num(),
                        statement.getReceiverAccount().getAccount_num(),
                        statement.getAmount(),
                        statement.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getCategorySpent(String username){
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new IllegalArgumentException("User not found");
        }

        // 날짜 구하기(지난 한달)
        LocalDate now = LocalDate.now();
        LocalDateTime lastMonthStart = now.minusMonths(1).withDayOfMonth(1).atStartOfDay();
        LocalDateTime lastMonthEnd = now.minusMonths(1).withDayOfMonth(now.minusMonths(1).lengthOfMonth()).atTime(23, 59, 59);

        // 저번 달 동안의 모든 소비내역 가져오기
        List<Statement> statements = statementRepository.findByUserAndDateBetween(user.getId(), lastMonthStart, lastMonthEnd);

        // 카테고리별로 소비한 금액 계산
        Map<String, Integer> categorySpent = new HashMap<>();
        for (Statement statement : statements){
            if(statement.getExpenseType() != null){
                String category = statement.getExpenseType().name(); // Enum -> String
                int amount = statement.getAmount();
                categorySpent.put(category, categorySpent.getOrDefault(category, 0) + amount);
            }
        }
        return categorySpent;

    }

    public List<Map<String, Integer>> getWeeklyTransferHistory (String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("User not found");
        }
        // 3달 ~ 현재
        LocalDateTime startDate = LocalDate.now().minusMonths(3).withDayOfMonth(1).atStartOfDay();
        LocalDateTime endDate = LocalDate.now().atTime(23, 59, 59);

        // 이체내역 조회
        List<Statement> statements = statementRepository.findByUserAndDateBetween(user.getId(), startDate, endDate);

        // 주차별 이체 내역 정리
        Map<Integer, Integer> weeklySpent = new TreeMap<>();
        for (Statement statement : statements) {
            if (statement.getStatementType() == StatementType.MONEY) { // 이체 내역만
                LocalDate date = statement.getCreatedAt().toLocalDate();
                int weekOfYear = date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
                weeklySpent.put(weekOfYear, weeklySpent.getOrDefault(weekOfYear, 0) + statement.getAmount());
            }
        }

        // 결과 생성
        List<Map<String, Integer>> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : weeklySpent.entrySet()) {
            Map<String, Integer> weekData = new HashMap<>();
            weekData.put("Date", entry.getKey());
            weekData.put("Amount", entry.getValue());
            result.add(weekData);
        }
        return result;

    }

    public List<Map<String, Integer>> getMonthlyTransferHistory(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        List<Statement> statements = statementRepository.findByUser(user);

        // 월별 소비 내역 정리
        Map<Integer, Integer> monthlySpent = new LinkedHashMap<>();
        for (Statement statement : statements) {
            if (statement.getStatementType() == StatementType.MONEY) {
                LocalDate date = statement.getCreatedAt().toLocalDate();
                int monthKey = date.getYear() * 100 + date.getMonthValue(); // "YYYYMM" 형식
                monthlySpent.put(monthKey, monthlySpent.getOrDefault(monthKey, 0) + statement.getAmount());
            }
        }

        // 결과 정리
        List<Map<String, Integer>> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : monthlySpent.entrySet()) {
            Map<String, Integer> monthData = new HashMap<>();
            monthData.put("Date", entry.getKey()); // Date는 "YYYYMM" 형식
            monthData.put("Amount", entry.getValue()); // 해당 월 소비 금액
            result.add(monthData);
        }
        return result;
    }

    public List<MilageStatementsDTO> getCurrentMilageStatement(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        List<MilageStatementsDTO> milageDTOs = new ArrayList<>();
        List<Statement> milageStatements = statementRepository.findByUserAndStatementType(user, StatementType.MILEAGE);

        // statement가 없을 경우 빈 리스트 반환
        if(milageStatements == null){
            return new ArrayList<>();
        }

        milageStatements.stream().forEach(statement -> {
            milageDTOs.add(new MilageStatementsDTO(
                    statement.getAmount(),
                    statement.getMemo(),
                    statement.getCreatedAt()

            ));
        });


        return milageDTOs;
    }
}

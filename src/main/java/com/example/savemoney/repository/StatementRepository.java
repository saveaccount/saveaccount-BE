package com.example.savemoney.repository;

import com.example.savemoney.entity.Statement;
import com.example.savemoney.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface StatementRepository extends JpaRepository<Statement, Long> {
    List<Statement> findByUser(User user);
    List<Statement> findBySenderAccountId(Long accountId);
    List<Statement> findByReceiverAccountId(Long accountId);

    @Query("SELECT COALESCE(SUM(s.amount), 0) FROM Statement s WHERE s.user.id = :userId AND s.createdAt >= :firstDayOfLastMonth AND s.createdAt <= :lastDayOfLastMonth AND s.statementType=0")
    int calculateMonthlySpend(@Param("userId") Long userId, LocalDateTime firstDayOfLastMonth, LocalDateTime lastDayOfLastMonth);

    // 특정 유저의 저번 달 기간 동안의 소비 내역 조회
    @Query("SELECT s FROM Statement s WHERE s.user.id = :userId AND s.createdAt BETWEEN :startDate AND :endDate")
    List<Statement> findByUserAndDateBetween(@Param("userId") Long userId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

    @Query("SELECT s FROM Statement s WHERE s.senderAccount.account_num = :accountNum AND s.createdAt BETWEEN :startOfMonth AND :now")
    List<Statement> findByAccountAndDateBetween(@Param("accountNum") String accountNum,
                                                @Param("startOfMonth") LocalDateTime startOfMonth,
                                                @Param("now") LocalDateTime now);

    @Query("SELECT COALESCE(SUM(s.amount), 0) FROM Statement s WHERE s.senderAccount.user.id = :userId AND s.statementType = 0 AND s.createdAt BETWEEN :startOfMonth AND :now")
    int calculateCurrentMonthExpense(@Param("userId") Long userId,
                                     @Param("startOfMonth") LocalDateTime startOfMonth,
                                     @Param("now") LocalDateTime now);


}

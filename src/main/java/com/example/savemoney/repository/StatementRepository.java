package com.example.savemoney.repository;

import com.example.savemoney.entity.Statement;
import com.example.savemoney.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StatementRepository extends JpaRepository<Statement, Long> {
    List<Statement> findByUser(User user);
    List<Statement> findBySenderAccountId(Long accountId);
    List<Statement> findByReceiverAccountId(Long accountId);

    @Query("SELECT COALESCE(SUM(s.amount), 0) FROM Statement s WHERE s.user.id = :userId AND s.createdAt >= :oneMonthAgo AND s.statementType=0")
    int calculateMonthlySpend(@Param("userId") Long userId, @Param("oneMonthAgo") LocalDateTime oneMonthAgo);

}

package com.example.savemoney.repository;

import com.example.savemoney.entity.Statement;
import com.example.savemoney.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatementRepository extends JpaRepository<Statement, Long> {
    List<Statement> findByUser(User user);
    List<Statement> findBySenderAccountId(Long accountId);
    List<Statement> findByReceiverAccountId(Long accountId);

}

package com.example.savemoney.repository;

import com.example.savemoney.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // 특정 사용자 ID로 해당 사용자의 모든 계좌를 조회
    List<Account> findByUserId(Long userId);
}

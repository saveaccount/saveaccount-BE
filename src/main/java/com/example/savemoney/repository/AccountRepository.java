package com.example.savemoney.repository;

import com.example.savemoney.entity.Account;
import com.example.savemoney.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // 1. 특정 사용자의 계좌를 조회하는 메서드 추가
    List<Account> findByUser_Username(String username);

    // 2. 다른 필요한 메서드들
    Account findByAccountNum(String accountNum);
}

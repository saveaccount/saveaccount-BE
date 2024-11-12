package com.example.savemoney.repository;

import com.example.savemoney.entity.Account;
import com.example.savemoney.entity.User;
import com.example.savemoney.enumeration.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // 특정 사용자 ID로 해당 사용자의 모든 계좌를 조회
    List<Account> findByUserId(Long userId);

    List<Account> findByUser(User user);

    // 특정 사용자와 계좌 번호를 기준으로 계좌 조회 (account_num으로 수정)
    @Query("SELECT a FROM Account a WHERE a.user = :user AND a.account_num = :account_num")
    Account findByUserAndAccount_num(@Param("user") User user, @Param("account_num") String account_num);

    // 계좌 번호(account_num)를 기준으로 계좌 조회 (account_num으로 수정)
    @Query("SELECT a FROM Account a WHERE a.account_num = :account_num")
    Account findByAccount_num(@Param("account_num") String account_num);

}

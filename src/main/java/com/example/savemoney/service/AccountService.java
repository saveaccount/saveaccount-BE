package com.example.savemoney.service;

import com.example.savemoney.entity.User;
import com.example.savemoney.dto.AccountRequestDto;
import com.example.savemoney.dto.TransferRequestDto;
import com.example.savemoney.entity.Account;
import com.example.savemoney.entity.Statement;
import com.example.savemoney.repository.AccountRepository;
import com.example.savemoney.repository.UserRepository;
import com.example.savemoney.repository.StatementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.savemoney.enumeration.StatementType;
import com.example.savemoney.enumeration.ExpenseType;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final StatementRepository statementRepository;
    private final UserRepository userRepository;
    private final StatementService statementService;

    @Autowired
    public AccountService(AccountRepository accountRepository,
                          StatementRepository statementRepository,
                          UserRepository userRepository,
                          StatementService statementService) {
        this.accountRepository = accountRepository;
        this.statementRepository = statementRepository;
        this.userRepository = userRepository;
        this.statementService = statementService;
    }

    @Transactional
    public Account createAccount(AccountRequestDto accountRequestDto) {
        // 1. 사용자 정보 조회 (null 체크 후 예외 처리)
        User user = userRepository.findByUsername(accountRequestDto.getUser());
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        // 2. Account 객체 생성
        Account account = new Account(
                accountRequestDto.getAccountNum(),
                accountRequestDto.getBank(),
                user,  // User 객체 할당
                accountRequestDto.getPw(),
                accountRequestDto.getType(),
                0 // 초기 잔액 0
        );

        // 3. Account 저장
        return accountRepository.save(account);
    }

    @Transactional
    public void transfer(TransferRequestDto transferRequestDto) {
        // 송금자와 수취인 계좌 조회
        Account senderAccount = accountRepository.findById(transferRequestDto.getSenderAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
        Account receiverAccount = accountRepository.findById(transferRequestDto.getReceiverAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

        // 비밀번호 확인
        if (!senderAccount.getPw().equals(transferRequestDto.getPw())) {
            throw new IllegalArgumentException("Invalid password");
        }

        // 잔액 확인
        if (senderAccount.getBalance() < transferRequestDto.getAmount()) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        // 송금자 계좌 잔액 차감
        senderAccount.setBalance(senderAccount.getBalance() - transferRequestDto.getAmount());

        // 수취인 계좌 잔액 증가
        receiverAccount.setBalance(receiverAccount.getBalance() + transferRequestDto.getAmount());

        // 계좌 상태 변경 저장
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // 이체 내역 생성 (StatementService에 위임)
        statementService.createStatement(transferRequestDto, senderAccount, receiverAccount);
    }

}

package com.example.savemoney.service;

import com.example.savemoney.dto.AccountDTO;
import com.example.savemoney.dto.AccountEnrollDTO;
import com.example.savemoney.dto.TransferRequestDTO;
import com.example.savemoney.dto.TransferResponseDTO;
import com.example.savemoney.entity.Account;
import com.example.savemoney.entity.Statement;
import com.example.savemoney.entity.User;
import com.example.savemoney.enumeration.AccountType;
import com.example.savemoney.enumeration.BankType;
import com.example.savemoney.enumeration.ExpenseType;
import com.example.savemoney.enumeration.StatementType;
import com.example.savemoney.repository.AccountRepository;
import com.example.savemoney.repository.StatementRepository;
import com.example.savemoney.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final StatementRepository statementRepository;

    public int enrollAccount(AccountEnrollDTO accountEnrollDTO){
        // 유저 조회
        User user = userRepository.findByUsername(accountEnrollDTO.getUsername());
        if(user == null){
            throw new IllegalArgumentException("존재하지 않는 사용자입니다");
        }

        // AccountType 설정
        AccountType accountType = accountEnrollDTO.getType() == 0 ? AccountType.PERSONAL : AccountType.GROUP;
        // BankType 변환
        BankType bankType;
        try{
            bankType = BankType.valueOf(accountEnrollDTO.getBank().toUpperCase());
        }catch ( IllegalArgumentException e){
            throw new IllegalArgumentException("은행 정보가 올바르지 않습니다");
        }

        //Account 생성
        Account account = Account.builder()
                .user(user)
                .account_num(accountEnrollDTO.getAccountNum())
                .type(accountType)
                .pw(bCryptPasswordEncoder.encode(accountEnrollDTO.getPw()))
                .bank(bankType)
                .balance(0)
                .build();

        //Account 저장
        accountRepository.save(account);
        return account.getBalance(); // 초기 잔액 반환
    }

    public boolean deleteAccount(String username,String accountNum, String pw){
        // User 조회
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("존재하지 않는 사용자입니다");
        }
        // 사용자 계좌 조회
        Account account = accountRepository.findByUserAndAccount_num(user, accountNum);
        if(account == null){
            throw new IllegalArgumentException("존재하지 않는 계좌입니다");
        }

        // 계좌 비밀번호 검증
        if(!bCryptPasswordEncoder.matches(pw, account.getPw())){
            return false;
        }
        // 잔액이 0인지 확인
        if (account.getBalance() != 0){
            return false;
        }
        // 모임 계좌의 경우 호스트 여부 확인
        if(account.getType() == AccountType.GROUP && !isHost(user,account)){
            return false;
        }

        // 모든 조건을 통과한 경우 계좌 삭제
        accountRepository.delete(account);
        return true;
    }


    public List<AccountDTO> getUserAccounts(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다");
        }

        return accountRepository.findByUserId(user.getId()).stream()
                .map(account -> new AccountDTO(
                        account.getId(),
                        account.getAccount_num(),
                        account.getType(),
                        account.getBank(),
                        account.getBalance(),
                        user.getUsername()
                ))
                .collect(Collectors.toList());
    }

    public int getTotalBalance(String username){
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("존재하지 않는 사용자입니다");
        }
        List<Account> accounts = accountRepository.findByUserId(user.getId());
        return accounts.stream()
                .mapToInt(Account::getBalance)
                .sum();
    }

    @Transactional
    public TransferResponseDTO transfer(TransferRequestDTO transferRequestDTO, String username){
        //유저 확인
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("존재하지 않는 사용자입니다");
        }
        // 보내는 계좌 및 비밀번호 검증
        Account senderAccount = accountRepository.findByUserAndAccount_num(user, transferRequestDTO.getSenderAccountNum());;
        if(senderAccount == null){
            throw new IllegalArgumentException("보내는 계좌가 존재하지 않는 계좌입니다");
        }
        if(!bCryptPasswordEncoder.matches(transferRequestDTO.getAccountPassword(), senderAccount.getPw())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }
        //잔액 확인 및 차감
        if(senderAccount.getBalance() < transferRequestDTO.getAmount()){
            throw new IllegalArgumentException("잔액이 부족합니다");
        }

        // 받는계좌 확인 및 잔액추가
        Account receiverAccount = accountRepository.findByAccount_num(transferRequestDTO.getReceiverAccountNum());
        if(receiverAccount == null){
            throw new IllegalArgumentException("받는 계좌가 존재하지 않는 계좌입니다");
        }
        senderAccount.withdraw(transferRequestDTO.getAmount());
        receiverAccount.deposit(transferRequestDTO.getAmount());

        // 수동 플러시 (Dirty Checking 보장)
        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        // 이체 내역 생성 및 저장
        Statement statement = Statement.builder()
                .memo("이체내역메모")
                .statementType(StatementType.MONEY)
                .expenseType(transferRequestDTO.getCategory())
                .amount(transferRequestDTO.getAmount())
                .senderAccount(senderAccount)
                .receiverAccount(receiverAccount)
                .user(user)
                .build();

        statementRepository.save(statement);

        // TransferResponseDTO 반환
        return new TransferResponseDTO(
                statement.getId(),
                statement.getMemo(),
                statement.getStatementType(),
                statement.getExpenseType(),
                statement.getAmount(),
                statement.getSenderAccount().getAccount_num(),
                statement.getSenderAccount().getBalance(),
                statement.getReceiverAccount().getAccount_num(),
                statement.getReceiverAccount().getBalance()
        );


    }

    // 모임 계좌의 호스트 여부 확인
    private boolean isHost(User user, Account account){
        return account.getUser().equals(user);
    }
}

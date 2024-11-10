package com.example.savemoney.controller;

import com.example.savemoney.entity.Account;
import com.example.savemoney.repository.AccountRepository;
import org.springframework.http.ResponseEntity;
import com.example.savemoney.dto.StatementResponseDto;
import com.example.savemoney.dto.TransferRequestDto;
import com.example.savemoney.service.StatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statements")
public class StatementController {

    private final StatementService statementService;
    private final AccountRepository accountRepository;

    @Autowired
    public StatementController(StatementService statementService, AccountRepository accountRepository) {
        this.statementService = statementService;
        this.accountRepository = accountRepository; //
    }

    // 1. 이번 달 전체 이체 내역 조회
    @GetMapping("/main")
    public List<StatementResponseDto> getMainStatements(@RequestParam String userId) {
        return statementService.getMainStatements(userId);
    }

    // 2. 계좌별 이체 내역 조회
    @GetMapping("/account")
    public List<StatementResponseDto> getAccountStatements(@RequestParam String userId, @RequestParam Long accountId) {
        return statementService.getAccountStatements(userId, accountId);
    }

//    // 3. 이체 내역 분석 (일별/주간별/월별/분류별)
//    @GetMapping("/analysis")
//    public List<StatementResponseDto> getAnalysisStatements(
//            @RequestParam String userId,
//            @RequestParam String period, // "daily", "weekly", "monthly", "category"
//            @RequestParam(required = false) String category, // Optional, only used for category-based analysis
//            @RequestParam(required = false) String date // Optional, used for filtering by specific date (YYYY-MM-DD)
//    ) {
//        return statementService.getAnalysisStatements(userId, period, category, date);
//    }

    // 4. 이체 내역 생성
    @PostMapping("/create")
    public ResponseEntity<String> createStatement(@RequestBody TransferRequestDto transferRequestDto) {
        // 송금자 계좌 조회
        Account senderAccount = accountRepository.findById(transferRequestDto.getSenderAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

        // 수취인 계좌 조회
        Account receiverAccount = accountRepository.findById(transferRequestDto.getReceiverAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

        // StatementService의 createStatement 메서드 호출
        statementService.createStatement(transferRequestDto, senderAccount, receiverAccount);

        return ResponseEntity.ok("Transfer and Statement Created Successfully");
    }
}

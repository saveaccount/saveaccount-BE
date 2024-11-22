package com.example.savemoney.controller;

import com.example.savemoney.dto.MilageStatementsDTO;
import com.example.savemoney.dto.StatementResponseDTO;
import com.example.savemoney.entity.Statement;
import com.example.savemoney.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/statements")
public class StatementController {

    private final StatementService statementService;

    @GetMapping("/category")
    public ResponseEntity<?> getCategorySpent(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, Integer> categorySpent = statementService.getCategorySpent(username);
        return ResponseEntity.ok(categorySpent);
    }

    @GetMapping("/week")
    public ResponseEntity<?> getWeeklyTransferHistory(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Map<String, Integer>> weekSpent = statementService.getWeeklyTransferHistory(username);
        return ResponseEntity.ok(weekSpent);
    }

    @GetMapping("/month")
    public ResponseEntity<?> getMonthlyTransferHistory() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Map<String, Integer>> monthSpent = statementService.getMonthlyTransferHistory(username);
        return ResponseEntity.ok(monthSpent);
    }
    @GetMapping("/all")
    public ResponseEntity<List<StatementResponseDTO>> getCurrentMonthStatements() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<StatementResponseDTO> statements = statementService.getCurrentMonthStatements(username);
        return ResponseEntity.ok(statements);
    }
    @GetMapping("/account")
    public ResponseEntity<List<StatementResponseDTO>> getCurrentMonthStatementsByAccount(@RequestParam String account_num) {
        List<StatementResponseDTO> statements = statementService.getCurrentMonthStatementsByAccount(account_num);
        return ResponseEntity.ok(statements);
    }
    @GetMapping("/expense")
    public ResponseEntity<Map<String, Integer>> getCurrentMonthExpense() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int totalSpent = statementService.getCurrentMonthExpense(username);
        Map<String, Integer> response = new HashMap<>();
        response.put("totalSpent", totalSpent);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/milage")
    public ResponseEntity<?> getCurrentMilageStatement() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MilageStatementsDTO> milageStatements = statementService.getCurrentMilageStatement(username);

        return new ResponseEntity<>(milageStatements, milageStatements.isEmpty() ? org.springframework.http.HttpStatus.NO_CONTENT : org.springframework.http.HttpStatus.OK);
    }
}

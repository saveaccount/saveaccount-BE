package com.example.savemoney.controller;

import com.example.savemoney.service.StatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

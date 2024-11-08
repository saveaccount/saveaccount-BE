package com.example.savemoney.controller;


import com.example.savemoney.dto.AccountRequestDto;
import com.example.savemoney.dto.TransferRequestDto;
import com.example.savemoney.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/enroll")
    public ResponseEntity<?> createAccount(@RequestBody AccountRequestDto accountRequestDto) {
        accountService.createAccount(accountRequestDto);
        return ResponseEntity.ok("Account created successfully");
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequestDto transferRequestDto) {
        accountService.transfer(transferRequestDto);
        return ResponseEntity.ok("Transfer successful");
    }
}

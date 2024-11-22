package com.example.savemoney.controller;

import com.example.savemoney.dto.AccountDTO;
import com.example.savemoney.dto.AccountEnrollDTO;
import com.example.savemoney.dto.TransferRequestDTO;
import com.example.savemoney.dto.TransferResponseDTO;
import com.example.savemoney.entity.Account;
import com.example.savemoney.entity.Statement;
import com.example.savemoney.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    // 계좌 등록 API
    @PostMapping("/enroll")
    public ResponseEntity<?> enrollAccount(@RequestBody AccountEnrollDTO accountEnrollDTO) {
        try{
            int initialBalance = accountService.enrollAccount(accountEnrollDTO);
            return new ResponseEntity<>(initialBalance, HttpStatus.OK);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("계좌 등록 중 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //계좌 삭제 API
    @DeleteMapping("")
    public ResponseEntity<?> deleteAccount(@RequestParam String accountNum, @RequestParam String pw){
        // 현재 로그인 된 사용자의 username 가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isDeleted = accountService.deleteAccount(username, accountNum, pw);

        if(isDeleted){
            return new ResponseEntity<>("계좌 삭제 성공", HttpStatus.OK);
        }else {
            return new ResponseEntity<>("계좌 삭제 실패", HttpStatus.BAD_REQUEST);
        }
    }

    //유저의 모든 계좌 정보 조회
    @GetMapping("")
    public ResponseEntity<?> getUserAccounts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<AccountDTO> accounts = accountService.getUserAccounts(username);

        if (accounts.isEmpty()) {
            return new ResponseEntity<>("계좌 정보가 존재하지 않습니다", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    // 유저의 모든 계좌 잔액 총합 조회
    @GetMapping("/balance")
    public ResponseEntity<?> getTotalBalance(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        int totalBalance = accountService.getTotalBalance(username);
        return new ResponseEntity<>(totalBalance, HttpStatus.OK);
    }

    // 이체
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransferRequestDTO transferRequestDTO){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        try{
            TransferResponseDTO statement = accountService.transfer(transferRequestDTO, username);
            return new ResponseEntity<>(statement, HttpStatus.OK);
        }catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>("이체 중 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

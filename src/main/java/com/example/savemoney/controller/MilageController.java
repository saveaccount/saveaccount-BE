package com.example.savemoney.controller;

import com.example.savemoney.service.MilageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/milage")
public class MilageController {

    private final MilageService milageService;

    @PatchMapping("/in")
    public ResponseEntity<?> putMilageIn(int amount) {

        // amount<=0일 경우
        if (amount <= 0) {
            return new ResponseEntity<>("0 이상의 값을 입력해주세요", HttpStatus.BAD_REQUEST);
        }

        // 마일리지 입금
        int milage = milageService.putMilageIn(amount);

        return new ResponseEntity<>("이체 내역 생성 완료, 이체 후 잔액" + milage, HttpStatus.OK);
    }
}

package com.example.savemoney.controller;

import com.example.savemoney.service.MilageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/milage")
public class MilageController {

    private final MilageService milageService;

    @PatchMapping("/in")
    public ResponseEntity<?> putMilageIn() {

        // 마일리지 입금
        int milage = milageService.putMilageIn();

        return new ResponseEntity<>("이체 내역 생성 완료, 이체 후 잔액" + milage, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getMilage() {

        // 마일리지 조회
        int milage = milageService.getMilage();

        return new ResponseEntity<>(milage, HttpStatus.OK);
    }
}

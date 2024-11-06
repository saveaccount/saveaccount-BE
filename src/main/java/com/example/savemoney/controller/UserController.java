package com.example.savemoney.controller;


import com.example.savemoney.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/check-id")
    public ResponseEntity<?> checkId(@RequestParam String username) {
        if(username == null) {
            return new ResponseEntity<>("아이디를 입력해주세요.", HttpStatus.BAD_REQUEST);
        } else {
            boolean isExists = userService.checkId(username);
            return new ResponseEntity<>(isExists, HttpStatus.OK);
        }

    }
}

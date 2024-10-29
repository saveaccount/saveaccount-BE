package com.example.savemoney.controller;

import com.example.savemoney.dto.SigninDTO;
import com.example.savemoney.dto.SignupDTO;
import com.example.savemoney.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService signService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupDTO user) {
        boolean isSuccess = signService.signup(user);
        if(!isSuccess) {
            return new ResponseEntity<>("이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("회원 가입 성공", HttpStatus.CREATED);
    }

    @GetMapping("/")
    public String test() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return "로그인한 사람: "+ name;
    }
}

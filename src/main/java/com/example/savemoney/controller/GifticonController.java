package com.example.savemoney.controller;

import com.example.savemoney.service.GifticonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gifticon")
public class GifticonController {

    private final GifticonService gifticonService;

    @GetMapping
    public ResponseEntity<?> getGifticon() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.example.savemoney.controller;

import com.example.savemoney.dto.CardResponseDTO;
import com.example.savemoney.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    // 카드 추천 API
    @GetMapping("")
    public ResponseEntity<?> getRecommendCard() {
        //현재 로그인된 사용자의 username가져오기
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        // 카드 추천 서비스 호출
        List<CardResponseDTO> recommendedCards = cardService.recommendCards(username);
        if(recommendedCards.isEmpty()) {
            return new ResponseEntity<>("추천 카드가 없습니다.", HttpStatus.NOT_FOUND); //추천카드가 없을 경우
        }
        return new ResponseEntity<>(recommendedCards, HttpStatus.OK); //추천카드가 있을 경우 반환


    }
}

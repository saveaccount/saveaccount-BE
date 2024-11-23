package com.example.savemoney.controller;

import com.example.savemoney.dto.GifticonInfoRequest;
import com.example.savemoney.entity.Gifticon_Info;
import com.example.savemoney.entity.Gifticon_relation;
import com.example.savemoney.service.GifticonService;
import com.example.savemoney.service.MilageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/gifticon")
public class GifticonController {

    private final GifticonService gifticonService;

    // 유저가 가지고 있는 기프티콘 전부 조회
    @GetMapping
    public ResponseEntity<?> getUserGifticons() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<String> gifticonDetails = gifticonService.getUserGifticonDetails(username);

        if (gifticonDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    List.of("보유한 기프티콘이 없습니다.")
            );
        }

        // 성공적으로 결과 반환
        return ResponseEntity.ok(gifticonDetails);
    }

    // 룰렛 결과에 따라 기프티콘 저장과 마일리지 차감
    @PostMapping
    public ResponseEntity<?> spinRoulette(@RequestParam boolean isWin) {
        try {
            gifticonService.processRouletteResult(isWin);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
package com.example.savemoney.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class rouletteService {

    public boolean spinActualRoulette() {
        Random random = new Random();
        return random.nextInt(100) < 35; // 35% 확률
    }

    // 기프티콘 코드 생성
    public String generateGifticonCode() {
        return "GIFT-" + System.currentTimeMillis() + "-" + new Random().nextInt(1000);
    }
}

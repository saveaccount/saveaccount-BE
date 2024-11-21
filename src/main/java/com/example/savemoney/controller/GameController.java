package com.example.savemoney.controller;

import com.example.savemoney.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    @PostMapping("/in")
    public ResponseEntity<?> startGame() {
        gameService.startGame();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/out")
    public ResponseEntity<?> endGame(Boolean gameResult) {
        if(gameResult==null) {
            return new ResponseEntity<>("게임 결과를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        gameService.endGame(gameResult);
        return ResponseEntity.ok().build();
    }

}

package com.example.savemoney.service;

import com.example.savemoney.entity.Milage;
import com.example.savemoney.entity.Statement;
import com.example.savemoney.entity.User;
import com.example.savemoney.enumeration.StatementType;
import com.example.savemoney.repository.MilageRepository;
import com.example.savemoney.repository.StatementRepository;
import com.example.savemoney.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
/**
 * 유저 gamelife ≥1 인지 확인
 * 게임 목숨이 없으면 IllegalArgumentException 발생
 */
public class GameService {

    private final UserRepository userRepository;

    private final MilageRepository milageRepository;

    private final StatementRepository statementRepository;
    @Transactional
    public void startGame() {
        // 유저 gamelife ≥1 인지 확인
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username);
        if(user.getGameLife() <1){
            throw new IllegalArgumentException("게임 목숨이 없습니다.");
        } else {
            user.updateGameLife(user.getGameLife()-1);
        }

    }

    /**
     * 게임 승리시 목숨 5개, 300 마일리지 지급
     * @param gameResult
     */
    @Transactional
    public void endGame(Boolean gameResult) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);

        // 게임 승리시
        if(gameResult == true){
            // 목숨 5개 지급
            user.updateGameLife(user.getGameLife()+5);

            // 300 마일리지 지급
            Milage milage = milageRepository.findByUser(user);
            milage.updateBalance(300);

            // 마일리지 지급 내역 추가
            // statement 생성
            Statement statement = Statement.builder()
                    .user(user)
                    .memo("미니 게임 승리로 인한 마일리지 지급")
                    .statementType(StatementType.MILEAGE)
                    .amount(300)
                    .build();

            statementRepository.save(statement);
            log.info("statement : {}", statement.toString());
        }
    }
}

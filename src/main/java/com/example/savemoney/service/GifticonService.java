package com.example.savemoney.service;

import com.example.savemoney.entity.*;
import com.example.savemoney.enumeration.StatementType;
import com.example.savemoney.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GifticonService {

    private final Gifticon_infoRepository gifticonInfoRepository;
    private final Gifticon_relationRepository gifticonRelationRepository;
    private final MilageRepository milageRepository;
    private final StatementRepository statementRepository;
    private final UserRepository userRepository;

    // 유저가 보유한 기프티콘 코드 조회
    public List<String> getUserGifticonDetails(String username) {
        List<Gifticon_relation> gifticonRelations = gifticonRelationRepository.findByUser_Username(username);
        return gifticonRelations.stream()
                .map(relation -> {
                    Gifticon_Info gifticon = relation.getGifticon();
                    return "상품명: " + gifticon.getProductName() + ", 코드: " + gifticon.getCode();
                })
                .toList();
    }

    // 룰렛 결과 처리
    @Transactional
    public String processRouletteResult(boolean isWin) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("유저가 존재하지 않습니다.");
        }

        Milage milage = milageRepository.findByUser(user);
        final int TICKET_COST = 100;

        if (milage.getBalance() < TICKET_COST) {
            throw new IllegalArgumentException("마일리지가 부족합니다. 현재 잔액: " + milage.getBalance());
        }

        milage.updateBalance(-TICKET_COST);
        milageRepository.save(milage);

        Statement statement = Statement.builder()
                .user(user)
                .amount(-TICKET_COST)
                .statementType(StatementType.MILEAGE)
                .memo(isWin ? "룰렛 당첨 처리" : "룰렛 꽝 처리")
                .build();
        statementRepository.save(statement);

        if (!isWin) {
            return "꽝! 마일리지가 차감되었습니다.";
        }

        Gifticon_Info randomGifticon = gifticonInfoRepository.findRandomGifticon();
        if (randomGifticon == null) {
            throw new IllegalArgumentException("기프티콘 정보가 없습니다. 기프티콘을 등록해주세요.");
        }

        Gifticon_relation gifticonRelation = Gifticon_relation.builder()
                .user(user)
                .gifticon(randomGifticon)
                .build();
        gifticonRelationRepository.save(gifticonRelation);

        return "축하합니다! 상품: " + randomGifticon.getProductName() + ", 코드: " + randomGifticon.getCode();
    }
}
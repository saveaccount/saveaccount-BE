package com.example.savemoney.service;

import com.example.savemoney.dto.CardResponseDTO;
import com.example.savemoney.entity.Account;
import com.example.savemoney.entity.Card;
import com.example.savemoney.entity.Statement;
import com.example.savemoney.entity.User;
import com.example.savemoney.enumeration.BankType;
import com.example.savemoney.enumeration.ExpenseType;
import com.example.savemoney.repository.AccountRepository;
import com.example.savemoney.repository.CardRepository;
import com.example.savemoney.repository.StatementRepository;
import com.example.savemoney.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.plaf.nimbus.State;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final StatementRepository statementRepository;
    private final CardRepository cardRepository;

    public List<CardResponseDTO> recommendCards(String username){ //추천카드 가져오기
        // 1. 유저 가져오기
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }

        // 2. 주거래 은행 찾기
        BankType mainBank = getMainBank(user);
        // 3. 소비 상위 3개 카테고리 가져오기
        List<ExpenseType> topCategories = getTopExpenseCategories(user);

        // 4. 카테고리와 은행에 맞는 카드 추천
        return getRecommendedCards(topCategories, mainBank);

    }

    // 2. 주거래 은행 찾기
    private BankType getMainBank(User user){
        List<Account> accounts = accountRepository.findByUserId(user.getId());

        // 은행별 거래 횟수 합산
        Map<BankType, Integer> bankTransactionCounts = new HashMap<>();
        for (Account account: accounts){
            Long accountId = account.getId();
            List<Statement> sentTransactions = statementRepository.findBySenderAccountId(accountId);
            List<Statement> receivedTransactions = statementRepository.findByReceiverAccountId(accountId);

            // 송금 및 수신 거래 수 합산
            bankTransactionCounts.put(account.getBank(),
                    bankTransactionCounts.getOrDefault(account.getBank(), 0) + sentTransactions.size() + receivedTransactions.size());
        }

        // 거래 수가 가장 많은 은행 찾기
        return bankTransactionCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(BankType.ETC);// 가장 많은 거래가 없을 경우 기타로 설정
        }
    // 3. 사용자의 상위 3개 소비 카테고리 추출
    private List<ExpenseType> getTopExpenseCategories(User user){
        List<Statement> statements = statementRepository.findByUser(user);

        // 카테고리별 소비 금액 합산
        Map<ExpenseType, Integer> categoryTotals = new HashMap<>();
        for (Statement statement: statements){
            ExpenseType category = statement.getExpenseType();
            int amount = statement.getAmount();
            categoryTotals.put(category, categoryTotals.getOrDefault(category, 0) + amount);
        }
        // 소비 금액 기준으로 상위 3개 카테고리 추출
        return categoryTotals.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // 4. 카테고리와 은행에 맞는 추천 카드 반환
    private List<CardResponseDTO> getRecommendedCards(List<ExpenseType> topCategories, BankType mainBank){
        List<CardResponseDTO> recommendedCards = new ArrayList<>();

        // 상위 카테고리별로 주거래 은행과 일치하는 카드 찾기
        for (ExpenseType category: topCategories){
            List<Card> cards = cardRepository.findByCategoryAndBank(category, mainBank);
            if (!cards.isEmpty()){
                recommendedCards.addAll(cards.stream()
                        .map(CardResponseDTO::new).collect(Collectors.toList()));
                break;
            }
        }
        return recommendedCards;
    }
}

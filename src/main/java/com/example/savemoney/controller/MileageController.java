package com.example.savemoney.controller;


import com.example.savemoney.entity.User;
import com.example.savemoney.service.MileageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mileage")
public class MileageController {

    private final MileageService mileageService;

    // 사용자의 마일리지 조회
    @GetMapping("/checkMileage")
    public ResponseEntity<Integer> getBalance(@AuthenticationPrincipal User user) {
        int balance = mileageService.viewBalance(user);
        return ResponseEntity.ok(balance);
    }

    //마일리지 차감
    @PostMapping("/minusMileage")
    public ResponseEntity<?> mileageMinus (@RequestParam int value, @AuthenticationPrincipal User user) {

        boolean minusSuccess = mileageService.decreaseBalance(user, value);
        if (minusSuccess) return ResponseEntity.ok(value + "포인트 차감되었습니다");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("마일리지 잔액 부족");
    }

    //마일리지 증가
    @PostMapping("/addMileage")
    public ResponseEntity<?> mileageAdd (@RequestParam int value, @AuthenticationPrincipal User user) {

        boolean addSuccess = mileageService.increaseBalance(user, value);
        if (addSuccess) return ResponseEntity.ok(value + "포인트를 받았습니다");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("마일리지 처리 실패");
    }

    // 마일리지를 티켓 n장으로 교환
    @PostMapping("/mileageToTicket")
    public ResponseEntity<?> mileageToTicket (@RequestParam int ticketCount, @AuthenticationPrincipal User user){
        boolean isMileageToTicket = mileageService.mileageToTicket(user, ticketCount);
        if(isMileageToTicket) return ResponseEntity.ok(ticketCount+"장 교환 성공");
        else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("마일리지 잔액 부족");
    }

    /*//룰렛 돌리기
    @PostMapping("/spinRoulette")
    public ResponseEntity<?> spinRoulette(@AuthenticationPrincipal User user){
        int ticketCount = mileageService.getTicketCount(user);
        // 룰렛 전 티켓부터 확인
        if(ticketCount <= 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("티켓 부족");

        // 4가지 기프티콘 당첨(기프티콘 id로 return), 1가지 꽝(0)
        int result = mileageService.spinRoulette(user);
        if(result == 0) return ResponseEntity.ok("다음 기회에..");
        else {
            // return받은 result로 기프티콘 이름과 mapping
            String gifticonName = mileageService.getGifticonNameById(result);
            return ResponseEntity.ok("축하합니다!\n" + gifticonName + " 당첨!");
        }
    }*/
}

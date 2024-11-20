package com.example.savemoney.service;

import com.example.savemoney.entity.User;
import com.example.savemoney.entity.Mileage;
import com.example.savemoney.repository.UserRepository;
import com.example.savemoney.repository.MileageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MileageService {

    private final UserRepository userRepository;
    private final MileageRepository mileageRepository;

    // 사용자의 마일리지 조회
    public int viewBalance(User user) {

        Mileage mileage = user.getMileage();
        // mileage 가 0이 아니면 값 반환
        if (mileage.getBalance()>0) return mileage.getBalance();
        return 0;
    }

    // 차감 혹은 증가 된 mileage 값 업데이트
    public void setBalance(User user, int newBalance){
        Mileage mileage = user.getMileage();
        if (mileage != null) {
            mileage.setBalance(newBalance);
            mileageRepository.save(mileage);
        }
    }

    //mileage 차감
    public boolean decreaseBalance(User user, int value) {
        //현재 가지고 있는 마일리지 조회하여 currentBalance에 저장
        int currentBalance = viewBalance(user);

        if(currentBalance < value) return false;

        //유저의 현재 마일리지와 차감해야하는 값 비교 후 차감
        setBalance(user, currentBalance-value);
        return true;
    }

    //mileage 증가
    public boolean increaseBalance(User user, int value) {

        int currentBalance = viewBalance(user);

        setBalance(user,currentBalance + value);
        return true;
    }

    // 마일리지를 티켓으로 교환
    public boolean mileageToTicket(User user, int ticketCount) {
        int requitedMileageToTicket = 100;
        int currentBalance = viewBalance(user);
        int totalRequitedMileageToTicket = requitedMileageToTicket * ticketCount;

        // 티켓을 교환할 충분한 마일리지가 있을 때 마일리지값과 티켓값 업데이트
        if (currentBalance >= totalRequitedMileageToTicket){
            Mileage mileage = user.getMileage();
            int newBalance = currentBalance - totalRequitedMileageToTicket;
            mileage.setBalance(newBalance);

            int newTicketCount = mileage.getTicketCount() + ticketCount;
            mileage.setTicketCount(newTicketCount);

            mileageRepository.save(mileage);
            return true;
        }
        else return false;
    }

    // 사용자가 보유한 티켓의 수 조회
    public int getTicketCount(User user) {
        Mileage mileage = user.getMileage();
        // 티켓 개수 확인 후 출력, 없으면 0출력
        if(mileage.getTicketCount()>0) return mileage.getTicketCount();
        return 0;
    }

    /*
    // 티켓을 사용하여 룰렛 돌리기
    public int spinRoulette(User user) {
        return 0;
    }

    // 룰렛의 result (gifticonID) 를 통한 기프티콘명 출력
    public String getGifticonNameById(int result) {
        return null;
    }*/
}

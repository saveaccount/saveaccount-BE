package com.example.savemoney.service;

import com.example.savemoney.repository.Gifticon_infoRepository;
import com.example.savemoney.repository.Gifticon_relationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GifticonService {

    private final Gifticon_infoRepository gifticon_infoRepository;

    private final Gifticon_relationRepository gifticon_relationRepository;
    public void getGifticon() {

    }
}

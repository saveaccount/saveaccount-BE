package com.example.savemoney.repository;

import com.example.savemoney.entity.Gifticon_Info;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface Gifticon_infoRepository extends JpaRepository<Gifticon_Info, Long> {

    @Query(value = "SELECT * FROM gifticon_info ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Gifticon_Info findRandomGifticon();
}
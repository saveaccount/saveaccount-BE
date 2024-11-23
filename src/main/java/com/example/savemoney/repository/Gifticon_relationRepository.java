package com.example.savemoney.repository;

import com.example.savemoney.entity.Gifticon_Info;
import com.example.savemoney.entity.Gifticon_relation;
import com.example.savemoney.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Gifticon_relationRepository extends JpaRepository<Gifticon_relation, Long> {

    List<Gifticon_relation> findByUser_Username(String username);
}
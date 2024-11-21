package com.example.savemoney.repository;

import com.example.savemoney.entity.Gifticon_relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Gifticon_relationRepository extends JpaRepository<Gifticon_relation, Long> {
}

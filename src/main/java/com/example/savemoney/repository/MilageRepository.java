package com.example.savemoney.repository;

import com.example.savemoney.entity.Milage;
import com.example.savemoney.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MilageRepository extends JpaRepository<Milage, Long> {

    Milage findByUser(User user);

    @Query("select m.balance from Milage m where m.user = :user")
    int getMilage(User user);

}

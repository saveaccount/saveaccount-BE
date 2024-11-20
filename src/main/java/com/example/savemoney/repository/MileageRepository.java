package com.example.savemoney.repository;

import com.example.savemoney.entity.Mileage;
import com.example.savemoney.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MileageRepository extends JpaRepository<Mileage, Long> {
    Mileage findByUser(User user);
}
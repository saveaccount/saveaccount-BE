package com.example.savemoney.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gifticon_Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private byte[] gifticonImage;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String productName;

    @Builder
    public Gifticon_Info(byte[] gifticonImage, String code, String productName) {
        this.gifticonImage = gifticonImage;
        this.code = code;
        this.productName = productName;
    }
}
package com.example.savemoney.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gifticon_Info extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private byte[] gifticon_image;

    @Column(nullable = false)
    private String code;

    @OneToOne(mappedBy = "gifticon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Gifticon_relation gifticon;
}

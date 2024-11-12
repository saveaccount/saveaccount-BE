package com.example.savemoney.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false, unique = true)
    private String code;

    @OneToOne(mappedBy = "gifticon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Gifticon_relation gifticon;

    @Builder
    public Gifticon_Info(byte[] gifticon_image, String code) {
        this.gifticon_image = gifticon_image;
        this.code = code;
    }
}

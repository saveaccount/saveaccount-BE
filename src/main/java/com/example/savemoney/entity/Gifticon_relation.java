package com.example.savemoney.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Gifticon_relation extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    @OneToOne
    @JoinColumn(name = "gifticon_id")
    private Gifticon_Info gifticon;

    @Builder
    public Gifticon_relation(User user, Gifticon_Info gifticon) {
        this.user = user;
        this.gifticon = gifticon;
    }
}

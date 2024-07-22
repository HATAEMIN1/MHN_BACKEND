package com.project.mhnbackend.payment.domain;

import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.subscription.domain.Subscription;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id")
//    private Member member;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;
    private String pg;
    private String merchantUid;
    private String customerUid;
    private int amount;
    private String impUid;
    private String productName;
    private String buyerName;
    private String buyerTel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
package com.project.mhnbackend.subscription.domain;

import com.project.mhnbackend.member.domain.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime paymentDate;
    private LocalDateTime nextBillingDate;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
    private LocalDateTime createdAt;
    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }


    public enum SubscriptionStatus {
        ACTIVE, PAUSED, CANCELLED
    }


}

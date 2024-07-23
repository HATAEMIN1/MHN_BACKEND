package com.project.mhnbackend.subscription.dto.response;

import com.project.mhnbackend.subscription.domain.Subscription;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class SubscriptionResponseDTO {
    private Long id;
    private Long userId;
    private Subscription.SubscriptionStatus status;
    private LocalDateTime paymentDate;
    private LocalDateTime nextBillingDate;
}

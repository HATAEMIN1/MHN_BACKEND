package com.project.mhnbackend.subscription.service;

import com.project.mhnbackend.payment.dto.request.PaymentRequestDTO;
import com.project.mhnbackend.subscription.domain.Payment;
import com.project.mhnbackend.subscription.domain.Subscription;
import com.project.mhnbackend.subscription.dto.response.SubscriptionResponseDTO;
import com.project.mhnbackend.payment.repository.PaymentRepository;
import com.project.mhnbackend.subscription.repository.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public SubscriptionResponseDTO createSubscription(PaymentRequestDTO paymentRequestDTO) {
        Subscription subscription = Subscription.builder()
                .status(Subscription.SubscriptionStatus.ACTIVE)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMinutes(1))
                .nextBillingDate(LocalDateTime.now().plusMinutes(1))
                .build();
        subscriptionRepository.save(subscription);

        Payment payment = Payment.builder()
                .pg(paymentRequestDTO.getPg())
                .merchantUid(paymentRequestDTO.getMerchantUid())
                .amount(paymentRequestDTO.getAmount())
                .impUid(paymentRequestDTO.getImpUid())
                .createdAt(LocalDateTime.now())
                .subscription(subscription)
                .build();
        paymentRepository.save(payment);

        return SubscriptionResponseDTO.builder()
                .startDate(subscription.getStartDate())
                .nextBillingDate(subscription.getNextBillingDate())
                .status(subscription.getStatus())
                .build();
    }
}

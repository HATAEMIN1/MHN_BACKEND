package com.project.mhnbackend.payment.service;

import com.project.mhnbackend.payment.domain.Payment;
import com.project.mhnbackend.payment.dto.request.UnScheduleRequestDTO;
import com.project.mhnbackend.payment.dto.response.UnScheduleResponseDTO;
import com.project.mhnbackend.payment.repository.PaymentRepository;
import com.project.mhnbackend.subscription.domain.Subscription;
import com.project.mhnbackend.subscription.repository.SubscriptionRepository;
import com.project.mhnbackend.subscription.service.SubscriptionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService {
    private final IamportService iamportService;
    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;

    public Mono<UnScheduleResponseDTO> unschedulePayment(UnScheduleRequestDTO unScheduleRequestDTO) {
        return iamportService.getToken()
                .flatMap(tokenResponse -> {
                    String token = tokenResponse.getResponse().getAccess_token();
                    return iamportService.unSchedule(token, unScheduleRequestDTO);
                })
                .flatMap(unScheduleResponse ->
                        updateSubscriptionStatus(unScheduleRequestDTO.getCustomer_uid(), Subscription.SubscriptionStatus.PAUSED)
                                .thenReturn(unScheduleResponse)
                );
    }

    @Transactional
    public Mono<Void> updateSubscriptionStatus(String customer_uid, Subscription.SubscriptionStatus status) {
        return findPaymentByCustomerUid(customer_uid)
                .flatMap(payment -> Mono.fromRunnable(() -> {
                    Subscription subscription = payment.getSubscription();
                    if (subscription == null) {
                        throw new EntityNotFoundException("Subscription not found for payment: " + payment.getId());
                    }
                    subscription.setStatus(status);
                    subscriptionRepository.save(subscription);
                }));
    }

    public Mono<Payment> findPaymentByCustomerUid(String customer_uid) {
        log.info("Attempting to find payment with customer_uid: {}", customer_uid);
        log.info("Attempting to find payment with customer_uid.trim: {}", customer_uid.trim());
        log.info("Attempting to find"+paymentRepository.findTopByCustomerUidOrderByCreatedAtDesc(customer_uid.trim()));
        return Mono.fromCallable(() ->
                paymentRepository.findTopByCustomerUidOrderByCreatedAtDesc(customer_uid.trim())

        ).flatMap(optionalPayment ->
                optionalPayment.map(Mono::just)
                        .orElseGet(() -> Mono.error(new EntityNotFoundException("Payment not found for customer_uid: " + customer_uid)))
        );
    }
}

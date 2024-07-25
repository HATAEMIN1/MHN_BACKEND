package com.project.mhnbackend.subscription.service;

import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.repository.MemberRepository;
import com.project.mhnbackend.payment.dto.request.PaymentRequestDTO;
import com.project.mhnbackend.payment.domain.Payment;
import com.project.mhnbackend.payment.dto.request.UnScheduleRequestDTO;
import com.project.mhnbackend.payment.service.PaymentService;
import com.project.mhnbackend.subscription.domain.Subscription;
import com.project.mhnbackend.subscription.dto.response.SubscriptionResponseDTO;
import com.project.mhnbackend.payment.repository.PaymentRepository;
import com.project.mhnbackend.subscription.repository.SubscriptionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final PaymentService paymentService;
    @Transactional
    public SubscriptionResponseDTO createSubscription(PaymentRequestDTO paymentRequestDTO) {
        Member member = memberRepository.findById(paymentRequestDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + paymentRequestDTO.getMemberId()));

        Subscription subscription = Subscription.builder()
                .member(member)
                .status(Subscription.SubscriptionStatus.ACTIVE)
                .paymentDate(LocalDateTime.now())
//                .nextBillingDate(LocalDateTime.now().plusMonths(1))
//                .nextBillingDate(LocalDateTime.now().plusDays(1))
                .nextBillingDate(LocalDateTime.now().plusMinutes(1))
                .createdAt(LocalDateTime.now())
                .build();
        subscriptionRepository.save(subscription);

        Payment payment = Payment.builder()
                .member(member)
                .pg(paymentRequestDTO.getPg())
                .merchantUid(paymentRequestDTO.getMerchantUid())
                .customerUid(paymentRequestDTO.getCustomerUid())
                .impUid(paymentRequestDTO.getImpUid())
                .productName(paymentRequestDTO.getProductName())
                .buyerName(paymentRequestDTO.getBuyerName())
                .amount(paymentRequestDTO.getAmount())
                .buyerTel(paymentRequestDTO.getBuyerTel())
                .createdAt(LocalDateTime.now())
                .subscription(subscription)
                .build();
        paymentRepository.save(payment);

        return SubscriptionResponseDTO.builder()
                .paymentDate(subscription.getPaymentDate())
                .nextBillingDate(subscription.getNextBillingDate())
                .status(subscription.getStatus())
                .build();
    }

    public SubscriptionResponseDTO getSubscription(Long memberId) {
        Optional<Subscription> subscription = subscriptionRepository.findById(memberId);
        return SubscriptionResponseDTO.builder()
                .id(subscription.get().getId())
                .userId(memberId)
                .nextBillingDate(subscription.get().getNextBillingDate())
                .paymentDate(subscription.get().getPaymentDate())
                .status(subscription.get().getStatus())
                .build();
    }

    @Scheduled(fixedRate = 3600000) // 매 1분마다 실행
    public void checkAndCancelSubscriptions() {
        LocalDateTime now = LocalDateTime.now();
        log.info("현재시간은"+now);
        List<Subscription> expiredSubscriptions = subscriptionRepository.findByNextBillingDateBeforeAndStatus(now, Subscription.SubscriptionStatus.PAUSED);
        log.info("expiredSubscriptions는" + expiredSubscriptions);
        for (Subscription subscription : expiredSubscriptions) {
            subscription.setStatus(Subscription.SubscriptionStatus.CANCELLED);
            subscriptionRepository.save(subscription);  // 명시적으로 저장
            log.info("Subscription cancelled: " + subscription.getId());
        }

    }




}

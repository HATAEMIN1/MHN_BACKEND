package com.project.mhnbackend.subscription.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mhnbackend.member.domain.Member;
import com.project.mhnbackend.member.domain.MemberType;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;

//    private final List<SseEmitter> emitters = new ArrayList<>();
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    @Autowired
    private ObjectMapper objectMapper;

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }

    public void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }

    private static class StatusUpdate {
        public String status;
        public Long userId;  //추가

        public StatusUpdate(String status, Long userId) {
            this.status = status;
            this.userId = userId; //추가
        }
    }

//    public void notifyStatusChange(String status, Long userId) {
//        List<SseEmitter> deadEmitters = new ArrayList<>();
//        emitters.forEach(emitter -> {
//            try {
//                String jsonStatus = objectMapper.writeValueAsString(new StatusUpdate(status, userId));
//                emitter.send(SseEmitter.event().data(jsonStatus));
//                log.info("이미터는" + SseEmitter.event().data(jsonStatus));
//            } catch (IOException e) {
//                deadEmitters.add(emitter);
//            }
//        });
//        emitters.removeAll(deadEmitters);
//    }
public void notifyStatusChange(String newStatus, Long userId) {
    String eventData = String.format("{\"status\":\"%s\",\"userId\":%d}", newStatus, userId);
    List<SseEmitter> deadEmitters = new ArrayList<>();

    emitters.forEach(emitter -> {
        try {
            emitter.send(SseEmitter.event().data(eventData));
        } catch (IOException e) {
            deadEmitters.add(emitter);
        }
    });

    emitters.removeAll(deadEmitters);
}


    @Transactional
    public SubscriptionResponseDTO createSubscription(PaymentRequestDTO paymentRequestDTO) {
        Member member = memberRepository.findById(paymentRequestDTO.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + paymentRequestDTO.getMemberId()));
        member.addType(MemberType.SUB_USER);
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
        Optional<Subscription> subscription = subscriptionRepository.findByMemberId(memberId);
        return SubscriptionResponseDTO.builder()
                .id(subscription.get().getId())
                .userId(memberId)
                .nextBillingDate(subscription.get().getNextBillingDate())
                .paymentDate(subscription.get().getPaymentDate())
                .status(subscription.get().getStatus())
                .build();
    }
@Scheduled(fixedRate = 10000) // 매 10초마다 실행
@Transactional
public void checkAndCancelSubscriptions() {
    LocalDateTime now = LocalDateTime.now();
    log.info("현재시간은" + now);
    List<Subscription.SubscriptionStatus> statusList = Arrays.asList(
            Subscription.SubscriptionStatus.ACTIVE,
            Subscription.SubscriptionStatus.PAUSED
    );
    List<Subscription> expiredSubscriptions = new ArrayList<>(subscriptionRepository.findByNextBillingDateBeforeAndStatusIn(now, statusList));
    log.info("expiredSubscriptions는" + expiredSubscriptions);

    List<Member> membersToUpdate = new ArrayList<>();

    for (Subscription subscription : expiredSubscriptions) {
        subscription.setStatus(Subscription.SubscriptionStatus.CANCELLED);
        log.info("Subscription cancelled: " + subscription.getId());

        Member member = subscription.getMember();
        if (member != null) {
            member.removeType(MemberType.SUB_USER);
            membersToUpdate.add(member);
            log.info("SUB_USER role removed from member: " + member.getId());
        }
    }

    // 변경된 구독 정보 저장
    subscriptionRepository.saveAll(expiredSubscriptions);

    // 변경된 회원 정보 저장
    memberRepository.saveAll(membersToUpdate);

    // 상태 변경 알림
    for (Member member : membersToUpdate) {
        notifyStatusChange("SUB_USER", member.getId());
    }
}
}

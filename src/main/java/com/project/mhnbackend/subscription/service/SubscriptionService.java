package com.project.mhnbackend.subscription.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.mhnbackend.admin.dto.response.StatsResponseForMemberDTO;
import com.project.mhnbackend.admin.dto.response.StatsResponseForSubMemberDTO;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }
    public void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }
	

	private static class StatusUpdate {
        public String status;
        public Long userId;  //추가

//        public StatusUpdate(String status, Long userId) {
//            this.status = status;
//            this.userId = userId; //추가
//        }
    }
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
                .nextBillingDate(LocalDateTime.now().plusMonths(1))
//                .nextBillingDate(LocalDateTime.now().plusDays(1))
//                .nextBillingDate(LocalDateTime.now().plusMinutes(1))
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
//@Scheduled(fixedRate = 10000) // 매 10초마다 실행
@Scheduled(fixedRate = 86400000) // 24시간
@Transactional
public void checkAndCancelSubscriptions() {
    LocalDateTime now = LocalDateTime.now();
    List<Subscription.SubscriptionStatus> statusList = Arrays.asList(
            Subscription.SubscriptionStatus.ACTIVE,
            Subscription.SubscriptionStatus.PAUSED
    );
    List<Subscription> expiredSubscriptions = new ArrayList<>(subscriptionRepository.findByNextBillingDateBeforeAndStatusIn(now, statusList));
    log.info("expiredSubscriptions는" + expiredSubscriptions);
    List<Member> membersToUpdate = new ArrayList<>();

    for (Subscription subscription : expiredSubscriptions) {
        subscription.setStatus(Subscription.SubscriptionStatus.CANCELLED);
        Member member = subscription.getMember();
        if (member != null) {
            member.removeType(MemberType.SUB_USER);
            membersToUpdate.add(member);
        }
    }
    subscriptionRepository.saveAll(expiredSubscriptions);
    memberRepository.saveAll(membersToUpdate);
    for (Member member : membersToUpdate) {
        notifyStatusChange("SUB_USER", member.getId());
    }
}
    
    // 구독 유저 수 가져오는로직:(
    
    public List<StatsResponseForSubMemberDTO> getAllSubMemberLists () {
        List<Subscription> subscriptions = subscriptionRepository.findAll ();
        
        return subscriptions.stream()
                .map(subMember -> {
                    return StatsResponseForSubMemberDTO.builder()
                            .id(subMember.getId())
                            .createdAt (subMember.getCreatedAt ())
                            .memberId (subMember.getMember ().getId ())
                            .build();
                })
                .collect(Collectors.toList());
    }
    }
    


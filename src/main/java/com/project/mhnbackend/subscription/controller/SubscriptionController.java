package com.project.mhnbackend.subscription.controller;


import com.project.mhnbackend.payment.dto.request.PaymentRequestDTO;
import com.project.mhnbackend.subscription.dto.response.SubscriptionResponseDTO;
import com.project.mhnbackend.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscriptions")
    public SubscriptionResponseDTO createSubscription(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        return subscriptionService.createSubscription(paymentRequestDTO);
    }

    @GetMapping("/subscriptions")
    public SubscriptionResponseDTO getSubscription(@RequestParam("userId") Long memberId) {
        return subscriptionService.getSubscription(memberId);
    }

    @GetMapping("/subscribe-status")
    public SseEmitter subscribeToStatusUpdates() {
        SseEmitter emitter = new SseEmitter();

        subscriptionService.addEmitter(emitter);

        emitter.onCompletion(() -> subscriptionService.removeEmitter(emitter));
        emitter.onTimeout(() -> subscriptionService.removeEmitter(emitter));
        emitter.onError((ex) -> subscriptionService.removeEmitter(emitter));

        return emitter;
    }
}

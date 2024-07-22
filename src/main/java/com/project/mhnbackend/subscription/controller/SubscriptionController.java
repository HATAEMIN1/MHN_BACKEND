package com.project.mhnbackend.subscription.controller;


import com.project.mhnbackend.payment.dto.request.PaymentRequestDTO;
import com.project.mhnbackend.subscription.dto.response.SubscriptionResponseDTO;
import com.project.mhnbackend.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscriptions")
    public SubscriptionResponseDTO createSubscription(@RequestBody PaymentRequestDTO paymentRequestDTO) {
        return subscriptionService.createSubscription(paymentRequestDTO);
    }
}

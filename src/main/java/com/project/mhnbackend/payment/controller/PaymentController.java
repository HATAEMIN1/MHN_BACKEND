package com.project.mhnbackend.payment.controller;

import com.project.mhnbackend.payment.domain.Payment;
import com.project.mhnbackend.payment.dto.request.BillingKeyRequestDTO;
import com.project.mhnbackend.payment.dto.request.ImpTokenRequestDTO;
import com.project.mhnbackend.payment.dto.request.PaymentRequestDTO;
import com.project.mhnbackend.payment.dto.request.UnScheduleRequestDTO;
import com.project.mhnbackend.payment.dto.response.ImpTokenResponseDTO;
import com.project.mhnbackend.payment.dto.response.PaymentResponseDTO;
import com.project.mhnbackend.payment.dto.response.UnScheduleResponseDTO;
import com.project.mhnbackend.payment.service.IamportService;
import com.project.mhnbackend.payment.service.PaymentService;
import com.project.mhnbackend.subscription.service.SubscriptionService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RequestMapping("/api/v1")
@RestController
@RequiredArgsConstructor
public class PaymentController {
    private final IamportService iamportService;
    private final SubscriptionService subscriptionService;
    private final PaymentService paymentService;

//    @PostMapping("/iamport/token")
//    public Mono<ImpTokenResponseDTO> getIamportToken() {
//        return iamportService.getToken();
//    }
//    @PostMapping("/payment")
//    public Mono<PaymentResponseDTO> requestPayment(@RequestBody PaymentRequestDTO paymentRequest) {
//        return iamportService.getToken()
//                .flatMap(tokenResponse -> {
//                    String token = tokenResponse.getResponse().getAccess_token();
//                    return iamportService.requestPayment(token, paymentRequest);
//                });
//    }

    @PostMapping("/payments/billing")
    public Mono<PaymentResponseDTO> registerBillingKeyAndSchedulePayment(@RequestBody BillingKeyRequestDTO billingKeyRequestDTO) {
        return iamportService.getToken()
                .flatMap(tokenResponse -> {
                    String token = tokenResponse.getResponse().getAccess_token();
                    return iamportService.registerBillingKeyAndSchedulePayment(token, billingKeyRequestDTO);
                });
    }

//    @PostMapping("/payments/unschedule")
//    public  Mono<UnScheduleResponseDTO> getPayment(@RequestBody UnScheduleRequestDTO unScheduleRequestDTO) {
//        return iamportService.getToken()
//                .flatMap(tokenResponse -> {
//                    String token = tokenResponse.getResponse().getAccess_token();
//                    return iamportService.unSchedule(token, unScheduleRequestDTO);
//                });
//    }

    @PostMapping("/payments/unschedule")
    public Mono<UnScheduleResponseDTO> unschedulePayment(@RequestBody UnScheduleRequestDTO unScheduleRequestDTO) {
        return paymentService.unschedulePayment(unScheduleRequestDTO);
    }



}

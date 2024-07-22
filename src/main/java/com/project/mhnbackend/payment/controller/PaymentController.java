package com.project.mhnbackend.payment.controller;

import com.project.mhnbackend.payment.dto.request.BillingKeyRequestDTO;
import com.project.mhnbackend.payment.dto.request.ImpTokenRequestDTO;
import com.project.mhnbackend.payment.dto.request.PaymentRequestDTO;
import com.project.mhnbackend.payment.dto.response.ImpTokenResponseDTO;
import com.project.mhnbackend.payment.dto.response.PaymentResponseDTO;
import com.project.mhnbackend.payment.service.IamportService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
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

    @PostMapping("/billing")
    public Mono<PaymentResponseDTO> registerBillingKeyAndSchedulePayment(@RequestBody BillingKeyRequestDTO request) {
        return iamportService.getToken()
                .flatMap(tokenResponse -> {
                    String token = tokenResponse.getResponse().getAccess_token();
                    return iamportService.registerBillingKeyAndSchedulePayment(token, request);
                });
    }
}

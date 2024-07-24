package com.project.mhnbackend.payment.service;


import com.project.mhnbackend.payment.dto.request.BillingKeyRequestDTO;
import com.project.mhnbackend.payment.dto.request.ImpTokenRequestDTO;
import com.project.mhnbackend.payment.dto.request.PaymentRequestDTO;
import com.project.mhnbackend.payment.dto.request.UnScheduleRequestDTO;
import com.project.mhnbackend.payment.dto.response.ImpTokenResponseDTO;
import com.project.mhnbackend.payment.dto.response.PaymentResponseDTO;
import com.project.mhnbackend.payment.dto.response.UnScheduleResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class IamportService {

    @Value("${iamport.api.key}")
    private String impKey;

    @Value("${iamport.api.secret}")
    private String impSecret;

    private final WebClient webClient;

    public IamportService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.iamport.kr").build();
    }

    public Mono<ImpTokenResponseDTO> getToken() {
        ImpTokenRequestDTO requestDTO = new ImpTokenRequestDTO(impKey, impSecret);

        return webClient.post()
                .uri("/users/getToken")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .retrieve()
                .bodyToMono(ImpTokenResponseDTO.class);
    }

//    public Mono<PaymentResponseDTO> requestPayment(String token, PaymentRequestDTO paymentRequestDTO) {
//        return webClient.post()
//                .uri("/subscribe/payments/again")
//                .header("Authorization", token)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(paymentRequestDTO)
//                .retrieve()
//                .bodyToMono(PaymentResponseDTO.class);
//    }

    public Mono<PaymentResponseDTO> registerBillingKeyAndSchedulePayment(String token, BillingKeyRequestDTO billingKeyRequestDTO) {
        return webClient.post()
                .uri("/subscribe/customers/{customer_uid}", billingKeyRequestDTO.getCustomer_uid())
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(billingKeyRequestDTO)
                .retrieve()
                .bodyToMono(PaymentResponseDTO.class)
                .flatMap(billingKeyResponse -> {
                    // 빌링키 등록 성공 후 결제 예약
                    return schedulePayments(token, billingKeyRequestDTO);

                });
    }
    private Mono<PaymentResponseDTO> schedulePayments(String token, BillingKeyRequestDTO billingKeyRequestDTO) {
        return webClient.post()
                .uri("/subscribe/payments/schedule")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(billingKeyRequestDTO)
                .retrieve()
                .bodyToMono(PaymentResponseDTO.class);
    }


    public Mono<UnScheduleResponseDTO> unSchedule(String token, UnScheduleRequestDTO unScheduleRequestDTO) {
        return webClient.post()
                .uri("/subscribe/payments/unschedule")
                .header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(unScheduleRequestDTO)
                .retrieve()
                .bodyToMono(UnScheduleResponseDTO.class);
    }
}

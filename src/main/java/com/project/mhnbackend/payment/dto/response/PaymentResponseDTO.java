package com.project.mhnbackend.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
public class PaymentResponseDTO {
    private int code;
    private String message;
    private List<PaymentData> response;  // 변경된 부분

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PaymentData {
        private String imp_uid;
        private String merchant_uid;
        private int amount;
        private String customer_uid;
    }
